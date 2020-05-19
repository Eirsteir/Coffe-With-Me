import React from 'react';

import { makeStyles, withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Typography from '@material-ui/core/Typography';
import Tab from '@material-ui/core/Tab';

import TabPanel from "./TabPanel";
import FriendsTabPanel from "./FriendsTabPanel";
import FriendRequestsTabPanel from "./FriendRequestsTabPanel";

const StyledTabs = withStyles({
  indicator: {
    display: 'flex',
    justifyContent: 'center',
    border: "none",
    borderRadius: 5,
    backgroundColor: 'transparent',
    '& > div': {
      maxWidth: 40,
      width: '100%',
      backgroundColor: '#A4A9B3',
    },
  },
})((props) => <Tabs {...props} TabIndicatorProps={{ children: <div /> }} />);

const StyledTab = withStyles((theme) => ({
  root: {
    textTransform: 'none',
    fontWeight: theme.typography.fontWeightRegular,
    fontSize: theme.typography.pxToRem(15),
    marginRight: theme.spacing(1),
    '&:focus': {
      opacity: 1,
    },
  },
}))((props) => <Tab disableRipple {...props} />);

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  padding: {
    padding: theme.spacing(3),
  },
  demo1: {
    backgroundColor: theme.palette.background.paper,
  },
  demo2: {
    backgroundColor: '#DCDCDC',
    border: "none",
    borderRadius: 5
  },
}));


export default function MyFriendshipsTabs(props) {
  const classes = useStyles();
  const [value, setValue] = React.useState(0);
  const { userId, isAuthenticated } = props;

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <div className={classes.root}>
      <div className={classes.demo2}>
        <StyledTabs value={value} onChange={handleChange} aria-label="Users friendships tabs">
          <StyledTab label="Friends" />
          <StyledTab label="Friend Requests" />
          <StyledTab label="Recently Added" />
        </StyledTabs>
        <FriendsTabPanel value={value}  index={0} userId={userId} isAuthenticated={isAuthenticated} />
        <FriendRequestsTabPanel value={value} index={1} userId={userId} isAuthenticated={isAuthenticated} />
        <TabPanel value={value} index={2}>
          Recently added
        </TabPanel>
        <Typography className={classes.padding} />
      </div>
    </div>
  );
}