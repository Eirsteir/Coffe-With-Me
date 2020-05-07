import React from "react";
import { withRouter } from "react-router";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import CircularProgress from "@material-ui/core/CircularProgress";
import FormControl from "@material-ui/core/FormControl";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import InputAdornment from "@material-ui/core/InputAdornment";
import Visibility from "@material-ui/icons/Visibility";
import VisibilityOff from "@material-ui/icons/VisibilityOff";
import IconButton from "@material-ui/core/IconButton";


const styles = theme => ({
    login: {
        height: "80vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center"
    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
        width: 200,
        margin: "1em"
    },
    paper: {
        width: "17em",
        backgroundColor: "#fff"
    },
    form: {
        display: "flex",
        flexWrap: "wrap",
        justifyContent: "space-around",
        padding: "2em"
    },
    button: {
        marginTop: "2em"
    },
    control: {
        padding: ".5em"
    }
});

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            signInEmail: "",
            signInPassword: "",
            isLoading: false,
            showPassword: false,
            errorMessage: ""
        };
    }

    toggleLoading = () => {
        this.setState((prevState, props) => ({
            isLoading: !prevState.isLoading
        }));
    };

    handleClickShowPassword = () => {
        this.setState(state => ({ showPassword: !state.showPassword }));
    };

    onEmailChange = event => {
        this.setState({ signInEmail: event.target.value });
    };

    onPasswordChange = event => {
        this.setState({ signInPassword: event.target.value });
    };

    saveAuthTokenInSession = token => {
        window.localStorage.setItem("token", token);
    };

    handleSignin = () => {
        this.toggleLoading();
        fetch(`/signin`, {
            method: "post",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                email: this.state.signInEmail,
                password: this.state.signInPassword
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.userId && data.success === "true") {
                    this.saveAuthTokenInSession(data.token);
                    fetch(`/profile/${data.userId}`, {
                        method: "get",
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: data.token
                        }
                    })
                        .then(response => response.json())
                        .then(user => {
                            if (user && user.email) {
                                this.toggleLoading();
                                this.props.toggleSigninState();
                                this.props.loadUser(user);
                                this.props.history.push("/home");
                            }
                        })
                        .catch(err => {
                            this.toggleLoading();
                            console.warn("unable to log in");
                        });
                } else {
                    this.toggleLoading();
                    this.setState({ errorMessage: data });
                }
            })
            .catch(err => {
                this.toggleLoading();
                console.warn("unable to log in");
            });
    };

    onKeyDown = event => {
        if (event.key === "Enter") {
            event.preventDefault();
            this.handleSignin();
        }
    };

    onSubmit = event => {
        event.preventDefault();
        event.stopPropagation();
        this.handleSignin();
    };

    render() {
        const { classes } = this.props;
        return (
            <div className={classes.login}>
                <Paper className={classes.paper} elevation={16}>
                    <Typography
                        variant="headline"
                        component="h3"
                        className={classes.control}
                        style={{
                            display: "flex",
                            justifyContent: "center",
                            backgroundColor: "#cc285d",
                            color: "#fff",
                            fontWeight: 300
                        }}
                    >
                        Log in
                    </Typography>
                    <form
                        onKeyDown={this.onkeyDown}
                        onSubmit={this.onSubmit}
                        className={classes.form}
                    >
                        <TextField
                            id="input-email"
                            label="Email address"
                            autoComplete="email"
                            className={classes.textField}
                            margin="normal"
                            onChange={this.onEmailChange}
                        />
                        <FormControl className={classes.textField}>
                            <InputLabel htmlFor="adornment-password">Password</InputLabel>
                            <Input
                                id="adornment-password"
                                type={this.state.showPassword ? "text" : "password"}
                                value={this.state.signInPassword}
                                onChange={this.onPasswordChange}
                                autoComplete="current-password"
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="Toggle password visibility"
                                            onClick={this.handleClickShowPassword}
                                        >
                                            {this.state.showPassword ? (
                                                <VisibilityOff />
                                            ) : (
                                                <Visibility />
                                            )}
                                        </IconButton>
                                    </InputAdornment>
                                }
                            />
                        </FormControl>
                        {this.state.errorMessage ? (
                            <Typography
                                component="p"
                                style={{
                                    display: "flex",
                                    justifyContent: "center",
                                    color: "red"
                                }}
                            >
                                {this.state.errorMessage}
                            </Typography>
                        ) : true}
                        <Button
                            className={classes.button}
                            onClick={this.onSubmitSignIn}
                            variant="raised"
                            label="Submit"
                            type="submit"
                            color="secondary"
                        >
                            {this.state.isLoading ? (
                                <CircularProgress style={{ color: "#fff" }} size={20} />
                            ) : (
                                "Log in"
                            )}
                        </Button>
                    </form>
                </Paper>
            </div>
        );
    }
}

Login.propTypes = {
    classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(Login));