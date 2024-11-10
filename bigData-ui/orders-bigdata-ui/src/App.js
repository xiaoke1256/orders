import logo from './logo.svg';
import './App.css';
import MainRuoter from './MainRuoter';
import {Link} from "react-router-dom";

function App() {
  return (
    <div className="App">
      <MainRuoter></MainRuoter>
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
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
