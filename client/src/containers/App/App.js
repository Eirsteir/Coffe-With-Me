import React, { Component } from "react";
import { withRouter } from "react-router-dom";

import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";

import Navigation from "../../components/Navigation/Navigation";
import ErrorBoundary from "../../components/ErrorBoundary/ErrorBoundary";
import Main from "../Main/Main";


const theme = createMuiTheme({
  palette: {
    primary: {
      main: "#616773" 
    },
    secondary: {
      main: "#A4A9B3"
    }
  }
});

const initialState = {
  isAuthenticated: false,
  user: {
    id: "",
    name: "",
    email: "",
    username: "",
  },
  isLoading: false,
};

class App extends Component {
  constructor() {
    super();
    this.state = initialState;
  }

  toggleAuthenticatedState = () => {
    this.setState(prevState => ({
      ...prevState,
      isAuthenticated: !prevState.isAuthenticated
    }));
  };

  componentDidMount() {
    const token = window.localStorage.getItem("auth");
    if (token) {
      this.toggleLoading();
      fetch(`/api/me`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: token
        }
      })
          .then(response => {
            if (response.status === 400) {
              return this.toggleLoading();
            }
            return response.json();
          })
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
  }

  toggleLoading = () => {
    this.setState((prevState, props) => ({
      isLoading: !prevState.isLoading
    }));
  };

  loadUser = user => {
    if (user.id) {
      this.setState({ user });
    }
  };

  render() {
    const { isAuthenticated } = this.state;

    return (
        <div>
          <Navigation
              isAuthenticated={isAuthenticated}
              toggleAuthenticatedState={this.toggleAuthenticatedState}
          />
          <MuiThemeProvider theme={theme}>
            <ErrorBoundary>
              <Main
                  isAuthenticated={isAuthenticated}
                  user={this.state.user}
                  loadUser={this.loadUser}
                  toggleAuthenticatedState={this.toggleAuthenticatedState}
              />
            </ErrorBoundary>
          </MuiThemeProvider>
        </div>
    );
  }
}

export default withRouter(App);