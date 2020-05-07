import React from "react";
import { withRouter } from "react-router-dom";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import MenuIcon from "@material-ui/icons/Menu";
import SwipeableDrawer from "@material-ui/core/SwipeableDrawer";
import List from "@material-ui/core/List";
import Divider from "@material-ui/core/Divider";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import HomeOutlinedIcon from "@material-ui/icons/Home";
import PersonIcon from "@material-ui/icons/Person";


const styles = theme => ({
    root: {
        flexGrow: 1
    },
    grow: {
        flexGrow: 1,
        fontWeight: 300
    },
    appBar: {
        boxShadow: "none",
        // backgroundColor: "#343b64"
        backgroundColor: "transparent",
        padding: "1rem 6rem",
        [theme.breakpoints.down("sm")]: {
            padding: "0"
        }
    },
    menuButton: {
        marginRight: "-1rem"
    },
    list: {
        width: 250
    },
    label: {
        textTransform: "capitalize",
        backgroundColor: "transparent",
        fontSize: "1.1rem",
        marginRight: "1rem",
        [theme.breakpoints.down("sm")]: {
            marginRight: "0"
        }
    }
});

class Navigation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            open: false,
            isSignedIn: this.props.isSignedIn
        };
    }

    toggleDrawer = () => {
        this.setState(prevState => ({
            open: !prevState.open
        }));
    };

    handleClick = route => {
        return this.props.history.push(route);
    };

    handleLogout = () => {
        const token = window.localStorage.getItem("token");
        this.props.toggleSigninState();

        fetch(`/signout`, {
            method: "post",
            headers: {
                "Content-Type": "application/json",
                Authorization: token
            }
        })
            .then(resp => {
                if (resp.status === 200 || resp.status === 304) {
                    window.localStorage.removeItem("token");
                    return this.props.history.push("/");
                }
            })
            .catch(console.log);
    };

    render() {
        const { classes, isSignedIn } = this.props;
        const iOS = process.browser && /iPad|iPhone|iPod/.test(navigator.userAgent);
        const sideList = (
            <div className={classes.list}>
                <List component="nav">
                    <ListItem button onClick={() => this.handleClick("/home")}>
                        <ListItemIcon>
                            <HomeOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary="Home" />
                    </ListItem>
                </List>

                <Divider />

                <List component="nav">
                    <ListItem button onClick={() => this.handleClick("/account")}>
                        <ListItemIcon>
                            <PersonIcon />
                        </ListItemIcon>
                        <ListItemText primary="My account" />
                    </ListItem>
                </List>

                <Divider />

                <Divider />
                <List component="nav">
                    <ListItem button>
                        <ListItemText primary="Help" />
                    </ListItem>
                </List>

                <List component="nav">
                    <ListItem button>
                        <ListItemText primary="Log Out" onClick={this.handleLogout} />
                    </ListItem>
                </List>
            </div>
        );

        if (isSignedIn) {
            return (
                <div className={classes.root}>
                    <AppBar
                        position="absolute"
                        style={{
                            boxShadow: "none",
                            backgroundColor: "transparent"
                        }}
                    >
                        <Toolbar>
                            <Typography
                                variant="title"
                                color="inherit"
                                className={classes.grow}
                                style={{ cursor: "pointer", color: "#cc285d" }}
                                onClick={() => this.handleClick("/home")}
                            >
                                MyExp
                            </Typography>
                            <IconButton
                                className={classes.menuButton}
                                color="inherit"
                                aria-label="Menu"
                            >
                                <MenuIcon onClick={this.toggleDrawer} />
                            </IconButton>
                            <SwipeableDrawer
                                anchor="right"
                                open={this.state.open}
                                onClose={this.toggleDrawer}
                                onOpen={this.toggleDrawer}
                                disableBackdropTransition={!iOS}
                                disableDiscovery={iOS}
                            >
                                <div
                                    tabIndex={0}
                                    role="button"
                                    onClick={this.toggleDrawer}
                                    onKeyDown={this.toggleDrawer}
                                >
                                    {sideList}
                                </div>
                            </SwipeableDrawer>
                        </Toolbar>
                    </AppBar>
                </div>
            );
        } else {
            return (
                <div className={classes.root}>
                    <AppBar position="static" className={classes.appBar}>
                        <Toolbar>
                            <Typography
                                variant="title"
                                color="inherit"
                                className={classes.grow}
                                style={{
                                    cursor: "pointer",
                                    color: "#fff",
                                    fontWeight: "bold",
                                    fontSize: "2rem"
                                }}
                                onClick={() => this.handleClick("/")}
                            >
                                MyExp
                            </Typography>
                            <Button
                                color="inherit"
                                onClick={() => this.handleClick("/login")}
                                className={classes.label}
                            >
                                Log in
                            </Button>
                            <Button
                                color="inherit"
                                onClick={() => this.handleClick("/register")}
                                className={classes.label}
                            >
                                Register
                            </Button>
                        </Toolbar>
                    </AppBar>
                </div>
            );
        }
    }
}

Navigation.propTypes = {
    classes: PropTypes.object.isRequired
};


export default withRouter(withStyles(styles)(Navigation));