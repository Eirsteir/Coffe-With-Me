import React from "react";
import {withRouter} from "react-router-dom";

import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";

import MyFriendshipsTabs from "../../components/Friends/MyFriendshipsTabs";

class MyProfilePage extends React.Component {

    render() {
        const { user, isAuthenticated } = this.props;       

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

                    { user.nickname && (
                        <Grid item>
                            <Typography
                                variant="subtitle1"
                                style={{
                                    color: "#fff",
                                }}
                            >
                                {user.nickname}
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
                            isAuthenticated={isAuthenticated}
                        />
                        
                    </Grid>
                </Grid>
            </div>
        );
    }
}

export default withRouter(MyProfilePage);