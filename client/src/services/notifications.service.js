import { getAuthToken } from '../helpers/auth-headers';
import { handleResponse } from "./user.service";


export function getNotifications() {

    return fetch(`/api/notifications`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuthToken()
        }
    })
        .then(handleResponse);
}