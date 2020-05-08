import {createBasicAuthToken, getAuthTokenHeader, saveAuthTokenInLocal} from '../helpers/auth-headers';

export const userService = {
    login,
    logout,
    getAll
};

function login(email, password) {
    const requestOptions = {
        method: 'GET',
        headers: { 'Content-Type': 'application/json',
                    'Authorization': createBasicAuthToken(email, password)
        }
    };

    return fetch(`api/user`, requestOptions)
        .then(handleResponse)
        .then(user => {
            // login successful if there's a user in the response
            if (user) {
                // store user details and basic auth credentials in local storage
                // to keep user logged in between page refreshes
                user.authdata = window.btoa(user.email + ':' + user.password);
                saveAuthTokenInLocal(JSON.stringify(user));
            }

            return user;
        });
}

function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('auth');
}

function getAll() {
    const requestOptions = {
        method: 'GET',
        headers: getAuthTokenHeader()
    };

    // todo
    return fetch(`api/users`, requestOptions).then(handleResponse);
}

export function handleResponse(response) {
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        if (!response.ok) {
            if (response.status === 401) {
                // auto logout if 401 response returned from api
                logout();
                this.props.history.push("/");
            }

            const error = (data && data.details) || response.statusText;
            return Promise.reject(error);
        }

        return data;
    });
}