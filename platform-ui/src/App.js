import React from 'react';
import logo from './logo.svg';
import './App.css';
import GetList from './components/PostList';
import Header from './components/HeaderCustomerService';
import AddFund from './components/AddFund';
import SearchBar from './components/SearchBar';
import FundFinder from './components/FundFinder';

function App() {
  return (
    <div className="App">
      <Header></Header>
      <FundFinder></FundFinder>
    </div>
  );
}

export default App;
