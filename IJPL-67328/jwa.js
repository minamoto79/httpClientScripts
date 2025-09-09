function normalizeData(data) {
    if (typeof data === 'object') {
        return JSON.stringify(data);
    } else
        return data;
}

function createHmacSigner(bits) {
    return (data, secret) => {
        const str = normalizeData(data)
        return crypto.hmac[`sha${bits}`]()
            .withTextSecret(secret)
            .updateWithText(str)
            .digest().toBase64(true);
    }
}

function createHmacVerifier(bits) {
    return (data, secret, signature) => {
        const signatureToCheck = createHmacSigner(bits)(data, secret)
        return signature === signatureToCheck;
    }
}

function createNoneSigner() {
    return () => {
        return '';
    }
}

function createNoneVerifier() {
    return (_, signature) => {
        return signature === '';
    }
}

function createRsaPssSigner(bits) {
    return (data, privateKey) => {
        const str = normalizeData(data)
        const signature = crypto.subtle.sign({
                name: 'RSA-PSS',
                saltLength: 32,
            },
            privateKey,
            Uint8Array.from(str, c => c.charCodeAt(0)))
        return btoaUrl(String.fromCharCode(...new Uint8Array(signature)))
    }
}

function createRsaPssVerifier(bits) {
    return (data, publicKey, signature) => {
        const str = normalizeData(data)
        const sig = Uint8Array.from(atobUrl(signature), a => a.charCodeAt(0))
        console.log(`signature: ${signature}, sig: ${sig}`)
        return crypto.subtle.verify({
            name: 'RSA-PSS',
            saltLength: 32,
        }, publicKey, sig, str)
    }
}

function createRsaSsaSigner(bits) {
    return (data, privateKey) => {
        const str = normalizeData(data)
        const signature = crypto.subtle.sign({
                name: 'RSASSA-PKCS1-v1_5'
            }, privateKey,
            Uint8Array.from(atob(str), a => a.charCodeAt(0)))
        return btoaUrl(String.fromCharCode(...new Uint8Array(signature)))
    }
}

function createRsaSsaVerifier(bits) {
    return (data, publicKey, signature) => {
        const str = normalizeData(data)
        return crypto.subtle.verify({
                name: 'RSASSA-PKCS1-v1_5'
            },
            publicKey,
            signature,
            Uint8Array.from(atob(str), a => a.charCodeAt(0)))
    }
}

function createEcdsaSigner(bits) {
    return (data, privateKey) => {
        const str = normalizeData(data)
        const signature = crypto.subtle.sign({
                name: 'ECDSA',
                hash: {name: `SHA-${bits}`}
            },
            privateKey,
            Uint8Array.from(atob(str), a => a.charCodeAt(0)))
        return btoaUrl(String.fromCharCode(...new Uint8Array(signature)))
    }
}

function createEcdsaVerifier(bits) {
    return (data, publicKey, signature) => {
        const str = normalizeData(data)
        return crypto.subtle.verify({
            name: 'ECDSA',
            hash: {name: `SHA-${bits}`}
        },
            publicKey,
            signature,
            Uint8Array.from(atob(str), a => a.charCodeAt(0)))
    }
}

export function jwa(algorithm) {
    const signers = {
        hs: createHmacSigner,
        rs: createRsaSsaSigner,
        ps: createRsaPssSigner,
        es: createEcdsaSigner,
        none: createNoneSigner
    }
    const verifiers = {
        hs: createHmacVerifier,
        rs: createRsaSsaVerifier,
        ps: createRsaPssVerifier,
        es: createEcdsaVerifier,
        none: createNoneVerifier
    }
    const match = algorithm.match(/^(RS|PS|ES|HS)(256|384|512)$|^(none)$/);
    if (!match)
        throw new Error(`unsupported algorithm: ${algorithm}`);
    const algo = (match[1] || match[3]).toLowerCase();
    const bits = match[2];

    return {
        sign: signers[algo](bits),
        verify: verifiers[algo](bits),
    }
}