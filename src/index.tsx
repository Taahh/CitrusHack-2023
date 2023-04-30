import React, { Fragment } from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { createBrowserRouter, RouterProvider, useNavigate } from "react-router-dom";
import Register from "./pages/Register";
import { FirebaseApp, initializeApp } from "firebase/app";
import { getAuth, Auth} from "firebase/auth"
import Login from "./pages/Login";
import Room from "./pages/Room";
import TitleComponent from "./components/TitleComponent";


const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

const firebaseConfig = {
    apiKey: process.env.REACT_APP_FIREBASE_AUTH_KEY,
    authDomain: "citrus-hack-6b50e.firebaseapp.com",
    databaseURL: "https://citrus-hack-6b50e-default-rtdb.firebaseio.com",
    projectId: "citrus-hack-6b50e",
    storageBucket: "citrus-hack-6b50e.appspot.com",
    messagingSenderId: "693410512723",
    appId: "1:693410512723:web:7350865f452b8a686eaba6"
};

const app: FirebaseApp = initializeApp(firebaseConfig)

const auth = getAuth(app)


const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>
    },
    {
        path: "/register",
        element: <Register/>
    },
    {
        path: "/login",
        element: <Login />
    },
    {
        path: "/rooms/:roomId",
        element: <Room />
    }
])


root.render(
    <Fragment>
        <RouterProvider router={router}/>
    </Fragment>
);

export function getAuthentication(): Auth {
    return auth;
}
