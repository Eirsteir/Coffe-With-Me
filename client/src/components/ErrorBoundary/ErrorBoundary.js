import React, { Component } from "react";

class ErrorBoundary extends Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false };
    }

    componentDidCatch(error, info) {
        this.setState({ hasError: true });
    }

    render() {
        if (this.state.hasError) {
            return (
                <h1 style={{ marginTop: "5em", fontWeight: 300, color: "fff" }}>
                    We're sorry. Something went wrong.
                </h1>
            );
        }
        return this.props.children;
    }
}

export default ErrorBoundary;