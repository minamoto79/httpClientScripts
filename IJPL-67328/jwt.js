/**
 * Base64URL encode a string.
 *
 * @param input {string} The string to encode.
 * @returns {string} The Base64URL encoded string.
 */
import {jwa} from "./jwa";




function rawSign(header, payload, secretOrPrivateKey) {
    const encodedPayload = btoaUrl(JSON.stringify(payload));
    const encodedHeader = btoaUrl(JSON.stringify(header));

    const unsignedToken = `${encodedHeader}.${encodedPayload}`;
    const signature = jwa(header.alg).sign(unsignedToken, secretOrPrivateKey)

    return `${unsignedToken}.${signature}`;
}

const signingOptionsSchema = {
    expiresIn: value => typeof value === 'number',
    audience: value => typeof value === 'string' || Array.isArray(value),
    algorithm: value => SUPPORTED_ALGS.includes(value),
    header: value => typeof value === 'object',
    encoding: value => typeof value === 'string',
    issuer: value => typeof value === 'string',
    subject: value => typeof value === 'string',
    jwtid: value => typeof value === 'string',
    keyid: value => typeof value === 'string',
    noTimestamp: value => typeof value === 'boolean',
};

const verifyOptionsSchema = {
    algorithm: value => SUPPORTED_ALGS.includes(value),
    audience: value => typeof value === 'string',
    complete: value => typeof value === 'boolean',
    issuer: value => typeof value === 'string',
    jwtid: value => typeof value === 'string',
    ignoreExpiration: value => typeof value === 'number',
    ignoreNotBefore: value => typeof value === 'number',
    clockTolerance: value => typeof value === 'number',
    clockTimestamp: value => typeof value === 'number',
    none: value => typeof value === 'string',
}

const claimSchema = {
    iat: value => typeof value === 'number',
    exp: value => typeof value === 'number',
    nbf: value => typeof value === 'number',
};

const SUPPORTED_ALGS = ['RS256', 'RS384', 'RS512', 'ES256', 'PS256', 'PS384', 'PS512', 'ES384', 'ES512', 'HS256', 'HS384', 'HS512', 'none'];


const signingOptions2Payload = {
    'audience': `aud`,
    'issuer': 'iss',
    'subject': 'sub',
    'jwtid': 'jti'
};

const signingOptions4Objects = [
    'expiresIn',
    'notBefore',
    'noTimestamp',
    'audience',
    'issuer',
    'subject',
    'jwtid',
];

function timespan(time, iat) {
    if (typeof time === 'string') {
        throw `node/ms format unsupported (IJPL-202577)`
    } else if (typeof time === 'number') {
        return time + iat
    }
    throw `unsupported time type: ${typeof time}`
}

function validatePayload(payload) {
    validate(payload, claimSchema, 'payload');
}

function validateSigningOptions(options) {
    validate(options, signingOptionsSchema, 'sign::options');
}

function validateVerifyOptions(options) {
    validate(options, verifyOptionsSchema, 'verify::options');
}

function validateJwtString(jwtString) {
    if (typeof jwtString !== 'string') {
        throw `Error: expected a string, but got ${jwtString}`;
    }
    if (jwtString.split('.').length !== 3) {
        throw `Error: ${jwtString} is malformed`
    }
}

function validate(object, schema, name) {
    if (typeof object !== 'object') {
        throw `${name} parameter supposed to be an object`;
    }
    Object.keys(object).forEach(key => {
        if (schema[key]) {
            if(!schema[key](object[key])) {
                throw `Error: Unsupported type (${typeof object[key]}) of ${name}.${key}`;
            }
        }
    })
}



export const jwt = {
    sign: (payload, secretOrPrivateKey, options = {algorithm: 'HS256'}) => {
        const header = Object.assign({
            alg: options.algorithm,
            typ: 'JWT',
            kid: options.keyid
        }, options.header);

        let isObjectPayload = typeof payload === 'object';
        if (!payload) {
            throw `Error: payload is required`
        } else if (isObjectPayload) {
            validatePayload(payload)
        } else {
            const invalidOptions = signingOptions4Objects.filter(opt => typeof options[opt] !== 'undefined')
            if (invalidOptions.length > 0) {
                throw `Error: invalid ${invalidOptions.join(',')} option for payload: ${JSON.stringify(payload)}`
            }

            if (typeof payload.exp !== 'undefined' && typeof options.expiresIn !== 'undefined' ) {
                throw `Error: "payload.exp" already in payload ("options.expiresIn").`
            }

            if (typeof payload.nbf !== 'undefined' && typeof options.notBefore !== 'undefined' ) {
                throw `Error: "payload.nbf" already in payload ("options.notBefore").`
            }
        }

        validateSigningOptions(options)

        const timestamp = payload.iat || Math.floor(Date.now() / 1000);

        if (options.noTimestamp) {
            delete payload.iat;
        } else if (isObjectPayload) {
            payload.iat = timestamp;
        }

        if (options.notBefore) {
            payload.nbf = timespan(options.notBefore, timestamp);
        }

        if (options.expiresIn && isObjectPayload) {
            payload.exp = timespan(options.expiresIn, timestamp);
        }

        Object.keys(signingOptions2Payload).forEach(key => {
            const claim = signingOptions2Payload[key];
            if (!options[key]) {
                if (payload[claim]) {
                    throw `Error: option ${key} already in payload ${claim}`;
                }
                payload[claim] = options[key];
            }
        })
        return rawSign(header, payload, secretOrPrivateKey);
    },
    verify: (jwtString, secretOrPublicKey, options = {algorithm: 'HS256'}) => {
        validateJwtString(jwtString);
        const [encodedPayload, encodedHeaded, signature] = jwtString.split('.');
        const jwt = {encodedPayload, encodedHeaded, signature};
        const clockTimestamp = options.clockTimestamp || Math.floor(Date.now() / 1000);
        validateVerifyOptions(options);

        const decodedToken = {
            header: JSON.parse(atobUrl(encodedHeaded, c => c.charCodeAt(0))),
            payload: JSON.parse(atobUrl(encodedPayload, c => c.charCodeAt(0))),
            signature: signature
        };

        let payload = decodedToken.payload;
        if (payload.nbf && !options.ignoreNotBefore) {
            if (typeof payload.nbf !== 'number') {
                throw `Error: invalid nbf value ${typeof payload.nbf}`;
            }
            if (payload.nbf > clockTimestamp + (options.clockTolerance || 0)) {
                throw `Error: jwt isn't active: ${new Date(payload.nbf * 1000)}`
            }
        }

        if (payload.exp && !options.ignoreExpiration) {
            if (typeof payload.exp !== 'number') {
                throw `Error: invalid exp value ${typeof payload.exp}`;
            }
            if (clockTimestamp > payload.exp + (options.clockTolerance || 0)) {}
                throw `Error: jwt is expired: ${new Date(payload.exp * 1000)}`
        }

        if (options.audience) {
            const audience = Array.isArray(options.audience) ? options.audience : [options.audience];
            const target = Array.isArray(payload.aud) ? payload.aud : [payload.aud]
            if (!target.some(ta => audience.some(a => a === ta))) {
                throw `Error: requested audience ${JSON.stringify(audience)} doesn't match the target audience [${target.join(',')}]`
            }
        }

        if (options.issuer) {
            if (options.issuer !== payload.iss ||
                (Array.isArray(options.issuer) && options.issuer.indexOf(payload.iss) === -1)) {
                throw `Error: jwt issuer(${payload.iss}) is invalid. expected: ${JSON.stringify(options.issuer)}`
            }
        }

        if (options.subject) {
            if (payload.sub !== options.subject) {
                throw `Error: subject ${payload.sub} is invalid. expected: ${options.subject}`
            }
        }

        if (options.jwtid) {
            if (payload.jti !== options.jwtid) {
                throw `Error: jwtid ${payload.jti} is invalid. expected: ${options.jwtid}`
            }
        }

        if (options.nonce) {
            if (payload.nonce !== options.nonce) {
                throw `Error: jwt nonce ${payload.nonce} is invalid. expected: ${options.nonce}`
            }
        }

        return jwa(options.algorithm).verify(`${jwt.encodedPayload}.${jwt.encodedHeaded}`, secretOrPublicKey, signature);
    },
    decode: (jwtString, options = {}) => {
        const [encodedPayload, encodedHeaded, signature] = jwtString.split('.')
        return {
            header: atobUrl(encodedHeaded, c => c.charCodeAt(0)),
            payload: atobUrl(encodedPayload, c => c.charCodeAt(0)),
            signature: signature
        }
    }
}

