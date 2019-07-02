import React from 'react';
import ReactDOM from 'react-dom';
import { Route, Link, Redirect, Switch, BrowserRouter as Router } from 'react-router-dom';
import './index.css';
import App from './App';
// import AddFund from './components/AddFund';
// import Login from './components/Login';
import LoginScreen from './components/LoginScreen';
import * as serviceWorker from './serviceWorker';
import PreferencesScreen from './components/PreferencesScreen';
import CustomerService from './components/CustomerService';
import AuthComponent from './components/AuthComponent';

const routing = (
    <Router>
      <Switch>
        {/* <div> */}
          <Route path="/" exact component={LoginScreen} />
          <AuthComponent>
            <Route path="/portfolio" exact component={App} />
            <Route path="/admin" exact component={CustomerService} />
            <Route path="/preferences" exact component={PreferencesScreen} />
          </AuthComponent>
        {/* </div> */}
      </Switch>
    </Router>
  )

ReactDOM.render(routing, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
