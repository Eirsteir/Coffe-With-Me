import React from 'react';

import TabPanel from './TabPanel';
import FriendRequestsList from "./FriendRequestsList";
import { handleResponse } from "../../services/user.service";

class FriendRequestsTabPanel extends React.Component {
    _isMounted = false;

    constructor(props) {
        super(props);
        this.state = {
            userId: "",
            friends: [],
        };

    }

    componentDidMount() {
        this._isMounted = true;

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
            .then(resp => {               
                if (resp.length && this._isMounted) {
                    return this.setState({ friends: resp})
                }
            })
            .catch(console.log);
    };
        
    componentWillUnmount() {
        this._isMounted = false;
      }

    render() {
        const { index, value, isAuthenticated } = this.props;
        
        return (
            <TabPanel 
                index={index} 
                value={value}
                children={<FriendRequestsList friends={this.state.friends} isAuthenticated={isAuthenticated} />}/>
        )
    }
}

export default FriendRequestsTabPanel;