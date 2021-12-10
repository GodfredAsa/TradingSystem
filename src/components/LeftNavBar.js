import { Card, List, ListItem, ListItemIcon, ListItemText} from '@mui/material';
import AddBusinessIcon from '@mui/icons-material/AddBusiness';
import SettingsIcon from '@mui/icons-material/Settings';
import PendingIcon from '@mui/icons-material/Pending';
import HistoryIcon from '@mui/icons-material/History';
import { makeStyles } from '@mui/styles';
import { grey } from '@mui/material/colors';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import React from 'react';

const useStyles = makeStyles({
   leftBarContainer:{
       height: "91.5vh",
       position: "static",
       backgroundColor: grey[100]
   },
  });

const LeftNavBar = () =>{
    const classes = useStyles();

    return(
        <Card  variant="outlined">
        <List className={classes.leftBarContainer}>
          <ListItem button>
            <ListItemIcon>
               <AddBusinessIcon />
            </ListItemIcon>
            <ListItemText> Market Stocks</ListItemText>
          </ListItem>

          <ListItem button>
            <ListItemIcon>
               <InboxIcon />
            </ListItemIcon>
            <ListItemText> My Portfolios</ListItemText>
          </ListItem>

          <ListItem button>
            <ListItemIcon>
               <PendingIcon />
            </ListItemIcon>
            <ListItemText> Pending Orders</ListItemText>
          </ListItem>

          <ListItem button>
            <ListItemIcon>
               <HistoryIcon />
            </ListItemIcon>
            <ListItemText> Order History</ListItemText>
          </ListItem>

          <ListItem button>
            <ListItemIcon>
               <SettingsIcon />
            </ListItemIcon>
            <ListItemText> Settings</ListItemText>
          </ListItem>
      </List>
      </Card>
    )

}

export default LeftNavBar; 