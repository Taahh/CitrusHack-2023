import React from "react";
import { useNavigate } from "react-router-dom";

const TitleComponent = () => {
    const nav = useNavigate()
    return (<h3 className="webName" onClick={() => nav("/")}>OURSEARCH</h3>)
}

export default TitleComponent