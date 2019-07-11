import React, { Component } from 'react';
import 'materialize-css/dist/css/materialize.min.css';
import M from 'materialize-css'
import './css/preferences.css'
import axios from 'axios';
import getCookie from './Cookie';


function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};

class Preferences extends Component {
    constructor(props){
        super(props);

        this.state={
            userId:'',
            fullName:'',
            baseCurr:'',
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
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else{
            // console.log(jwt);
            axios({
                method: "POST",
                url: "http://localhost:8762/portfolio/update/baseCurrency?Currency=" + this.state.baseCurr, 
                headers : { Authorization: `Bearer ${jwt}` } 
            })
            .then( res => {
                window.location = "/portfolio"
            })
            .catch( error => {
                if(error.response){
                    if(error.response.status === 500){
                        this.setState({
                            errorResponse: [<p>The server is down at the moment. Please try again later.</p>]
                        })
                    }else if (error.response.status === 403){
                        this.setState({
                            errorResponse: [<p>You are not authorized to access this page. Please Logout.</p>]
                        })
                    }else if (error.response.status === 401){
                        document.cookie = "token= ";
                        window.location = "/"
                    }
                }
            });
        }
    }


    componentDidMount(){
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else{
            axios.get('http://localhost:8762/portfolio', {headers : { Authorization: `Bearer ${jwt}` } })
            .then( res => {
                this.setState({
                    userId : res.data.userId,
                    fullName : parseJwt(jwt).name,
                    baseCurr: res.data.baseCurr
                })
                // console.log(res.data);
            }).catch( error => {
                if(error.response){
                    if(error.response.status === 500){
                        this.setState({
                            errorResponse: [<p>The server is down at the moment. Please try again later.</p>]
                        })
                    }else if (error.response.status === 403){
                        this.setState({
                            errorResponse: [<p>You are not authorized to access this page. Please Logout.</p>]
                        })
                    }else if (error.response.status === 401){
                        document.cookie = "token= ";
                        window.location = "/"
                    }
                }
            });
        }
    }




    render(){ 
        M.updateTextFields();
        return (
            <div class="preferences-container">
                <div className="error-response">
                    {this.state.errorResponse}
                </div>
                <div className="row page-content">
                    <div className="card-container">
                    <div className="card ">
                        <div className="card-action">
                            <b><span id="info">Preferences</span></b>
                            <span id="userId">{this.state.userId}</span>
                        </div>
                        <div className="card-content black-text">
                            <form className="row" onSubmit={this.handleSubmit}>
                                <div className="col s12 form-content">
                                    <label htmlFor="fullName" className="label-content">
                                        Name :
                                    </label>
                                    <textarea type="text" id="fullName" value={this.state.fullName} disabled readOnly></textarea>
                                </div>
                                <div className="col s12 form-content">
                                    <label className="label-content">
                                        Base Currency :
                                    </label>
                                    <select id="baseCurr" className="browser-default" value={this.state.baseCurr} onChange={this.handleChange} required>
                                        <option value="INR">Indian Rupees</option>
                                        <option value="USD">United States Dollars</option>
                                        <option value="GBP">Great Britain Pounds</option>
                                        <option value="EUR">Euros</option>
                                        <option value="AED">Arab Emirates Dollar</option>
                                        <option value="SAR">Saudi Arabian Riyal</option>
                                    </select>
                                </div>
                                
                                <div className="button-container col s12">
                                    <button className="btn waves-effect" type="submit">Submit</button>
                                </div>
                            </form>                            
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Preferences;