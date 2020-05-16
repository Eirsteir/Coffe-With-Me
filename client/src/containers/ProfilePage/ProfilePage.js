import React from "react";
import {withRouter} from "react-router-dom";

import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";

import {handleResponse} from "../../services/user.service";

class ProfilePage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {
                name: "",
                email: "",
                username: "",
            }
        }
    }

    componentDidMount() {
        const { match: { params } } = this.props;
        console.log(this.props);
        
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

    render() {
        const user = this.state.user;

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
                </Grid>
            </div>
        );
    }
}

export default withRouter(ProfilePage);