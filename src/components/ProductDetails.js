import { Fab, Grid, Typography } from '@mui/material';
import ProductStatTable from './ProductStatTable';
import Chart from './Chart'; 
// import NavigationIcon from '@mui/icons-material/Navigation';
import React from 'react';


const ProductDetails = () => {
    return (
        <Grid container direction="row" style={{ height: "93.1vh" }}>
            <Grid item sm={12} style={{ height: "13.1vh" }}>
                <Typography style={{fontSize: "30px", position: "relative", left: "40px", top: "20px"}}>
                    $122.89
                </Typography>
                <Typography style={{fontSize: "24px", position: "absolute", top: "100px", right: "50px"}}>
                    TSLA
                </Typography>
                <Typography style={{fontSize: "16px", position: "absolute", top: "130px", right: "20px"}}>
                    Tesla Motors, Inc
                </Typography>
            </Grid>
            <Grid item sm={12} style={{ height: "40vh" }}>
                <Chart />
            </Grid>
            <Grid item sm={12} style={{   height: "30vh" }} container direction="row">
                <Grid item sm={6}><ProductStatTable /></Grid>
                <Grid item sm={6}></Grid>
            </Grid>
            <Grid item sm={12} style={{ height: "10vh" }}>
                <Fab variant="extended" size="medium"
                    style={{
                        backgroundColor: "darkgreen",
                        color: "white", position: "absolute",
                        bottom: "16px", right: "56px"
                    }}>
                    BUY
                </Fab>
                <Fab variant="extended" size="medium"
                    style={{
                        backgroundColor: "darkgreen",
                        color: "white", position: "absolute",
                        bottom: "16px", right: "156px"
                    }}>
                    SELL
                </Fab>
            </Grid>
        </Grid>
    )

}

export default ProductDetails;