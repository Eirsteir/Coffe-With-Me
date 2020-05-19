import React from "react";
import {Link} from "react-router-dom";

import {withStyles} from "@material-ui/core/styles";
import createMuiTheme from "@material-ui/core/styles/createMuiTheme";
import CircularProgress from "@material-ui/core/CircularProgress";
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';

import { FriendshipStatus, updateFriendRequest } from "../../services/friendship.service";



const theme = createMuiTheme({
    spacing: 4
});
const styles = {
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
        color: "#fff"
      },
    
    select: {
        marginTop: theme.spacing(2),
        '&:before': {
            borderColor: "#A4A9B3",
        },
        '&:after': {
            borderColor: "#A4A9B3",
        },
        color: "#A4A9B3"
    },
    icon: {
        fill: "#A4A9B3",
    }
};

class FriendRequestItem extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            friendshipStatus: "",
            isLoading: false,
            errorMessage: "",
        }
    }

    toggleLoading = () => {
        this.setState((prevState, props) => ({
            isLoading: !prevState.isLoading
        }));
    };

    handleChange = event => {
        this.setState({ friendshipStatus: event.target.value })
        const action = this.state.friendshipStatus;

        switch (action) {
            case Action.ACCEPT:
                updateFriendRequest(FriendshipStatus.ACCEPT);
                break;
            case Action.DECLINE:
                updateFriendRequest(FriendshipStatus.DECLINE);
                break;
            case Action.BLOCK:
                updateFriendRequest(FriendshipStatus.BLOCK);
                break;
            default:
                return;
        }
    }

    render() {
        const { classes, friend, userId, isAuthenticated } = this.props;
        const { friendshipStatus } = this.state;

        return (
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                                        alignItems: "center",
                    height: "2.5rem",
                    fontSize: ".9rem"
                }}
            >
                <Link 
                    to={{
                        pathname: `/users/${friend.id}`,
                        state: {
                            userId: userId,
                            friendId: friend.id,
                            isAuthenticated: isAuthenticated
                        }  
                    }}
                    style={{
                        textDecoration: "none",
                        fontSize: ".9rem",
                        letterSpacing: 1
                    }}
                >
                
                    {friend.username == null ? friend.name  : friend.username}
                
                </Link>
    
                {this.state.isLoading ? (
                        <CircularProgress style={{ color: "secondary" }} size={20} />
                    ) : (
                        <FormControl className={classes.formControl}>
                        <InputLabel>Update</InputLabel>
                            <Select
                                value={friendshipStatus}
                                onChange={this.handleChange}
                                displayEmpty
                                autoWidth
                                className={classes.select}
                                inputProps={{
                                    'aria-label': 'Friend request operations',
                                    classes: {
                                        icon: classes.icon,
                                    },
                                }}
                            >
                                <MenuItem value={FriendshipStatus.ACCEPTED}>Accept</MenuItem>
                                <MenuItem value={FriendshipStatus.DECLINED}>Decline</MenuItem>
                                <MenuItem value={FriendshipStatus.BLOCKED}>Block</MenuItem>
                            </Select>
                        </FormControl>
                )}
            </div>
        );
    }
};


export default withStyles(styles)(FriendRequestItem);