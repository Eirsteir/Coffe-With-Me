import { getAuthToken } from '../helpers/auth-headers';
import { handleResponse } from "./user.service";


export const FriendshipStatus = {
    ACCEPTED: "ACCEPTED",
    DECLINED: "DECLINED",
    BLOCKED: "BLOCKED",
}

export function updateFriendRequest(requesterId, addresseeId, status) {

    const requestOptions = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuthToken()
        },
        body: JSON.stringify({
            requesterId: requesterId,
            addresseeId: addresseeId,
            status: status
        })
    }

    return fetch(`api/friends`, requestOptions)
    .then(handleResponse);
}