import React from "react";
import { Redirect } from "react-router-dom";

import FriendsList from "../../components/Friends/FriendsList"
import "./Dashboard.css";

const initialState = {
    isAuthenticated: false,
    user: {
        id: "",
        name: "",
        email: "",
        username: "",
    },
    friends: [],
    isLoading: false
};
class Dashboard extends React.Component {
    constructor(props) {
        super(props);
        this.state = initialState;

        this.props.loadUser(this.props.user);
        const token = window.localStorage.getItem("auth");
        this.fetchFriends(token);
    }
    
    fetchFriends = token => {
        fetch(`/api/${encodeURIComponent(this.props.user.id)}/friends`, {
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

    toggleLoading = () => {
        this.setState((prevState, props) => ({
            isLoading: !prevState.isLoading
        }));
    };

    render() {
        const { loadUser, user } = this.props;

        if (!this.props.isAuthenticated) {
            return <Redirect to="/" />;
        }

        return (
            <div className="dashboard-container">
                <h2>Welcome {user.name}</h2>
                <FriendsList friends={this.state.friends} />
            </div>
        );
    }
}

export default Dashboard;