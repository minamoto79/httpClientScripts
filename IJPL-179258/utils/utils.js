export function assertStatusCode(code) {
    client.assert(response.status === code)
}