import React, { Component } from 'react';
import 'materialize-css/dist/css/materialize.min.css';
import M from 'materialize-css'
import axios from 'axios';
import './css/login.css' ;
import parseJwt from './utility/JwtParser'

class Login extends Component {
    constructor(props){
        super(props);
        
        this.state={
            userId:'',
            password:'',
            errorResponse: []
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
            if((role === "ROLE_TRADER") || (role === "ROLE_VIEWER")){
                window.location = "/portfolio";
            }else{
                window.location = "/admin";
            }
        })
        .catch(error => {
            if(error.response){ 
                if(error.response.status === 401 ){
                    this.setState({
                        errorResponse: [<p>Invalid Credentials</p>],
                        userId: '',
                        password: ''
                    })
                }else if(error.response.status === 500){
                    this.setState({
                        errorResponse: [<p>The server is down. Please try again later.</p>],
                        userId: '',
                        password: ''
                    })
                }
            }
        });
    }

    render(){ 
        M.updateTextFields();
        return (
            <div>
                <div className="top">
                    <h1>Login</h1>
                </div>
                <div className="error-response">
                    {this.state.errorResponse}
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