import React, { Component } from 'react';
import 'materialize-css/dist/css/materialize.min.css';
import M from 'materialize-css'
import './css/preferences.css'

class Preferences extends Component {
    constructor(props){
        super(props);

        this.state={
            userId:'Sammy',
            fullName:'',
            baseCurr:''
        }
    }

    handleChange = (event) => {
        this.setState({
            [event.target.id]: event.target.value
        });
    }

    handleSubmit = (event) => {
        event.preventDefault();
    }

    render(){ 
        M.updateTextFields();
        return (
            <div>
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
                                    <textarea type="text" id="fullName" value={this.state.fullName} onChange={this.handleChange} required></textarea>
                                </div>
                                <div className="col s12 form-content">
                                    <label className="label-content">
                                        Base Currency :
                                    </label>
                                    <select className="browser-default">
                                    <option value="" disabled selected>Choose your option</option>
                                        <option value="INR">INR</option>
                                        <option value="USD">USD</option>
                                        <option value="GBP">GBP</option>
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