import style from "./Register.module.css"
import React, { Fragment, useRef, useState } from "react";
import { getAuthentication } from "../index";
import { useNavigate } from "react-router-dom";
import TitleComponent from "../components/TitleComponent";

const Register = () => {
    const auth = getAuthentication()
    const email = useRef<HTMLInputElement>(null)
    const username = useRef<HTMLInputElement>(null)
    const password = useRef<HTMLInputElement>(null)
    const nav = useNavigate()
    const [ error, setError ] = useState<string>("")

    auth.onAuthStateChanged(value => {
        if (value) {
            nav("/")
        }
    })

    function handleRegister(event: React.MouseEvent<HTMLButtonElement>) {
        if (email.current != undefined && (email.current?.value.length === 0 || !email.current?.validity.valid)) {
            setError("Email was invalid!")
            return;
        } else if (username.current != undefined && username.current.value.length < 4) {
            setError("Username was less than 4 characters!")
            return;
        } else if (password.current != undefined && password.current.value.length < 12) {
            setError("Password was less than 12 characters!")
            return;
        } else {
            if (email.current == undefined || password.current == undefined || username.current == undefined) {
                setError("Something went wrong.. Please refresh")
                return;
            }
        }
        setError("")
        fetch(process.env.REACT_APP_BACKEND_URL as string + "api/users/create", {
            method: "PUT",
            headers: {
                "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email.current?.value,
                username: username.current?.value,
                password: password.current?.value
            })
        }).then(r => {
            if (r.status !== 200) {
                console.log("not ok")
                r.text().then(value => setError(value))
            }
        })
    }

    return (
        <Fragment>
            <TitleComponent />
            <div className={style.registerForm}>

                <span>Email</span><br/><input type="email" name="email" id="email" className={style.textField} ref={email}/><br/>
                <span>Username</span><br/><input type="text" name="username" id="username"
                                                 className={style.textField} ref={username}/><br/>
                <span>Password</span><br/><input type="password" name="password" id="password" className={style.textField}
                                                 ref={password}/><br/>
                <button className={style.submitButton} onClick={handleRegister}>Register</button>

                <p className={style.errorMsg}>{error}</p>
            </div>
        </Fragment>

    )
}

export default Register;