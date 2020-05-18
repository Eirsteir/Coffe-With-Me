import React from "react";
import {Link} from "react-router-dom";

const FriendItem = ({ userId, friend }) => {
    return (
        <div
            style={{
                display: "flex",
                justifyContent: "space-between",
                                    alignItems: "center",
                height: "2.5rem",
                fontSize: ".9rem"
            }}
        >

            {friend.username == null ? friend.name  : friend.username}

            <div>
                <Link 
                    to={{
                        pathname: `/users/${friend.id}`,
                        state: {
                            userId: userId,
                            friendId: friend.id
                        }  
                    }}
                    style={{
                        textDecoration: "none",
                        fontSize: ".9rem",
                        letterSpacing: 1
                    }}
                >
                    View profile
                </Link>
            </div>
        </div>
    );
};

export default FriendItem;