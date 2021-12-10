import React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({
  appBar:{
    position: "fixed"
  }
})

const TopAppBar = () => {
  const classes = useStyles();

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar className={classes.appBar}>
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            KARYDE
          </Typography>
          <Button color="inherit">Login</Button>
          <Button color="inherit">Name</Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}

export default TopAppBar;