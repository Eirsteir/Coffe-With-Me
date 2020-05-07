import React from "react";
import { Link } from "react-router-dom";

import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";

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
                    variant="display3"
                    style={{ color: "#fff", fontWeight: "bold" }}
                    id="header"
                >
                    MyExpences
                </Typography>
            </Grid>

            <Grid item>
                <Typography
                    variant="display1"
                    style={{ color: "#fff", fontWeight: 300 }}
                    id="sub-header"
                >
                    Start tracking your expences &mdash; <br /> with myExpences
                </Typography>
            </Grid>

            <Grid item>
                <Button
                    variant="raised"
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
                        Register now
                    </Link>
                </Button>
            </Grid>
        </Grid>
    );
};

export default LandingPage;