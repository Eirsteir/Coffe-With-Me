export function getAuthToken() {
    return window.localStorage.getItem("auth");
}

export function createBasicAuthToken(email, password) {
    return "Basic " + window.btoa(email + ":" + password);
}

export function saveAuthTokenInLocal(authData) {
    window.localStorage.setItem("auth", authData);
}