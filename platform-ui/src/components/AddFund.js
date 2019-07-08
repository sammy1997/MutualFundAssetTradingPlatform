import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import M from 'materialize-css/dist/js/materialize.min.js'
import './css/addFund.css'
import axios from 'axios';
import getCookie from './Cookie';
import { withRouter } from 'react-router-dom';
import FileUpload from './FileUploadComponent';

class AddFund extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
            fundNumber: "",
            fundName: "",
            invManager: "",
            setCycle: "",
            nav: "",
            invCurrency: "",
            sAndPRating: "",
            moodysRating: ""
        }
    }
    

    onChange = event =>{
        var value = event.target.value;
        if(event.target.type==='number' && event.target.id!=='seCycle'){
            value = parseFloat(value)
        }else if(event.target.type==='number'){
            value = parseInt(value)
        }
        this.setState({
            [event.target.id]: value
        })
    }

    addFundRequest = (event) =>{
        event.preventDefault()

        var baseUrl = "http://localhost:8762/fund-handling/api/funds/";
        var token =getCookie('token');
        
        if(!token){
            this.props.history.push('/');
        }
        var headers ={
            Authorization: 'Bearer ' + token
        }

        axios({
            method: 'post',
            url: baseUrl + 'create',
            headers: headers,
            data: this.state
        }).then(response =>{
            console.log(response.data)
            alert(response.data)
        }).catch(error =>{
            // console.log(error.response);
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }
            else{
                alert(error.response.data)
            }
        });
    }

    render() {
        M.updateTextFields();
        return (
            <div className="form-container center-align">
                <FileUpload endUrl='funds/addFund' buttonText='Add Fund from File'></FileUpload>
                <div className="form-card">
                    <div className="row">
                        <form className="col s12">
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="fundNumber" value={this.state.fundNumber} type="text" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="fundNumber">Fund Number</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="fundName" value={this.state.fundName} type="text" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="fundName">Fund Name</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="invManager" value={this.state.invManager} type="text" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="invManager">Investment Manager</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="setCycle" value={this.state.setCycle} type="number" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="setCycle">Settlement Cycle(in days)</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="nav" value={this.state.nav} type="number" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="nav">NAV</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <select id="invCurrency" defaultValue="" onChange={this.onChange}>
                                        <option value="" disabled>Investment currency</option>
                                        <option value="INR">INR</option>
                                        <option value="USD">USD</option>
                                        <option value="EUR">EUR</option>
                                        <option value="AED">AED</option>
                                        <option value="GBP">GBP</option>
                                        <option value="SAR">SAR</option>
                                        <option value="JPY">JPY</option>
                                    </select>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="sAndPRating" value={this.state.sAndPRating} type="number" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="sAndPRating">S and P Rating</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="moodysRating" value={this.state.moodysRating} type="number" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="moodysRating">Moody's Rating</label>
                                </div>
                            </div>
                            <button className="btn waves-effect waves-light" type="submit" 
                                onClick={this.addFundRequest}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

export default withRouter(AddFund)
