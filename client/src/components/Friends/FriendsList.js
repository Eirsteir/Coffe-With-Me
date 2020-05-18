import React from "react";

import FriendItem from "./FriendItem";

class FriendsList extends React.Component {

  render() {
    const { friends } = this.props;

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
        ? <p>Add some friends!</p>
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