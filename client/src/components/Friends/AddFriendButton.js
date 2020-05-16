import React from "react";

import {withStyles} from "@material-ui/core/styles";
import Button from '@material-ui/core/Button';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import createMuiTheme from "@material-ui/core/styles/createMuiTheme";


const AddFriendButton = (props) => {
        
    return (
        <div
            style={{
                display: "flex",
                justifyContent: "space-between",
                height: "2.5rem",
                color: "#c3cdd0",
                fontSize: ".9rem",
                marginTop: ".9rem"
            }}
        >
            <Button 
                variant="outlined" 
                color="secondary"
                size="small"
                disableElevation
                endIcon={<PersonAddIcon />}
                onClick={props.onClick}
                >
                Add friend
            </Button>
        </div>
    );
};

export default AddFriendButton;