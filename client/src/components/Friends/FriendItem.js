import React from "react";

const FriendItem = ({ friend }) => {
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
            {console.log(friend)}
            <p>{friend.username ? friend.username : friend.name}</p>
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    height: "110%"
                }}
            >
                View profile
            </div>
        </div>
    );
};

export default FriendItem;