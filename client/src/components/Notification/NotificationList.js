import React from 'react';

import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItemText';
import ListItemText from '@material-ui/core/ListItemText';

import NotificationItem from "./NotificationItem";


const styles ={
  root: {
    width: '100%',
    maxWidth: '36ch',
    minWidth: '36ch'
  },
  item: {
    display: 'flex',
    justifyContent: "center"
  },
};

class NotificationList extends React.Component {

  render() {
    const { classes, notifications } = this.props;

    return (
      <List className={classes.root}>
          { !notifications.length ? (
            <ListItem className={classes.item}>
              <ListItemText primary="No notifications available" />
            </ListItem>
          ) : (
            notifications.map((notification, i) => {
              return <NotificationItem key={i} notification={notification} />
            })
          )
        }
      </List>
    );
  }
}

export default withStyles(styles)(NotificationList);