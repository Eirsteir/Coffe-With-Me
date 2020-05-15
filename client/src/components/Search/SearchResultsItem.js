import React from "react";
import {Link} from "react-router-dom";


const PopularTagsItem = ({ id, username, name }) => {
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
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    height: "110%"
                }}
            >
                <Link 
                    to="/profile"
                    style={{
                        textDecoration: "none",
                        color: "#fff",
                        textTransform: "capitalize",
                        fontSize: "1rem",
                        letterSpacing: 1
                    }}
                >
                    {username == null ? name  : username}
                </Link>
            </div>
        </div>
    );
};

export default PopularTagsItem;