import React, { Component } from "react";
import { withRouter } from "react-router-dom";

import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";

import Navigation from "../../components/Navigation/Navigation";
import ErrorBoundary from "../../components/ErrorBoundary/ErrorBoundary";
import Main from "../Main/Main";


const theme = createMuiTheme({
  palette: {
    primary: {
      main: "#cc285d" // "#3CF2FD" very nice turqouise
    },
    secondary: {
      main: "#cc285d"
    }
  }
});

const initialState = {
  isSignedIn: false,
  user: {
    _id: "",
    name: "",
    email: "",
    age: "",
    joined: "",
    expences: [],
    tags: [],
    currency: ""
  },
  isLoading: false
};

class App extends Component {
  constructor() {
    super();
    this.state = initialState;
  }

  toggleSigninState = () => {
    this.setState(prevState => ({
      ...prevState,
      isSignedIn: !prevState.isSignedIn
    }));
  };

  componentDidMount() {
    const token = window.localStorage.getItem("token");
    if (token) {
      this.toggleLoading();
      fetch(`/signin`, {
        method: "post",
        headers: {
          "Content-Type": "application/json",
          Authorization: token // 'Bearer '
        }
      })
          .then(response => {
            if (response.status === 400) {
              return this.toggleLoading();
            }
            return response.json();
          })
          .then(data => {
            if (data && data.id) {
              fetch(`/profile/${data.id}`, {
                method: "get",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: token
                }
              })
                  .then(response => response.json())
                  .then(user => {
                    this.toggleLoading();
                    if (user && user.email) {
                      this.loadUser(user);
                      this.setState({ isSignedIn: true });
                      return this.props.history.push("/home");
                    }
                  })
                  .catch(err => {
                    this.toggleLoading();
                  });
            }
          })
          .catch(err => {
            this.toggleLoading();
          });
    }
  }

  toggleLoading = () => {
    this.setState((prevState, props) => ({
      isLoading: !prevState.isLoading
    }));
  };

  loadUser = user => {
    if (user._id) {
      this.setState({ user });
    }
  };

  render() {
    const { isSignedIn } = this.state;

    return (
        <div>
          <Navigation
              isSignedIn={isSignedIn}
              toggleSigninState={this.toggleSigninState}
          />
          <MuiThemeProvider theme={theme}>
            <ErrorBoundary>
              <Main
                  isSignedIn={isSignedIn}
                  user={this.state.user}
                  loadUser={this.loadUser}
                  toggleSigninState={this.toggleSigninState}
              />
            </ErrorBoundary>
          </MuiThemeProvider>
        </div>
    );
  }
}

export default withRouter(App);