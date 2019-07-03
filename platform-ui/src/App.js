import React, { Component } from 'react';
import './App.css';
import HeaderPortfolio from './components/HeaderPortfolio';
import UserFunds from './components/UserFundTable';
import Preferences from './components/Preferences'

class App extends Component{
  constructor(props) {
    super(props)
  
    this.state = {
      childComponents: [<UserFunds portfolio={true} />, <UserFunds portfolio={false} />, <Preferences/>],
      currTab: 0
    }
  }

  tabHandler = tab =>{
    this.setState({
        currTab: tab
    });
  }
  
  render(){
    var component = this.state.childComponents[this.state.currTab];
    return (
      <div className="App">
        <HeaderPortfolio tabHandler={this.tabHandler}></HeaderPortfolio>
        {component}
      </div>
    );
  }
}

export default App;
