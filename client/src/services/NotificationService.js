import { getAuthToken } from '../helpers/auth-headers';
import { handleResponse } from "./user.service";


const NotificationType = Object.freeze({
    FRIENDSHIP_REQUESTED: 'FRIENDSHIP_REQUESTED',
    FRIENDSHIP_ACCEPTED: 'FRIENDSHIP_ACCEPTED'
});

class NotificationService {

    static getNotifications = async () => {
        return fetch(`/api/notifications`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: getAuthToken()
            }
        })
            .then(handleResponse);
    }

    static getNotificationMessage = notification => {
        const name = notification.user.name;

        switch (notification.type) {
            case (NotificationType.FRIENDSHIP_REQUESTED):
                return `New friend request from ${name}`;
            case (NotificationType.FRIENDSHIP_ACCEPTED):
                return `${name} accepted your friend request`;
            default:
                return "";
        }
    }

    // todo: generify for other notification types
    static getPath = notification => {
        return `/users/${notification.user.id}`
    }

    // static updateNotificationReadState = async (id, newState, callback = null) => {
    //     const response = this.updateNotification(
    //         id,
    //         {read: newState},
    //     ).response();
    //     return response.then((data) => {
    //       !callback || callback(response.isError === true, data);
    //       return data;
    //     });
    //   }
  
}


export default NotificationService;