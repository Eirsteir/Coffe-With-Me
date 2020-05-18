import React from "react";
import { withRouter } from "react-router-dom";
import PropTypes from "prop-types";

import { withStyles } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import CircularProgress from "@material-ui/core/CircularProgress";
import FormControl from "@material-ui/core/FormControl";

import { handleResponse } from "../../services/user.service";

const styles = theme => ({
    register: {
        height: "80vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        [theme.breakpoints.down("md")]: {
            paddingTop: "3rem"
        }
    },
    textField: {
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1),
        width: 200,
        margin: "1em",
        '& .MuiInput-underline:before': {
            borderBottomColor: '#fff8', // Semi-transparent underline
        }
    },
    paper: {
        marginBottom: "3rem",
        width: "17em",
        backgroundColor: "transparent"
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
    },
    menu: {
        width: 200
    },
    checkbox: {
        fontSize: ".8rem"
    },
    input: {
        color: "#d3d3d3"
    }
});


class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            verifyEmail: "",
            password: "",
            verifyPassword: "",
            name: "",
            isLoading: false,
            errorMessage: ""
        };
    }

    toggleLoading = () => {
        this.setState((prevState, props) => ({
            isLoading: !prevState.isLoading
        }));
    };

    handleChange = name => event => {
        this.setState({ [name]: event.target.value });
    };

    handleRegister = () => {
        this.toggleLoading();
        fetch(`api/registration`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                email: this.state.email,
                verifyEmail: this.state.verifyEmail,
                password: this.state.password,
                verifyPassword: this.state.verifyPassword,
                name: this.state.name,
            })
        })
            .then(handleResponse)
            .then(user => {
                this.toggleLoading();
                if (user) {
                    this.props.loadUser(user);
                    this.props.toggleAuthenticatedState();
                    this.props.history.push("/home");
                }
            })
            .catch(this.handleValidationError);
    };

    handleValidationError = err => {
        this.setState({ errorMessage: err.replace(/,/g, "\n") })
    };

    onKeyDown = event => {
        if (event.key === "Enter") {
            event.preventDefault();
            this.handleRegister();
        }
    };

    onSubmit = event => {
        event.preventDefault();
        event.stopPropagation();
        this.handleRegister();
    };

    render() {
        const { classes } = this.props;
        return (
            <div className={classes.register}>
                <Paper className={classes.paper} elevation={0}>
                    <Typography
                        variant="h4"
                        component="h3"
                        className={classes.control}
                        style={{
                            display: "flex",
                            justifyContent: "center",
                            color: "#fff",
                            letterSpacing: 1
                        }}
                    >
                        Register
                    </Typography>
                    <form
                        onKeyDown={this.onKeyDown}
                        onSubmit={this.onSubmit}
                        className={classes.form}
                    >
                        <TextField
                            id="input-name"
                            label="Full name"
                            autoComplete="name"
                            className={classes.textField}
                            margin="normal"
                            onChange={this.handleChange("name")}
                            inputProps={{
                                style: { 
                                    color: "#d3d3d3"
                                }
                            }}
                            InputLabelProps={{
                                className: classes.input
                            }}
                        />
                        <TextField
                            id="input-email"
                            label="Email address"
                            autoComplete="email"
                            className={classes.textField}
                            margin="normal"
                            onChange={this.handleChange("email")}
                            inputProps={{
                                style: { 
                                    color: "#d3d3d3"
                                }
                            }}
                            InputLabelProps={{
                                className: classes.input
                            }}
                        />
                        <TextField
                            id="input-verify-email"
                            label="Verify email address"
                            autoComplete="email"
                            className={classes.textField}
                            margin="normal"
                            onChange={this.handleChange("verifyEmail")}
                            inputProps={{
                                style: { 
                                    color: "#d3d3d3"
                                }
                            }}
                            InputLabelProps={{
                                className: classes.input
                            }}
                        />
                        <FormControl className={classes.textField}>
                            <TextField
                                id="adornment-password"
                                label="Password"
                                type={this.state.showPassword ? "text" : "password"}
                                value={this.state.password}
                                onChange={this.handleChange("password")}
                                autoComplete="current-password"
                                helperText="Password must be at least 8 characters long"
                                inputProps={{
                                    style: { 
                                        color: "#d3d3d3"
                                    }
                                }}
                                InputLabelProps={{
                                    className: classes.input
                                }}
                            />
                            <TextField
                                id="adornment-verify-password"
                                label="Verify password"
                                type={this.state.showPassword ? "text" : "password"}
                                value={this.state.verifyPassword}
                                onChange={this.handleChange("verifyPassword")}
                                autoComplete="current-password"
                                inputProps={{
                                    style: { 
                                        color: "#d3d3d3"
                                    }
                                }}
                                InputLabelProps={{
                                    className: classes.input
                                }}
                            />
                        </FormControl>

                        {this.state.errorMessage && (
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
                        )}

                        <Button
                            onClick={this.onSubmit}
                            className={classes.button}
                            variant="text"
                            label="Submit"
                            type="submit"
                            color="secondary"
                        >
                            {this.state.isLoading ? (
                                <CircularProgress style={{ color: "#fff" }} size={20} />
                            ) : (
                                "Register"
                            )}
                        </Button>
                    </form>
                </Paper>
            </div>
        );
    }
}

Register.propTypes = {
    classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(Register));