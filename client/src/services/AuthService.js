

class AuthService {

    static TOKEN_NAME = "auth";

    static getToken = () => {
        return window.localStorage.getItem(this.TOKEN_NAME);
    }

    static _removeToken = () => {
        window.localStorage.removeItem(this.TOKEN_NAME);
    }

    static isAuthenticated = () => {        
        return typeof this.getToken() !== 'undefined';
      }

    static logout = async (callback = null, ...args) => {
        
        fetch(`/api/logout`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: this.getToken
            }
        })
            .then(resp => {
                if (resp.status === 204 || resp.status === 304) {
                    return callback.apply(args[1]);
                }
            })
            .catch(console.log);
    }
}

export default AuthService;