import React, { Component } from 'react';
import './App.css';
import HeaderPortfolio from './components/HeaderPortfolio';
import UserFunds from './components/UserFundTable';
import Preferences from './components/Preferences'

class App extends Component{
  constructor(props) {
    super(props)
  
    this.state = {
        childComponents: [<UserFunds portfolio={true} key="1" />, <UserFunds portfolio={false} key="2" />, <Preferences/>],
        currTab: 0,
        active: false 
    }
  }

  tabHandler = tab =>{
    const currentState = this.state.active;
    this.setState({
        currTab: tab,
        active: !currentState
    });
  }
  
  render(){
    var component = this.state.childComponents[this.state.currTab];
    return (
      <div className="App">
        <HeaderPortfolio active={this.state.active} tabHandler={this.tabHandler}></HeaderPortfolio>
        {component}
      </div>
    );
  }
}

export default App;
