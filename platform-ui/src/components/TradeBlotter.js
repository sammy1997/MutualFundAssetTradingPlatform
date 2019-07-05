//component for displaying funds

import React, { Component } from 'react'
// import './tradeBlotter.css' 
import AddTrade from './AddTrade'
import FundItem from './FundItem';
import VerifyButton from './VerifyButton';
import 'materialize-css/dist/css/materialize.min.css';
import './css/tradeBlotter.css'
//import M from 'materialize-css'

class TradeBlotter extends Component {

    constructor(props) {
        super(props)
    
        this.state = {
            funds: []
        }
    }

    // Set funds from fund finder page ui 
    componentDidMount () {
        this.setState({
            funds: this.props.funds 
        })
    }

    // Add Trade method
    addTrade = function (fundName, fundNumber, invManager) {
        // console.log({fundName}, {fundNumber}); 
        const newFund = {
            fundName,
            fundNumber,
            invManager
           }
           this.setState (
            {
                funds: [...this.state.funds, newFund]
            })
    }.bind(this);

    

    render() {
        return (
            <div className="page-content">
                
                <table className='centered' id="trade-blotter-table">
                    <thead>
                        <tr>
                            <th>Fund Name</th>
                            <th>Fund Number</th>
                            <th>Investment Manager</th>
                            <th>Quantity</th>
                            <th>Buy/Sell</th>
                        </tr>
                    </thead>

                    <tbody> {
                        // Render all the funds 
                        this.state.funds.map((f) => (
                            <FundItem className = "fund-item" fundName={f.fundName} fundNumber={f.fundNumber} invManager={f.invManager}
                             /> 
                        ))
                        }                          
                    </tbody>
                </table>
                        
                <AddTrade addTrade={this.addTrade} numberOfTrades={this.state.funds.length}/>
                <VerifyButton numberOfTrades={this.props.funds.length}/> 
            </div>
        )
    }
}

export default TradeBlotter;