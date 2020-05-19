import React from 'react';

import TabPanel from './TabPanel';
import FriendsList from "./FriendsList";
import { handleResponse } from "../../services/user.service";

class FriendsTabPanel extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userId: "",
            friends: [],
        };
    }

    componentDidMount() {
        const token = window.localStorage.getItem("auth");       
        this.fetchFriends(token);
    }

    fetchFriends = (token)  => {        
        fetch(`/api/${encodeURIComponent(this.props.userId)}/friends`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: token
            }
        })
            .then(handleResponse)
            .then(friends => {
                if (friends.length) {
                    return this.setState({ friends: friends})
                }
            })
            .catch(console.log);
    };


    render() {
        const { index, value, isAuthenticated } = this.props;
        
        return (
            <TabPanel 
                index={index} 
                value={value}
                children={<FriendsList friends={this.state.friends} isAuthenticated={isAuthenticated} />}/>
        )
    }
}

export default FriendsTabPanel;