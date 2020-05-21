import React from 'react';
import moment from 'moment';

import { makeStyles } from '@material-ui/core/styles';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import PersonAddIcon from '@material-ui/icons/PersonAdd';

const useStyles = makeStyles((theme) => ({
  item: {
    display: 'flex',
    alignItems: 'center',
    justifyItems: "space-between"
  }
}));

export default function NotificationItem({notification}) {
  const classes = useStyles();
  const backgroundColor = notification.isRead ? "none" : "#d2d2d2";
  const timeSince = moment(notification.createdDateTime).from(new Date());

  console.log(notification);

  return (
      <div>
        <ListItem alignItems="flex-start" style={{ backgroundColor: backgroundColor }}> 
            <ListItemAvatar>
            <Avatar alt="Remy Sharp" src="/static/images/avatar/1.jpg" />
            </ListItemAvatar>
            <ListItemText
            primary={`${notification.message}`}
            secondary={
                <React.Fragment>
                    <div className={classes.item}>
                        <PersonAddIcon fontSize="small"/>
                        <p>{timeSince}</p>
                    </div>
                </React.Fragment>
            }
            />
        </ListItem>
        <Divider variant="inset" component="li" />
    </div>
  );
}