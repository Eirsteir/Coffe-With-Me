import React from "react";
import { withRouter, Redirect } from "react-router-dom";

import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import CheckIcon from '@material-ui/icons/Check';

import FriendSelect from "../../components/Friends/FriendSelect";
import AddFriendSelect from "../../components/Friends/AddFriendButton";
import { handleResponse } from "../../services/user.service";
import { addFriend } from "../../services/friendship.service";
import { toggleLoading } from "../../helpers/loading";
import AuthService from "../../services/AuthService";


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
            },
            errorMessage: "",
            isLoading: "",
            friendRequestSent: false
        }
    }

    componentDidMount() {
        const { match: { params } } = this.props;        
        this.getUser(params.id);
    }

    getUser = id => {    
        toggleLoading(this);    
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
                toggleLoading(this);
                this.setState({ user });
            })
            .catch(err => {
                toggleLoading(this);
                this.setState({ errorMessage: err.message })
            });
    };

    handleAddFriend = () => {
        toggleLoading(this);
        addFriend(this.state.user.id)
            .then(friendship => {
                toggleLoading(this);
                this.setState({ friendRequestSent: true });
            })
            .catch(err => {
                toggleLoading(this);
                this.setState({ errorMessage: err.message })
            });    
        }

    hasSentFriendRequest = () => {
        return false;
    }

    render() {
        const { userId } = this.props;
        const { user, friendRequestSent } = this.state;        
        
        if (!AuthService.isAuthenticated()) {
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
                        : friendRequestSent 
                            ? <CheckIcon style={{ color: "#e22866", marginTop: "1rem" }} /> 
                            : <AddFriendSelect id={userId} friendId={user.id} onClick={this.handleAddFriend} />
                    }

                </Grid>
            </div>
        );
    }
}

export default withRouter(ProfilePage);