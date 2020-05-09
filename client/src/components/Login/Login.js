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
import {createBasicAuthToken, saveAuthTokenInLocal} from "../../helpers/auth-headers";


const styles = theme => ({
    login: {
        height: "80vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center"
    },
    textField: {
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1),
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
            loginEmail: "",
            loginPassword: "",
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
        this.setState({ loginEmail: event.target.value });
    };

    onPasswordChange = event => {
        this.setState({ loginPassword: event.target.value });
    };

    handleLogin = () => {
        this.toggleLoading();
        fetch(`api/user`, {
            method: "GET",
            headers: { "Content-Type": "application/json" ,
                        Authorization: createBasicAuthToken(
                            this.state.loginEmail,
                            this.state.loginPassword
                        )
            }
        })
            .then(response => response.json())
            .then(user => {
                if (user.id && user.email) {
                    saveAuthTokenInLocal(createBasicAuthToken(
                        this.state.loginEmail,
                        this.state.loginPassword
                    ));
                    this.toggleLoading();
                    this.props.toggleAuthenticatedState();
                    this.props.loadUser(user);
                    this.props.history.push("/home");
                } else {
                    this.toggleLoading();
                    this.setState({ errorMessage: user });
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
            this.handleLogin();
        }
    };

    onSubmit = event => {
        event.preventDefault();
        event.stopPropagation();
        this.handleLogin();
    };

    render() {
        const { classes } = this.props;
        return (
            <div className={classes.login}>
                <Paper className={classes.paper} elevation={16}>
                    <Typography
                        variant="h4"
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
                        onKeyDown={this.onKeyDown}
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
                                value={this.state.loginPassword}
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
                            onClick={this.onSubmit}
                            variant="text"
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