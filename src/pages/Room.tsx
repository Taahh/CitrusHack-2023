import { useNavigate, useParams } from "react-router-dom";
import TitleComponent from "../components/TitleComponent";
import React, { Fragment, useState } from "react";
import { getAuthentication } from "../index";

import "../style/Room.css"
import { Editor } from "@monaco-editor/react";
import { editor } from "monaco-editor";

interface Problem {
    problemName: string
    templates: Map<string, string>
}

const Room = () => {
    const auth = getAuthentication()
    const { roomId } = useParams()
    const nav = useNavigate()
    const [ users, setUsers ] = useState<string[]>([])
    const [ language, setLanguage ] = useState<string>("PYTHON")
    const [ problemId, setProblemId ] = useState<number>(1)
    const [ code, setCode ] = useState<string>("")
    const [ writtenCode, setWrittenCode ] = useState<string>("")
    const [ hasWrittenCode, setHasWrittenCode ] = useState(false)

    auth.onAuthStateChanged(value => {
        if (!value) {
            nav("/login")
        } else {
            fetch(process.env.REACT_APP_BACKEND_URL as string + `api/sessions/` + auth.currentUser?.uid, {
                method: "GET",
                headers: {
                    "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                    "Content-Type": "text/plain"
                }
            }).then(r => {
                r.json().then(value1 => {
                    setLanguage(value1.selected)
                    for (let x of value1.problemsList) {
                        if (x.problemId === problemId) {
                            setCode(x.templates[language] as string)
                            if (!hasWrittenCode) {
                                setWrittenCode(code)
                            }
                            break
                        }
                    }
                })
            })
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

    function updateLanguage(event: React.ChangeEvent<HTMLSelectElement>) {
        const old = language
        setHasWrittenCode(false)
        setLanguage(event.currentTarget.value)
        fetch(process.env.REACT_APP_BACKEND_URL as string + `api/sessions/update/` + auth.currentUser?.uid, {
            method: "POST",
            headers: {
                "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                language: old,
                newLanguage: event.currentTarget.value,
                problemId: problemId,
                code: writtenCode
            })
        }).then(r => {
            console.log(r.status)
        })
    }

    function updateProblem(event: React.ChangeEvent<HTMLSelectElement>) {
        const old = problemId
        setHasWrittenCode(false)
        setProblemId(parseInt(event.currentTarget.value))
        fetch(process.env.REACT_APP_BACKEND_URL as string + `api/sessions/update/` + auth.currentUser?.uid, {
            method: "POST",
            headers: {
                "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                language: language,
                newLanguage: event.currentTarget.value,
                problemId: old,
                code: writtenCode
            })
        }).then(r => {
            console.log(r.status)
        })
    }

    function updateCode(value: string | undefined, ev: editor.IModelContentChangedEvent) {
        if (value != undefined) {
            setHasWrittenCode(true)
            setWrittenCode(value)
            fetch(process.env.REACT_APP_BACKEND_URL as string + `api/sessions/update/` + auth.currentUser?.uid, {
                method: "POST",
                headers: {
                    "Authorization": process.env.REACT_APP_BACKEND_AUTH_KEY as string,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    language: language,
                    newLanguage: language,
                    problemId: problemId,
                    code: writtenCode
                })
            }).then(r => {
                console.log(r.status)
            })
        }
    }

    return (
        <Fragment>
            <TitleComponent/>
            <div className={"room"}>
                <Editor height="50vh" width="40vw" defaultLanguage="python" value={writtenCode} theme={"vs-dark"}
                        onChange={updateCode}/>
                <p>Members</p>
                <div className="members">
                    <ul>
                        {users.map(value => <li>{value}</li>)}
                    </ul>
                </div>
                <select name="options" id="options" onChange={updateLanguage} value={language}>
                    <option value="PYTHON">Python</option>
                    <option value="JAVA">Java</option>
                </select>

                <select name="problems" id="problems" onChange={updateProblem} value={problemId}>
                    <option value={1}>Two Sum</option>
                    <option value={33}>Search in Rotated Sorted Array</option>
                </select>
            </div>
        </Fragment>

    )

}

export default Room;