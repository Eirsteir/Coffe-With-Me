import React from 'react';

import CircularProgress from "@material-ui/core/CircularProgress";

import TabPanel from './TabPanel';
import FriendRequestsList from "./FriendRequestsList";
import { toggleLoading } from "../../helpers/loading";
import { getFriendRequests } from "../../services/friendship.service";

class FriendRequestsTabPanel extends React.Component {
    _isMounted = false;

    constructor(props) {
        super(props);
        this.state = {
            userId: "",
            friends: [],
            isLoading: false,
            errorMessage: "",
        };
    }

    componentDidMount() {
        this._isMounted = true;
        this.handleFetchFriendRequests();
    }

    handleFetchFriendRequests = ()  => {
        toggleLoading(this);
        getFriendRequests()
            .then(resp => {
                toggleLoading(this);
                if (resp.length && this._isMounted) {
                    return this.setState({ friendRequests: resp})
                }
            })
            .catch(err => {
                toggleLoading(this);
                this.setState({ errorMessage: err.message })
            });
    };
            
    componentWillUnmount() {
        this._isMounted = false;
      }

    render() {
        const { index, value, userId, isAuthenticated } = this.props;
        
        return (
            this.state.isLoading  
             ? <CircularProgress style={{ color: "secondary" }} size={20} />
             : <TabPanel 
                    index={index} 
                    value={value}
                    children={<FriendRequestsList 
                        friends={this.state.friends} 
                        userId={userId} 
                        isAuthenticated={isAuthenticated} />}
                />
        )
    }               
}

export default FriendRequestsTabPanel;