import React, { Component } from 'react';
import 'materialize-css/dist/css/materialize.min.css';
import M from 'materialize-css'
import axios from 'axios';
import './css/login.css' ;
import { Link } from 'react-router-dom';
import Status from './Status';

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};

class Login extends Component {
    constructor(props){
        super(props);
        
        this.state={
            userId:'',
            password:'',
            status: []
        }
    }

    handleChange = (event) => {
        this.setState({
            [event.target.id]: event.target.value
        });
    }

    handleSubmit = (event) => {
        event.preventDefault();
        var baseUrl = "http://localhost:8762/";
        var payload = {
            "userId": this.state.userId,
            "password": this.state.password
        }
        axios({
            method:'post',
            url: baseUrl + "auth",
            data: payload
        }).then(function (response){
            var authorization = response.headers['authorization'];
            var token = authorization.replace('Bearer ','');
            document.cookie = "token=" + token;
            var role = parseJwt(token).authorities[0];
            // console.log(sub);
            if((role === "ROLE_TRADER") || (role === "ROLE_VIEWER")){
                window.location = "/portfolio";
            }else{
                window.location = "/admin";
            }
        })
        .catch(function(error){
            console.log(error.response.status);
        })
    }

    render(){ 
        M.updateTextFields();
        return (
            <div>
                <div className="top">
                    <h1>Login</h1>
                </div>
                <div>
                    <div className="row form-container-custom">
                        <form className="form-custom" onSubmit={this.handleSubmit}>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="userId" type="text" value={this.state.userId} onChange={this.handleChange}/>
                                    <label htmlFor="userId">User Id</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="password" type="password" value={this.state.password} onChange={this.handleChange}/>
                                    <label htmlFor="password">Password</label>
                                </div>
                            </div>

                            <div className="row">
                                <div className="button-container">
                                    <button className="btn waves-effect" type="submit">Submit</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

export default Login;