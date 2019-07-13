import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import M from 'materialize-css/dist/js/materialize.min.js'
import './css/addFund.css'
import axios from 'axios';
import getCookie from './Cookie';
import { withRouter } from 'react-router-dom';
import FileUpload from './FileUploadComponent';
import autocomplete from './utility/Autocomplete';

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
            moodysRating: "",
            update: false,
            funds: [],
            fundSuggestions: [],
            errorResponse: []
        }
    }
    

    onChange = event =>{
        var value = event.target.value;
        
        if(event.target.type==='number' && event.target.id!=='seCycle'){
            value = parseFloat(value)
        } else if(event.target.type==='number'){
            value = parseInt(value)
        }
        
        this.setState({
            [event.target.id]: value
        })
    }

    addFundRequest = (event) =>{
        event.preventDefault()
        this.setState({
            fundNumber: document.getElementById('fundNumber').value
        }, () =>{
            var baseUrl = "http://localhost:8762/fund-handling/api/funds/";
            var token =getCookie('token');
            
            if(!token){
                this.props.history.push('/');
            }
            var headers ={
                Authorization: 'Bearer ' + token
            }
            axios({
                method: (this.state.update? 'patch': 'post'),
                url: baseUrl + (this.state.update? 'update': 'create'),
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
                }else if (error.response.status === 500){
                    this.setState({
                        errorResponse: [<p>The server is down. Please try again later.</p>]
                    })
                }else{
                    alert(error.response.data)
                }
            });
        })
        
    }

    onSwitchChanged = (event) =>{
        this.setState({
            update: event.target.checked
        })
    }

    componentDidMount(){
        var baseUrl = "http://localhost:8762/";
        var token = getCookie('token');
        if(!token){
            this.props.history.push('/');
        }
        // Setting headers
        var headers ={
            Authorization: 'Bearer ' + token
        }

        axios({
            method: 'get',
            url: baseUrl + 'fund-handling/api/funds/all',
            headers: headers
        }).then(response =>{
            console.log(response.data);
            var fundNumbers = [];
            response.data.forEach(fund => {
                fundNumbers.push(fund.fundNumber);
            });
            this.setState({
                funds: response.data,
                fundSuggestions: fundNumbers
            })
        }).catch(error =>{
            console.log(error);
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }
        });
    }

    componentDidUpdate(){
        autocomplete(document.getElementById("fundNumber"), this.handleSearchItemClick, 
        this.state.fundSuggestions, this.state.funds)
    }

    handleSearchItemClick = fundNumber =>{
        document.getElementById('fundNumber').value = fundNumber;
        for(var i=0; i< this.state.funds.length; i++){
            var fund = this.state.funds[i];
            if(fundNumber === fund.fundNumber){
                this.setState({
                    fundNumber: fund.fundNumber,
                    fundName: fund.fundName,
                    invManager: fund.invManager,
                    setCycle: fund.setCycle,
                    nav: fund.nav,
                    invCurrency: fund.invCurrency,
                    sAndPRating: fund.sAndPRating,
                    moodysRating: fund.moodysRating
                })
                break;
            }
        }
    }

    render() {
        M.updateTextFields();
        return (
            <div className="form-container center-align">
                <div class="error-response">
                    {this.state.errorResponse}
                </div>
                <FileUpload endUrl='funds/addFund' buttonText='Add Fund from File'></FileUpload>
                <div className="form-card">
                    <div className="row">
                        <form className="col s12">
                            <div className="row">
                                <div className="switch">
                                    <label>
                                        Add fund
                                        <input type="checkbox" onChange = {this.onSwitchChanged}/>
                                        <span className="lever"></span>
                                        Update fund
                                    </label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12 autocomplete">
                                    <input id="fundNumber" type="text" className="validate" autoComplete="off"/>
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
