import './App.scss';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Privacy from './pages/Privacy';
import Main from './pages/Main';
import Credits from './pages/Credits';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Switch>
          <Route exact path="/privacy" component={Privacy}></Route>
          <Route exact path="/credits" component={Credits}></Route>
          <Route path="/" component={Main}></Route>
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
