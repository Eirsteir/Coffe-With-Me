import React from "react";
import { Link } from "react-router-dom";

import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";

import "./LandingPage.css";

const LandingPage = ({ onRouteChange }) => {
    return (
        <div id="landing-page-container">
            <Grid
                container
                spacing={1}
                direction="column"
                alignItems="center"
                justify="center"
                style={{ minHeight: '70vh' }}
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

                <Grid container
                      spacing={2}
                      direction="row"
                      alignItems="center"
                      justify="center"
                      style={{ minHeight: '100%',marginTop: "1rem" }}
                >
                    <Grid item>
                        <Button
                            variant="text"
                            style={{ backgroundColor: "#dd1173", padding: "0.5rem 0", width: "8rem" }}
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
                        </Button>
                    </Grid>
                    <Grid item>
                        <Button
                            variant="text"
                            style={{ backgroundColor: "#dd1173", padding: "0.5rem 0", width: "8rem"  }}
                        >
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
            </Grid>
        </div>
    );
};

export default LandingPage;