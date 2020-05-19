import React from "react";
import { withRouter, Redirect } from "react-router-dom";

import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";

import FriendSelect from "../../components/Friends/FriendSelect";
import AddFriendSelect from "../../components/Friends/AddFriendButton";
import {handleResponse} from "../../services/user.service";

class ProfilePage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userId: "",
            user: {
                id: "",
                name: "",
                email: "",
                username: "",
                isFriend: false,
            }
        }
    }

    componentDidMount() {
        const { match: { params } } = this.props;        
        this.getUser(params.id);
    }

    getUser = id => {        
        const token = window.localStorage.getItem("auth");

        fetch(`/api/users/${encodeURIComponent(id)}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: token
            }
        })
            .then(handleResponse)
            .then(user => {
                this.setState({ user });
            })
            .catch(console.log);
    };

    handleAddFriend = () => {
        const token = window.localStorage.getItem("auth");
        
        fetch(`/api/friends?to_friend=${encodeURIComponent(this.state.user.id)}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: token
            }
        })
            .then(handleResponse)
            .then(friendship => {
                console.log(friendship);
                
            })
            .catch(console.log);    
        }

    hasSentFriendRequest = () => {
        return false;
    }

    render() {
        const { userId } = this.props;
        const { isAuthenticated } = this.props.location.state;
        const user = this.state.user;        

        if (!isAuthenticated) {
            return <Redirect to="/" />;
        } else if (user.id === userId) {
            return <Redirect to="/me" />;
        }

        return (
            <div id="landing-page-container">
                <Grid
                    container
                    spacing={1}
                    direction="column"
                    alignItems="center"
                    justify="center"
                    style={{
                        minHeight: '70vh'
                    }}
                >
                    <Grid item>
                        <Typography
                            variant="h4"
                            style={{
                                color: "#fff",
                                fontWeight: "bold"
                            }}
                        >
                            {user.name}
                        </Typography>
                    </Grid>

                    { user.username && (
                        <Grid item>
                            <Typography
                                variant="subtitle1"
                                style={{
                                    color: "#fff",
                                }}
                            >
                                {user.username}
                            </Typography>
                        </Grid>
                    )}

                    <Grid item>
                        <Typography
                            variant="subtitle1"
                            style={{
                                color: "#fff",
                            }}
                        >
                            {user.email}
                        </Typography>
                    </Grid>

                    { 
                        user.isFriend 
                        ? <FriendSelect id={userId} friendId={user.id} />  
                        : <AddFriendSelect id={userId} friendId={user.id} onClick={this.handleAddFriend} />
                    }

                </Grid>
            </div>
        );
    }
}

export default withRouter(ProfilePage);