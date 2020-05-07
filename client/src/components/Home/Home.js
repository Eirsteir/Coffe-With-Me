import React from 'react';
import { Link } from 'react-router-dom';

import { userService } from '../../services/user.service';

class Home extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            user: {},
            users: []
        };
    }

    componentDidMount() {
        this.setState({
            user: JSON.parse(localStorage.getItem('auth')),
            users: { loading: true }
        });
        userService.getAll().then(users => this.setState({ users }));
    }

    render() {
        const { user, users } = this.state;
        return (
            <div className="col-md-6 col-md-offset-3">
                <h1>Hi {user.name}!</h1>
                <p>You're logged in with React & Basic HTTP Authentication!!</p>
                <h3>Users from secure api end point:</h3>
                {users.loading && <em>Loading users...</em>}
                {users.length &&
                <ul>
                    {users.map((user, index) =>
                        <li key={user.id}>
                            {user.name}
                        </li>
                    )}
                </ul>
                }
                <p>
                    <Link to="/">Logout</Link>
                </p>
            </div>
        );
    }
}

export { Home };