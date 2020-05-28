import React from "react";
import {Link} from "react-router-dom";


const SearchResultsItem = ({ userId, user, isAuthenticated }) => {

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
                    to={{
                        pathname: `/users/${user.id}`,
                        state: {
                            userId: userId,
                            friendId: user.id,
                            isAuthenticated: isAuthenticated,
                        }  
                    }}
                    style={{
                        textDecoration: "none",
                        color: "#fff",
                        textTransform: "capitalize",
                        fontSize: "1rem",
                        letterSpacing: 1
                    }}
                >
                    {user.nickname == null ? user.name  : user.nickname}
                </Link>
            </div>
        </div>
    );
};

export default SearchResultsItem;