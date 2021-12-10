import { Divider, Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';
import React from 'react';
import LeftNavBar from './LeftNavBar';
import ProductDetails from './ProductDetails';
import ProductsDisplay from './ProductsDisplay';
import TopAppBar from './TopAppBar';

const useStyles = makeStyles({
    itemContainer:{
        paddingTop: "64px",
    }
})
const DashBoard = () => {
    const classes = useStyles();

    return (
        <div>
            <TopAppBar />
            <Grid container>
                <Grid item sm={2}  className={classes.itemContainer}>
                    <LeftNavBar />
                </Grid>
                <Grid item sm={3} className={classes.itemContainer}>
                    <ProductsDisplay />
                </Grid>
                <Grid item sm={7} className={classes.itemContainer}>
                    <ProductDetails />
                </Grid>
            </Grid>
        </div>
    )

}

export default DashBoard;