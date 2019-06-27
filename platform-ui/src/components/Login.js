import React, { Component } from 'react';
import 'materialize-css/dist/css/materialize.min.css';
import M from 'materialize-css'
import axios from 'axios';
import './css/login.css' 


class Login extends Component {
    constructor(props){
        super(props);
        
        this.state={
            userId:'',
            password:''
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
            console.log(response);  
        }).catch(function(error){
            console.log(error);
        })
    }

    render(){ 
        M.updateTextFields();
        return (
            <div>
                <div className="top">
                    <h1>Login</h1>
                </div>
                <div className="">
                    <div className="row form-container-custom">
                        <form className="form-custom" onSubmit={this.handleSubmit}>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="userId" type="text" className="validate" value={this.state.userId} onChange={this.handleChange}/>
                                    <label htmlFor="userId">User Id</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="password" type="password" className="validate" value={this.state.password} onChange={this.handleChange}/>
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