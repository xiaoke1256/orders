import React from 'react';
import logo from './logo.svg';
import './App.css';
import {Link} from "react-router-dom";
import MainRuoter from './MainRuoter';

function App() {
  return (
    <div className="App">
      <MainRuoter></MainRuoter>
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <Link to="/home">Home</Link>
      </header>
    </div>
  );
}

export default App;
