import React, { Component } from 'react';
import './App.css';
import HeaderPortfolio from './components/HeaderPortfolio';
import UserFunds from './components/UserFundTable';
import Preferences from './components/Preferences'

class App extends Component{
  constructor(props) {
    super(props)
  
    this.state = {
        childComponents: [<UserFunds updateAssets={this.updateAssets} portfolio={true} key="1" />, 
                          <UserFunds updateAssets={this.updateAssets} portfolio={false} key="2" />, <Preferences/>],
        currTab: 0,
        assets: 0
    }
    this.updateAssets = this.updateAssets.bind(this);
  }

  updateAssets = newAssetValue =>{
    this.setState({
      assets: newAssetValue
    });
  }
  


  tabHandler = tab =>{
    this.setState({
        currTab: tab
    });
    var tablet = document.getElementsByClassName('tab');
    var numberOfTabs = this.state.childComponents.length;
    if(!tablet[tab].classList.contains('active')){
       for(var i=0; i<numberOfTabs; i++){
         tablet[i].classList.remove('active');
       }
       tablet[tab].classList.add('active'); 
    }
  }
  
  render(){
    var component = this.state.childComponents[this.state.currTab];
    
    return (
      <div className="App">
        <HeaderPortfolio totalAssets={this.state.assets} active={this.state.active} tabHandler={this.tabHandler}></HeaderPortfolio>
        {component}
      </div>
    );
  }
}

export default App;
