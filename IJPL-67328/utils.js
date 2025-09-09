export function stringToByteArray(str) {
    return Uint8Array.from(str)
}

export function byteArrayToString(array) {
    let strBuffer = '';
    const chunk = 0x8000; // avoid call-arg limits
    for (let i = 0; i < array.length; i += chunk) {
        strBuffer += String.fromCharCode(...array.subarray(i, i + chunk));
    }
    return strBuffer;
}