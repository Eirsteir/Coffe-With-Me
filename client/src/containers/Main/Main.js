
import React from "react";
import { Switch, Route } from "react-router-dom";
import Loadable from "react-loadable";

import CircularProgress from "@material-ui/core/CircularProgress";

const styles = {
    loading: {
        position: "absolute",
        top: 0,
        left: 0,
        bottom: 0,
        right: 0,
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: "99999",
        backgroundColor: "rgba(0,0,0, 0.5)"
    }
};

const Loading = () => (
    <div style={styles.loading}>
        <CircularProgress size={80} color="secondary" />
    </div>
);

const Home = Loadable({
    loader: () => import("../Dashboard/Dashboard"),
    loading: Loading
});

const LandingPage = Loadable({
    loader: () => import("../LandingPage/LandingPage"),
    loading: Loading
});

const Login = Loadable({
    loader: () => import("../../components/Login/Login"),
    loading: Loading
});

const Register = Loadable({
    loader: () => import("../../components/Register/Register"),
    loading: Loading
});

const MyProfile = Loadable({
    loader: () => import("../ProfilePage/MyProfilePage"),
    loading: Loading
});

const SearchPage = Loadable({
    loader: () => import("../SearchPage/SearchPage"),
    loading: Loading
});

const ProfilePage = Loadable({
    loader: () => import("../ProfilePage/ProfilePage"),
    loading: Loading
});

class Main extends React.Component {
    render() {
        const { user, loadUser, toggleAuthenticatedState, 
            isAuthenticated } = this.props;

        return (
            <Switch>
                <Route
                    exact
                    path="/"
                    render={props => (
                        <LandingPage
                            {...props}
                            loadUser={loadUser}
                            toggleAuthenticatedState={toggleAuthenticatedState}
                        />
                    )}
                />
                <Route
                    exact
                    path="/login"
                    render={props => (
                        <Login
                            {...props}
                            loadUser={loadUser}
                            toggleAuthenticatedState={toggleAuthenticatedState}
                        />
                    )}
                />
                <Route
                    exact
                    path="/register"
                    render={props => (
                        <Register
                            {...props}
                            loadUser={loadUser}
                            toggleAuthenticatedState={toggleAuthenticatedState}
                        />
                    )}
                />
                <Route
                    exact
                    path="/home"
                    render={props => (
                        <Home
                            {...props}
                            isAuthenticated={isAuthenticated}
                            user={user}
                            loadUser={loadUser}
                        />
                    )}
                />
                <Route
                    exact
                    path="/me"
                    render={props => (
                        <MyProfile
                            {...props}
                            isAuthenticated={isAuthenticated}
                            user={user}
                            loadUser={loadUser}
                        />
                    )}
                />
                <Route
                    path="/users/:id"
                    component={ProfilePage}
                />
                <Route
                    exact
                    path="/search"
                    component={SearchPage}
                />
            </Switch>
        );
    }
}

export default Main;