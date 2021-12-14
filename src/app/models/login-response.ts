export interface loginResponse{
        user:User;
        token: string;
    }


    export class User {
        id: number;
        firstName: string;
        lastName: string;
        email: string;
        password: string;
        userRole: string;
        portfolios: Portfolio;
    }
    
    export class Portfolio{
        id: number;
        status: string;
        orders: any[];
    }