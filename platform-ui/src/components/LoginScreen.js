import React, { Component } from 'react';
import 'materialize-css/dist/css/materialize.min.css';
import Login from './Login';

class LoginScreen extends Component{
    constructor(props){
        super(props);
        this.state={
            userId:'',
            password:'',
            loginScreen:[]
        }
    }
    
    componentDidMount(){
        var loginScreen = [];
        loginScreen.push(<Login key="1"/>);
        this.setState({
            loginScreen:loginScreen
        })
    }

    render(){
        return(
            <div className="loginScreen">
                {this.state.loginScreen}
            </div>
        );
    }
}

export default LoginScreen;