import './App.css';
import { Login } from './Login';
import MainRuoter from './MainRuoter'

function App() {
  //return (<Login></Login>);
  return (
    <div className="App">
      <MainRuoter></MainRuoter>
      {/* <header className="App-header">
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
      </header> */}
    </div>
  );
}

export default App;
