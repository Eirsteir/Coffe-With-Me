import React from "react";

import FriendItem from "./FriendItem";

class FriendsList extends React.Component {

  render() {
    const { friends } = this.props;

    return (
      <div
        style={{
          backgroundColor: "#343b64",
          color: "#fff",
          height: "18rem",
          padding: "1rem",
          border: "none",
          borderRadius: 5
        }}
      >
        Friends

        { !friends.length
        ? <div>Add some friends!</div>
        : (
            <div style={{ marginTop: "1rem" }}>
              {friends.map((friend, i) => {
                return (
                  <FriendItem
                    key={i}
                    friend={friend}
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