import React, { Fragment, useRef, useState } from 'react';
import { getAuthentication } from "./index";
import { useNavigate } from "react-router-dom";
import "./style/App.css"
import TitleComponent from "./components/TitleComponent";

function App() {

    const roomCode = useRef<HTMLInputElement>(null)
    const [ divState, setDivState ] = useState<JSX.Element>(<TitleComponent />)

    const auth = getAuthentication()
    const nav = useNavigate()

    function createRoom(event: React.MouseEvent<HTMLButtonElement>) {
        fetch(process.env.REACT_APP_BACKEND_URL as string + "api/rooms/create", {
            method: "PUT",
            headers: {
                "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                uid: auth.currentUser?.uid,
                username: auth.currentUser?.displayName
            })
        }).then(r => {
            if (r.status !== 200) {
                r.text().then(value => alert(value))
            } else {
                r.text().then(value => nav(`/rooms/${value}`))
            }
        })
    }

    function joinRoom(event: React.MouseEvent<HTMLButtonElement>) {
        fetch(process.env.REACT_APP_BACKEND_URL as string + "api/rooms/join/", {
            method: "POST",
            headers: {
                "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                joiningId: auth.currentUser?.uid,
                gameCode: roomCode.current?.value
            })
        }).then(r => {
            if (r.status !== 200) {
                r.text().then(value => alert(value))
            } else {
                r.text().then(value => nav(`/rooms/${value}`))
            }
        })
    }

    useState(() => {
        auth.onAuthStateChanged(value => {
            if (!value) {
                nav("/login")
            } else {
                fetch(process.env.REACT_APP_BACKEND_URL as string + "api/rooms/find/" + auth.currentUser?.uid, {
                    method: "GET",
                    headers: {
                        "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                        "Content-Type": "text/plain"
                    }
                }).then(r => {
                    if (r.status !== 200) {
                        setDivState(<div className="App">
                            <TitleComponent />
                            <button className="createRoom" onClick={createRoom}>Create Room</button>
                            <br/>
                            <br/>
                            <br/>
                            <span>Join Room</span><br/><input type="text" name="roomCode" id="roomCode"
                                                              placeholder="Room Code" maxLength={12} minLength={12}
                                                              ref={roomCode}/>
                            <button onClick={joinRoom}>Join</button>
                        </div>)
                    } else {
                        r.text().then(value1 => {
                            setDivState(<div className="App">
                                <TitleComponent />
                                <button className="joinRoom" onClick={() => nav(`/rooms/${value1}`)}>Join Room</button>
                                <br/>
                                <br/>
                                <br/>
                                <span>Join Other Room</span><br/><input type="text" name="roomCode" id="roomCode"
                                                                        placeholder="Room Code" maxLength={12} minLength={12}
                                                                        ref={roomCode}/>
                                <button onClick={joinRoom}>Join</button>
                            </div>)
                        })
                    }
                })
            }
        })
    })


    return divState

}

export default App;
