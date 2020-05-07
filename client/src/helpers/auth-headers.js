export function getAuthTokenHeader() {
    // return authorization header with basic auth credentials
    let user = JSON.parse(localStorage.getItem('auth'));

    if (user && user.authdata) {
        return { 'Authorization': 'Basic ' + user.authdata };
    } else {
        return {};
    }
}

export function createBasicAuthToken(email, password) {
    return "Basic " + window.btoa(email + ":" + password);
}

export function saveAuthTokenInLocal(authData) {
    window.localStorage.setItem("auth", authData);
}