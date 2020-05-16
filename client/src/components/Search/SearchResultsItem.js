import React from "react";
import {Link} from "react-router-dom";


const SearchResultsItem = ({ user }) => {
    
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
                    to={`/users/${user.id}`}
                    style={{
                        textDecoration: "none",
                        color: "#fff",
                        textTransform: "capitalize",
                        fontSize: "1rem",
                        letterSpacing: 1
                    }}
                >
                    {user.username == null ? user.name  : user.username}
                </Link>
            </div>
        </div>
    );
};

export default SearchResultsItem;