import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import M from 'materialize-css/dist/js/materialize.min.js'
import './css/addFund.css'

class AddFund extends Component {
    render() {
        M.updateTextFields();
        return (
            <div className="form-container center-align">
                <button className="btn waves-effect waves-light" id="csv-add" name="action">Add Funds from CSV
                            </button>
                <div className="form-card">
                    <div className="row">
                        <form className="col s12">
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="fundNumber" type="text" className="validate"/>
                                    <label htmlFor="fundNumber">Fund Number</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="fundName" type="text" className="validate"/>
                                    <label htmlFor="fundName">Fund Name</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="invManager" type="text" className="validate"/>
                                    <label htmlFor="invManager">Investment Manager</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="setCycle" type="text" className="validate"/>
                                    <label htmlFor="setCycle">Settlement Cycle(in days)</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="nav" type="text" className="validate"/>
                                    <label htmlFor="nav">NAV</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <select id="invCurrency" defaultValue="">
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
                                    <input id="sAndPRating" type="text" className="validate"/>
                                    <label htmlFor="sAndPRating">S and P Rating</label>
                                </div>
                            </div>
                            <div className="row">
                                <div className="input-field col s12">
                                    <input id="moodysRating" type="text" className="validate"/>
                                    <label htmlFor="moodysRating">Moody's Rating</label>
                                </div>
                            </div>
                            <button className="btn waves-effect waves-light" type="submit" name="action">Submit
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

export default AddFund
