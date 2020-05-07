import React from "react";
import { Redirect } from "react-router-dom";

import "./Dashboard.css";

class Dashboard extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const { loadUser, user } = this.props;

        if (!this.props.isAuthenticated) {
            return <Redirect to="/" />;
        }

        return (
            <div className="dashboard-container">
                <h2>Welcome {user.name}</h2>
            </div>
        );
    }
}

export default Dashboard;