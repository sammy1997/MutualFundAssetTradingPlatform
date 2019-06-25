import React from 'react';
import logo from './logo.svg';
import './App.css';
import GetList from './components/PostList';
import Header from './components/HeaderCustomerService';
import AddFund from './components/AddFund';

function App() {
  return (
    <div className="App">
      <Header></Header>
      <AddFund></AddFund>
    </div>
  );
}

export default App;
