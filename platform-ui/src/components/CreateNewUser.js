import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import M from 'materialize-css/dist/js/materialize.min.js'
import $ from 'jquery'
import './css/createNewUser.css'

class CreateNewUser extends Component{
    constructor(props){
        super(props)
        
        this.state = {
            userId: "",
            password: "",
            fullName: "",
            currBal: undefined,
            baseCurr: undefined,
            role: undefined,
            expanded: false
        }
    }

    showCheckboxes() {
        
        var checkboxes = document.getElementById("checkboxes");
        if (!this.state.expanded) {
            checkboxes.style.display = "block";
            this.setState({
                expanded : true
            })
        } else {
            checkboxes.style.display = "none";
            this.setState({
                expanded : false
            })
        }
    }

    render(){
        M.updateTextFields();
        
        
    
        

        
        
        return(
            <div className="form-container center-align">
                <div class="error-response">
                    {this.state.errorResponse}
                </div>
                <div className="form-card">
                    <div className="row">
                        <form className="col s12">
                            <div className="row">
                                <div className="input-field col s12 autocomplete">
                                    <input id="userId" type="text" className="validate" autoComplete="off"/>
                                    <label htmlFor="userId">User ID</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="password" value={this.state.password} type="password" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="password">Password</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="fullName" value={this.state.fullName} type="text" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="fullName">Full Name</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="currBal" value={this.state.currBal} type="number" 
                                        onChange={this.onChange} className="validate"/>
                                    <label htmlFor="currBal">Current Balance</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <select id="baseCurr" defaultValue="" onChange={this.onChange}>
                                        <option value="" disabled>Base Currency</option>
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
                                <div class="input-field col s12">
                                    <div class="multiselect">
                                        <div class="selectBox" onclick={this.showCheckboxes}>
                                        <select>
                                            <option>Select an option</option>
                                        </select>
                                        <div class="overSelect"></div>
                                        </div>
                                        <div id="checkboxes">
                                            <label for="one">
                                                <input type="checkbox" id="one" />First checkbox</label>
                                            <label for="two">
                                                <input type="checkbox" id="two" />Second checkbox</label>
                                            <label for="three">
                                                <input type="checkbox" id="three" />Third checkbox</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
    

}

export default CreateNewUser