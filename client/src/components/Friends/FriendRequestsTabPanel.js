import React from 'react';

import TabPanel from './TabPanel';
import FriendsList from "./FriendsList";
import { handleResponse } from "../../services/user.service";

class FriendRequestsTabPanel extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userId: "",
            friends: [],
        };

        const token = window.localStorage.getItem("auth");
        this.fetchFriends(token);
    }

    fetchFriends = token  => {        
        fetch(`/api/friends/requests`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: token
            }
        })
            .then(handleResponse)
            .then(friends => {
                console.log(friends);
                
                if (friends.length) {
                    return this.setState({ friends: friends})
                }
            })
            .catch(console.log);
    };


    render() {
        const { index, value } = this.props;

        
        return (
            <TabPanel 
                index={index} 
                value={value}
                children={<FriendsList friends={this.state.friends} />}/>
        )
    }
}

export default FriendRequestsTabPanel;