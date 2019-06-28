import React from 'react';
import logo from './logo.svg';
import './App.css';
import GetList from './components/PostList';
import Header from './components/HeaderCustomerService';
import AddFund from './components/AddFund';
import SearchBar from './components/SearchBar';
import FundFinder from './components/FundFinder';
import AddEntitlements from './components/AddEntitlements';

function App() {
  return (
    <div className="App">
      <Header></Header>
      <AddEntitlements></AddEntitlements>
    </div>
  );
}

export default App;
