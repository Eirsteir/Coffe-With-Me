import React from "react";
import {withRouter} from "react-router-dom";

import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";

import MyFriendshipsTabs from "../../components/Friends/MyFriendshipsTabs";
import {handleResponse} from "../../services/user.service";

class MyProfilePage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {
                id: "",
                name: "",
                email: "",
                username: "",
            },
            friends: [],
            friendRequests: []
        }
    }

    componentDidMount() {
        const token = window.localStorage.getItem("auth");

        this.fetchFriendRequests(token);
    }

    fetchFriendRequests = token  => {
        fetch(`/api/friends/requests`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: token
            }
        })
            .then(handleResponse)
            .then(requests => {
                if (requests.length) {
                    return this.setState({ friendRequests: requests})
                }
            })
            .catch(console.log);
    };
    
    render() {
        const { user } = this.props;

        return (
            <div>
                <Grid
                    container
                    spacing={1}
                    direction="column"
                    alignItems="center"
                    justify="flex-start"
                    style={{
                        minHeight: '70vh',
                        marginTop: "2rem"
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

                    <Grid 
                        item
                        style={{
                            marginTop: "1rem"
                        }}
                    >

                        <MyFriendshipsTabs 
                            userId={user.id}
                        />
                        
                    </Grid>
                </Grid>
            </div>
        );
    }
}

export default withRouter(MyProfilePage);