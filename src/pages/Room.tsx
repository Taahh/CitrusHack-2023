import { useNavigate, useParams } from "react-router-dom";
import TitleComponent from "../components/TitleComponent";
import React, { Fragment, useState } from "react";
import { getAuthentication } from "../index";

import "../style/Room.css"
import { Editor } from "@monaco-editor/react";

const Room = () => {
    const auth = getAuthentication()
    const { roomId } = useParams()
    const nav = useNavigate()
    const [ users, setUsers ] = useState<string[]>([])

    auth.onAuthStateChanged(value => {
        if (!value) {
            nav("/login")
        }
    })

    useState(() => {
        fetch(process.env.REACT_APP_BACKEND_URL as string + `api/rooms/exists/${roomId}`, {
            method: "GET",
            headers: {
                "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                "Content-Type": "text/plain"
            }
        }).then(r => {
            if (r.status === 404) {
                nav("/")
            } else {
                fetch(process.env.REACT_APP_BACKEND_URL as string + "api/rooms/" + roomId, {
                    method: "GET",
                    headers: {
                        "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                        "Accept": "application/json"
                    }
                }).then(r => {
                    if (r.status !== 200) {
                        alert(r.status)
                    } else {
                        r.json().then(value => {
                            setUsers(prevState => prevState.concat(value["owner"]))
                            setUsers(prevState => prevState.concat(value["members"]))
                            setUsers(prevState => prevState.concat("aa", "b", "c"))
                        })
                    }
                })
            }
        })
    })

    return (
        <Fragment>
            <TitleComponent/>
            <div className={"room"}>
                <Editor height="50vh" width="40vw" defaultLanguage="python" defaultValue="#yooo" theme={"vs-dark"} />
                <p>Members</p>
                <div className="members">
                    <ul>
                        {users.map(value => <li>{value}</li>)}
                    </ul>
                </div>
            </div>
        </Fragment>

    )

}

export default Room;