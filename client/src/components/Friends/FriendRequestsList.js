import React from "react";

import FriendRequestItem from "./FriendRequestItem";

class FriendsList extends React.Component {

  render() {
    const { friends, isAuthenticated } = this.props;

    return (
      <div
        style={{
          height: "18rem",
          padding: "1rem",
          border: "none",
          borderRadius: 5
        }}
      >
        { !friends.length
        ? <div>Add some friends!</div>
        : (
            <div style={{ marginTop: "1rem" }}>
              {friends.map((friend, i) => {
                return (
                  <FriendRequestItem
                    key={i}
                    friend={friend}
                    isAuthenticated={isAuthenticated}
                  />
                );
              })}
            </div>
        )}
      </div>
    );
  }
}

export default FriendsList;