import { getAuthToken } from '../helpers/auth-headers';
import { handleResponse } from "./user.service";


export const FriendshipStatus = Object.freeze({
    ACCEPTED,
    DECLINED,
    BLOCKED,
});

export function updateFriendRequest(requesterId, addresseeId, status) {

    const requestOptions = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuthToken()
        },
        body: JSON.stringify({
            requester: 
        })
    }

    return fetch(`api/logout`, requestOptions)
    .then(handleResponse);
}