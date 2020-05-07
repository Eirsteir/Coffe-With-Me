import React, { Component } from "react";
import { withRouter } from "react-router-dom";

import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";

import Navigation from "../../components/Navigation/Navigation";
import ErrorBoundary from "../../components/ErrorBoundary/ErrorBoundary";
import Main from "../Main/Main";


const theme = createMuiTheme({
  palette: {
    primary: {
      main: "#3CF2FD" // #cc285d
    },
    secondary: {
      main: "#3CF2FD"
    }
  }
});

const initialState = {
  isAuthenticated: false,
  user: {
    _id: "",
    name: "",
    email: "",
    // createdAt: "",
  },
  isLoading: false
};

class App extends Component {
  constructor() {
    super();
    this.state = initialState;
  }

  toggleLoginState = () => {
    this.setState(prevState => ({
      ...prevState,
      isAuthenticated: !prevState.isAuthenticated
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
                      this.setState({ isAuthenticated: true });
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
    const { isAuthenticated } = this.state;

    return (
        <div>
          <Navigation
              isAuthenticated={isAuthenticated}
              toggleLoginState={this.toggleLoginState}
          />
          <MuiThemeProvider theme={theme}>
            <ErrorBoundary>
              <Main
                  isAuthenticated={isAuthenticated}
                  user={this.state.user}
                  loadUser={this.loadUser}
                  toggleLoginState={this.toggleLoginState}
              />
            </ErrorBoundary>
          </MuiThemeProvider>
        </div>
    );
  }
}

export default withRouter(App);