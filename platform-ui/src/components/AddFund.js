import React, { Component } from 'react'
import './css/materialize.css'
import './css/addFund.css'

class AddFund extends Component {
    render() {
        return (
            <div className="form-container center-align">
                <a className="waves-effect waves-light btn deep-purple lighten-2">Add Funds from CSV File</a>
                <div className="form-card">
                    <div className="row"></div>
                </div>
            </div>
        )
    }
}

export default AddFund
