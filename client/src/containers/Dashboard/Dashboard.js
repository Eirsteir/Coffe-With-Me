import React from "react";
import { Redirect } from "react-router-dom";

import "./Dashboard.css";

class Dashboard extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const token = window.localStorage.getItem("auth");
        this.fetchFriends(token);
    }

    fetchFriends = token => {
        fetch(`/api/user/friends`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: token
            }
        })
            .then(response => {
                if (response.status === 400) {
                    return this.toggleLoading();
                }
                return response.json();
            })
            .then(friends => {
                this.toggleLoading();
                if (friends) {
                    return this.setState({ friends: friends})
                }
            })
            .catch(err => {
                this.toggleLoading();
            });
    };
    
    render() {
        const { loadUser, user } = this.props;

        if (!this.props.isAuthenticated) {
            return <Redirect to="/" />;
        }

        return (
            <div className="dashboard-container">
                <h2>Welcome {user.name}</h2>
                { this.state.friends && (
                    <ul>
                        {this.state.friends.map(friend => (
                            <li key={friend.id}>
                                <div>{friend.id}</div>
                                <div>{friend.name}</div>
                                { friend.username ? <div>{friend.username}</div> :  null}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        );
    }
}

export default Dashboard;