import React from 'react';
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
  },
}));

export default function NotificationItem({notification}) {
  const classes = useStyles();

  console.log(notification);
  
  return (
      <div>
        <ListItem alignItems="flex-start">
            <ListItemAvatar>
            <Avatar alt="Remy Sharp" src="/static/images/avatar/1.jpg" />
            </ListItemAvatar>
            <ListItemText
            primary={`${notification.message}`}
            secondary={
                <React.Fragment>
                    <div className={classes.item}>
                        <PersonAddIcon fontSize="small"/>
                        <p>{notification.createdDateTime}</p>
                    </div>
                </React.Fragment>
            }
            />
        </ListItem>
        <Divider variant="inset" component="li" />
    </div>
  );
}