import React from "react";

import {withStyles} from "@material-ui/core/styles";
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import createMuiTheme from "@material-ui/core/styles/createMuiTheme";
import FormHelperText from '@material-ui/core/FormHelperText';


const theme = createMuiTheme({
    spacing: 4
});
const styles = {
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
        color: "#fff"
      },
    
    select: {
        marginTop: theme.spacing(2),
        '&:before': {
            borderColor: "#A4A9B3",
        },
        '&:after': {
            borderColor: "#A4A9B3",
        },
        color: "#A4A9B3"
    },
    icon: {
        fill: "#A4A9B3",
    }
};

class FriendSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: "",
            friendId: "" 
        };
    }

    componentDidMount() {
        const {id, friendId} = this.props;
        this.setState({ id: id, friendId: friendId });
    }

    handleRemoveFriend = () => {
        return;
    }

    handleChange = () => {
        return;
    }

    render() {
        const { classes } = this.props;

        return (
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    height: "2.5rem",
                    color: "#c3cdd0",
                    fontSize: ".9rem"
                }}
            >    
                <FormControl className={classes.formControl}>
                    <Select
                        value="Friends"
                        onChange={this.handleChange}
                        displayEmpty
                        autoWidth
                        className={classes.select}
                        inputProps={{
                            'aria-label': 'Friendship operations',
                            classes: {
                                icon: classes.icon,
                            },
                        }}
                    >
                        <MenuItem value="Friends" disabled>
                            Friends
                        </MenuItem>
                        <MenuItem value={10}>Remove friend</MenuItem>
                        <MenuItem value={20}>View friendship</MenuItem>
                    </Select>
                </FormControl>
            </div>
        );
    }
   
};

export default withStyles(styles)(FriendSelect);