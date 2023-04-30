import style from "./Login.module.css"
import React, { Fragment, useRef, useState } from "react";
import { getAuthentication } from "../index";
import { signInWithEmailAndPassword } from "firebase/auth"
import { useNavigate } from "react-router-dom";
import TitleComponent from "../components/TitleComponent";

const Login = () => {
    const auth = getAuthentication()
    const email = useRef<HTMLInputElement>(null)
    const password = useRef<HTMLInputElement>(null)
    const nav = useNavigate()

    auth.onAuthStateChanged(value => {
        if (value) {
            nav("/")
        }
    })

    function handleLogin(event: React.MouseEvent<HTMLButtonElement>) {
        if (email.current != undefined && (email.current?.value.length === 0 || !email.current?.validity.valid)) {
            alert("Email was invalid!")
            return;
        } if (password.current != undefined && password.current.value.length < 12) {
            alert("Password was less than 12 characters!")
            return;
        } else {
            // eslint-disable-next-line eqeqeq
            if (email.current == undefined || password.current == undefined) {
                alert("Something went wrong.. Please refresh")
                return;
            }
        }
        signInWithEmailAndPassword(auth, email.current?.value, password.current?.value).then(value => {
            const user = value.user
            console.log(user.uid)
        }).catch(reason => {
            alert(reason.message)
        })
    }

    return (
        <Fragment>
            <TitleComponent />
            <div className={style.loginForm}>
                <span>Email</span><br/><input type="email" name="email" id="email" className={style.textField} ref={email}/><br/>
                <span>Password</span><br/><input type="password" name="password" id="password" className={style.textField}
                                                 ref={password}/><br/>
                <button className={style.submitButton} onClick={handleLogin}>Login</button>
                <p onClick={() => nav("/register")}>Don't have an account? Click here</p>
            </div>
        </Fragment>
    )
}

export default Login;