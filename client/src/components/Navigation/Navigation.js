import React from "react";
import { withRouter } from "react-router-dom";
import PropTypes from "prop-types";
import SockJsClient from 'react-stomp';

import { withStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import MenuIcon from "@material-ui/icons/Menu";
import SwipeableDrawer from "@material-ui/core/SwipeableDrawer";
import List from "@material-ui/core/List";
import Divider from "@material-ui/core/Divider";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import HomeOutlinedIcon from "@material-ui/icons/Home";
import ExitToAppOutlinedIcon from '@material-ui/icons/ExitToAppOutlined';
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import Badge from "@material-ui/core/Badge";
import NotificationsIcon from "@material-ui/icons/Notifications";
import AccountCircle from "@material-ui/icons/AccountCircle";
import MoreIcon from '@material-ui/icons/MoreVert';
import createMuiTheme from "@material-ui/core/styles/createMuiTheme";

import SearchBox from "../../components/Search/SearchBox";
import NotificationService from "../../services/NotificationService";
import AuthService from "../../services/AuthService";
import NotificationList from "../Notification/NotificationList";

const theme = createMuiTheme({
    spacing: 4
});

const styles = {
    grow: {
        flexGrow: 1,
    },
    appBar: {
        boxShadow: "none",
        backgroundColor: "transparent",
        padding: "1rem 6rem",
        [theme.breakpoints.down("sm")]: {
            padding: "0"
        }
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        display: 'none',
        [theme.breakpoints.up('sm')]: {
            display: 'block',
        },
        cursor: "pointer"
    },
    sectionDesktop: {
        display: 'none',
        [theme.breakpoints.up('md')]: {
            display: 'flex',
        },
    },
    sectionMobile: {
        display: 'flex',
        [theme.breakpoints.up('md')]: {
            display: 'none',
        },
    },
    list: {
        width: 200
    }
};


class Navigation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            notifications: [],
            searchValue: "",
            user: {},
            isAuthenticated: this.props.isAuthenticated,
            isLoading: false,
            open: false,
            anchorEl: null,
            mobileMoreAnchorEl: null,
            clientConnected: false
        };
    }

    componentDidMount() {
        // todo: load count and fetch requests on notification list open
        NotificationService.getNotifications()
            .then(notifications => {                               
                this.setState({ notifications });
            })
            .catch(console.log);    
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
        AuthService.logout(this.props.history.push, "/")
    };

    onMessageRecieve = (message, topic) => {
        console.log(message);
        
        this.setState(prevState => ({
            notifications: [...prevState.notifications, message]
        }));
    }

    render() {
        const { classes, user, isAuthenticated, searchResults, loadSearchResults } = this.props;
        
        const isMenuOpen = Boolean(this.state.anchorEl);
        const isMobileMenuOpen = Boolean(this.state.mobileMoreAnchorEl);

        const handleMenuOpen = (event) => {
            this.setState({ anchorEl: event.currentTarget});
        };

        const handleMobileMenuClose = () => {
            this.setState({ mobileMoreAnchorEl: null});
        };

        const handleMenuClose = () => {
            this.setState({ anchorEl: null});
            handleMobileMenuClose();
        };

        const handleMobileMenuOpen = (event) => {
            this.setState({ mobileMoreAnchorEl: event.currentTarget});
        };

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
                    <ListItem button onClick={this.handleLogout}>
                        <ListItemIcon>
                            <ExitToAppOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary="Log Out" />
                    </ListItem>
                </List>
            </div>
        );


        const profileMenuId = 'primary-search-account-menu';
        const renderProfileMenu = (
            <Menu
                anchorEl={this.state.anchorEl}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                id={profileMenuId}
                keepMounted
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                open={isMenuOpen}
                onClose={handleMenuClose}
            >
                <MenuItem onClick={() => this.handleClick(`/users/${user.name}`)}>Profile</MenuItem>
                <MenuItem onClick={() => this.handleClick("/account")}>My account</MenuItem>
            </Menu>
        );

        const notificationsMenuId = 'primary-search-notifications-menu';
        const renderNotificationsMenu = (
            <Menu
                anchorEl={this.state.anchorEl}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                id={notificationsMenuId}
                keepMounted
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                open={isMenuOpen}
                onClose={handleMenuClose}
            >
                <NotificationList notifications={this.state.notifications} />
                <MenuItem 
                    onClick={() => this.handleClick("/notifications")}
                    style={{
                        display: 'flex',
                        justifyContent: 'center'
                    }}
                >View more</MenuItem>
            </Menu>
        );

        const mobileMenuId = 'primary-search-account-menu-mobile';
        const renderMobileMenu = (
            <Menu
                anchorEl={this.state.mobileMoreAnchorEl}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                id={mobileMenuId}
                keepMounted
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                open={isMobileMenuOpen}
                onClose={handleMobileMenuClose}
            >
                <MenuItem onClick={handleMenuOpen}>
                    <IconButton aria-label={`show ${this.state.notifications.length} new notifications`} color="inherit">
                        <Badge badgeContent={this.state.notifications.length} color="secondary">
                            <NotificationsIcon />
                        </Badge>
                    </IconButton>
                    <p>Notifications</p>
                </MenuItem>
                <MenuItem onClick={() => this.handleClick("/me")}>
                    <IconButton
                        aria-label="profile of current user"
                        aria-controls="primary-search-account-menu"
                        aria-haspopup="true"
                        color="inherit"
                    >
                        <AccountCircle />
                    </IconButton>
                    <p>Profile</p>
                </MenuItem>
            </Menu>
        );

        if (isAuthenticated) {
            return (
                <div className={classes.grow}>
                    <AppBar
                        position="static"
                        style={{
                        boxShadow: "none",
                        backgroundColor: "transparent"
                        }}
                    >
                        <Toolbar>
                            <IconButton
                                edge="start"
                                className={classes.menuButton}
                                color="inherit"
                                aria-label="open drawer"
                                onClick={this.toggleDrawer}
                            >
                                <MenuIcon />
                            </IconButton>
                            <SwipeableDrawer
                                anchor="left"
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
                            
                            {/*<Typography*/}
                            {/*    className={classes.title}*/}
                            {/*    variant="h6"*/}
                            {/*    noWrap*/}
                            {/*    onClick={() => this.handleClick("/home")}*/}
                            {/*>*/}
                            {/*    Coffee With Me*/}
                            {/*</Typography>*/}

                            <SearchBox
                                userId={user.id}
                                searchResults={searchResults}
                                loadSearchResults={loadSearchResults}
                                isAuthenticated={isAuthenticated}
                            />

                            <div className={classes.grow} />
                            <div className={classes.sectionDesktop}>
                                <IconButton
                                    aria-label={`show ${this.state.notifications.length} new notifications`}
                                    color="inherit"
                                    aria-controls={notificationsMenuId}
                                    aria-haspopup="true"
                                    onClick={handleMenuOpen}
                                >
                                    <Badge badgeContent={this.state.notifications.length} color="secondary">
                                        <NotificationsIcon />
                                    </Badge>
                                </IconButton>
                                <IconButton
                                    edge="end"
                                    aria-label="account of current user"
                                    aria-controls={profileMenuId}
                                    aria-haspopup="true"
                                    onClick={() => this.handleClick("/me")}
                                    color="inherit"
                                >
                                    <AccountCircle />
                                </IconButton>
                            </div>
                            <div className={classes.sectionMobile}>
                                <IconButton
                                    aria-label="show more"
                                    aria-controls={mobileMenuId}
                                    aria-haspopup="true"
                                    onClick={handleMobileMenuOpen}
                                    color="inherit"
                                >
                                    <MoreIcon />
                                </IconButton>
                            </div>
                        </Toolbar>
                    </AppBar>
                    {renderMobileMenu}
                    {renderProfileMenu}
                    {renderNotificationsMenu}

                    <SockJsClient
                        url={`http://localhost:8080/api/ws`} 
                        topics={[`/user/${user.id}/queue/notifications`]}
                        onMessage={this.onMessageRecieve} 
                        onConnect={ () => { this.setState({ clientConnected: true }) } }
                        onDisconnect={ () => { this.setState({ clientConnected: false }) } }
                        debug={true} />

                </div>
            );
        } else {
            return (
                <div className={classes.root}>
                    <AppBar position="static" className={classes.appBar}>
                        <Toolbar>
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