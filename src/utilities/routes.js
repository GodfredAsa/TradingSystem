import DashBoard from "../components/Dashboard";
import React from 'react';

const Dashboard = React.lazy(()=> import ("../components/Dashboard"));

export const ComponentRoutes = [
    {
        element: DashBoard,
        path: "/dashboard",
        exact: true
    }
]