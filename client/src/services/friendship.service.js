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

export function addFriend(toFriend) {

    return fetch(`/api/friends?to_friend=${encodeURIComponent(toFriend)}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuthToken()
        }
    })
        .then(handleResponse);
}

export function getFriendRequests() {

    return fetch(`/api/friends/requests`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuthToken()
        }
    })
        .then(handleResponse);
}