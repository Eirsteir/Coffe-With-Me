import React from 'react';

import {fade, withStyles} from "@material-ui/core/styles";
import InputBase from '@material-ui/core/InputBase';

import {handleResponse} from "../../services/user.service";
import SearchIcon from "@material-ui/icons/Search";
import createMuiTheme from "@material-ui/core/styles/createMuiTheme";
import PropTypes from "prop-types";
import {withRouter} from "react-router-dom";

const theme = createMuiTheme({
    spacing: 4
});
const styles = {
    search: {
        position: 'relative',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: fade(theme.palette.common.white, 0.15),
        '&:hover': {
            backgroundColor: fade(theme.palette.common.white, 0.25),
        },
        marginRight: theme.spacing(2),
        marginLeft: 0,
        width: '100%',
        [theme.breakpoints.up('sm')]: {
            marginLeft: theme.spacing(3),
            width: 'auto',
        },
    },
    searchIcon: {
        padding: theme.spacing(0, 2),
        height: '100%',
        position: 'absolute',
        pointerEvents: 'none',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    inputRoot: {
        color: 'inherit',
    },
    inputInput: {
        padding: theme.spacing(1, 1, 1, 0),
        // vertical padding + font size from searchIcon
        paddingLeft: `calc(1em + ${theme.spacing(4)}px)`,
        transition: theme.transitions.create('width'),
        width: '100%',
        [theme.breakpoints.up('md')]: {
            width: '20ch',
        },
    }
};

class SearchBox extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchValue: "",
            isLoading: false,
        };
    }

    toggleLoading = () => {
        this.setState((prevState, props) => ({
            isLoading: !prevState.isLoading
        }));
    };

    onKeyDown = event => {
        if (event.key === "Enter") {
            event.preventDefault();
            this.handleSearch();
        }
    };

    onSubmit = event => {
        event.preventDefault();
        event.stopPropagation();
        this.handleSearch();
    };

    onSearchChange = event => {
        this.setState({ searchValue: event.target.value });
    };

    handleSearch = () => {
        const token = window.localStorage.getItem("auth");
        this.toggleLoading();

        fetch(`api/users?search=name=='*${encodeURIComponent(this.state.searchValue)}*',
                    username=='*${encodeURIComponent(this.state.searchValue)}*'`, {
            method: "GET",
            headers: { "Content-Type": "application/json" ,
                Authorization: token
            }
        })
            .then(handleResponse)
            .then(results => {
                this.toggleLoading();
                this.props.history.push(
                    `/search?q=${this.state.searchValue}`,
                    {results: results,
                    isAuthenticated: this.props.isAuthenticated});
            })
            .catch(err => {
                this.toggleLoading();
                console.warn("Unable to perform search", err);
            });
    };


    render() {
        const { classes } = this.props;
            
        return (
            <div className={classes.search}>
                <div className={classes.searchIcon}>
                    <SearchIcon />
                </div>

                <InputBase
                    placeholder="Search…"
                    classes={{
                        root: classes.inputRoot,
                        input: classes.inputInput,
                    }}
                    inputProps={{ 'aria-label': 'search' }}
                    onKeyDown={this.onKeyDown}
                    onChange={this.onSearchChange}
                    onSubmit={this.onSubmit}
                />    
                
            </div>
        )
    }
}


SearchBox.propTypes = {
    classes: PropTypes.object.isRequired
};


export default withRouter(withStyles(styles)(SearchBox));