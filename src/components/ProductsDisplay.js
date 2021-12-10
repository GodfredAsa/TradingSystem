import { Divider, List, ListItem, ListItemText, Card, Button } from '@mui/material';
import { grey } from '@mui/material/colors';
import React from 'react';
import MarketProducts from './MarketProducts';

const ProductsDisplay = () => {
    return (
        <Card variant="outlined">
            <List style={{ height: "91.5vh", backgroundColor: grey[200] }}>

                {
                    MarketProducts.map(products => {
                        return (
                            <>
                                <ListItem key={products.id} style={{ height: "80px" }} button>
                                    <ListItemText>
                                        {products.ticker}
                                    </ListItemText>
                                    {
                                        products.color == "red" ? <Button variant="contained" color="error">{products.price}</Button> :
                                            <Button variant="contained" color="success">{products.price}</Button>
                                    }
                                </ListItem>
                                <Divider />
                            </>
                        )
                    })

                }

            </List>
        </Card>
    )

}

export default ProductsDisplay;