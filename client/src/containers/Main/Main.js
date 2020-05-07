
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

const Account = Loadable({
    loader: () => import("../Account/Account"),
    loading: Loading
});

class Main extends React.Component {
    render() {
        const { user, loadUser, toggleSigninState, isSignedIn } = this.props;
        return (
            <Switch>
                <Route
                    exact
                    path="/"
                    render={props => (
                        <LandingPage
                            {...props}
                            loadUser={loadUser}
                            toggleSigninState={toggleSigninState}
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
                            toggleSigninState={toggleSigninState}
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
                            toggleSigninState={toggleSigninState}
                        />
                    )}
                />
                <Route
                    exact
                    path="/home"
                    render={props => (
                        <Home
                            {...props}
                            isSignedIn={isSignedIn}
                            user={user}
                            loadUser={loadUser}
                        />
                    )}
                />
                <Route
                    exact
                    path="/account"
                    render={props => (
                        <Account
                            {...props}
                            isSignedIn={isSignedIn}
                            name={user.name}
                            email={user.email}
                            joined={user.joined}
                            currency={user.currency}
                        />
                    )}
                />
            </Switch>
        );
    }
}

export default Main;