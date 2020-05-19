import React from "react";
import {Link} from "react-router-dom";

import {withStyles} from "@material-ui/core/styles";
import createMuiTheme from "@material-ui/core/styles/createMuiTheme";
import CircularProgress from "@material-ui/core/CircularProgress";
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import CheckIcon from '@material-ui/icons/Check';

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
            isFriendRequestUpdated: false
        }
    }

    toggleLoading = () => {
        this.setState((prevState, props) => ({
            isLoading: !prevState.isLoading
        }));
    };

    handleChange = event => {
        this.setState({ friendshipStatus: event.target.value })
        this.handleUpdateFriendRequest(event.target.value);
    }

    handleUpdateFriendRequest = newStatus => {
        const requesterId = this.props.friend.id;
        const addresseeId = this.props.userId;
        
        this.toggleLoading();
        updateFriendRequest(requesterId, addresseeId, newStatus)
        .then(resp => {
            this.toggleLoading();
            this.setState({ isFriendRequestUpdated: true })
        })
        .catch(err => {
            console.log(err);
            this.setState({ errorMessage: err.message })
        })
    }

    render() {
        const { classes, friend, userId, isAuthenticated } = this.props;
        const { friendshipStatus, isFriendRequestUpdated } = this.state;

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
                    ) : isFriendRequestUpdated ? (
                            <CheckIcon />
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