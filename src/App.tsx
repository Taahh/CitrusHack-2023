import React, { useRef, useState } from 'react';
import { getAuthentication } from "./index";
import { useNavigate } from "react-router-dom";

function App() {
  const auth = getAuthentication()
  const nav = useNavigate()

  auth.onAuthStateChanged(value => {
    if (!value) {
      nav("/login")
    }
  })
  return (
    <div className="App">
    </div>
  );
}

export default App;
