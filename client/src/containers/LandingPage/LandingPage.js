import React from "react";
import { Link } from "react-router-dom";

import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import Toolbar from "@material-ui/core/Toolbar";

const LandingPage = ({ onRouteChange }) => {
    return (
        <Grid
            container
            justify="space-around"
            direction="column"
            spacing={8}
            wrap="wrap"
            id="main-container"
        >
            <Grid item>
                <Typography
                    variant="h2"
                    style={{ color: "#fff", fontWeight: "bold" }}
                    id="header"
                >
                    Coffee With Me
                </Typography>
            </Grid>

            <Grid item>
                <Typography
                    variant="subtitle1"
                    style={{ color: "#fff", fontWeight: 300 }}
                    id="sub-header"
                >
                    Grab a warm cup of coffee with your friends
                </Typography>
            </Grid>

            <Grid item>
                <Button
                    variant="outlined"
                    style={{ backgroundColor: "#dd1173", padding: "20px 40px" }}
                    id="register-now-btn"
                >
                    <Link
                        to="/register"
                        style={{
                            textDecoration: "none",
                            color: "#fff",
                            textTransform: "capitalize",
                            fontSize: "1rem",
                            letterSpacing: 1
                        }}
                    >
                        Register
                    </Link>
                    <Link
                        to="/login"
                        style={{
                            textDecoration: "none",
                            color: "#fff",
                            textTransform: "capitalize",
                            fontSize: "1rem",
                            letterSpacing: 1
                        }}
                    >
                        Login
                    </Link>
                </Button>
            </Grid>
        </Grid>
    );
};

export default LandingPage;