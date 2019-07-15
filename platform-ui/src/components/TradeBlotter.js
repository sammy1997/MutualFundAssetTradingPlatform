//component for displaying funds

import React, { Component } from 'react'
// import './tradeBlotter.css' 
import AddTrade from './AddTrade'
import FundItem from './FundItem';
import VerifyButton from './VerifyButton';
import 'materialize-css/dist/css/materialize.min.css';
import './css/tradeBlotter.css'
import LoadingOverlay from 'react-loading-overlay';
import Loader from './Loader';
//import M from 'materialize-css'


class TradeBlotter extends Component {

    constructor(props) {
        super(props)
    
        this.state = {
            funds: this.props.funds,
            fundsPrevious: [], 
            trades: [],
            verified: false 

        }
    }


    // Set funds from fund finder page ui 
    componentDidMount = (prevState) => {
        this.setState({ 
            verified: false 
        }, () => {
            console.log(this.state.funds)
        })
    }

    unVerify = function() {
        this.child.unVerifyHandler()
    }.bind(this)

    // Add Trade method
    addTrade = function (fundName, fundNumber, invManager, presentNav) {
        // console.log({fundName}, {fundNumber}); 
        const newFund = {
            fundName,
            fundNumber,
<<<<<<< HEAD
            invManager,
            presentNav
           }
           this.setState (
            {
=======
            invManager
        }

        var currFunds = [...this.state.funds]
        var index = currFunds.findIndex(obj => obj.fundNumber === newFund.fundNumber)
        if (index!=-1) {
            alert('Fund already selected')
        } else {
            this.setState ({
>>>>>>> 8b88b1cb93f22aecc135b20fba80a7694aa608ce
                funds: [...this.state.funds, newFund]
            })
            this.unVerify()
        }       
    }.bind(this);

    delTrade = (fundNum) => {
        // var getFunds = [...this.state.funds]
        // var obj = getFunds.find(o => o.fundNumber === fundNumber)
        // getFunds.splice(obj, 1)

        // var getTrades = [...this.state.trades]
        // var obj = getTrades.find(o => o.fundNumber === fundNumber)
        // getTrades.splice(obj, 1)
        this.setState({
            funds: [...this.state.funds.filter(fund => fund.fundNumber !== fundNum)],
            trades: [...this.state.trades.filter(trade => trade.fundNumber !== fundNum)]
        }, () => {console.log(this.state.funds)})
    }

    callBackFund = (trade) => {
        const newTrade = {
            fundNumber: trade.fundNumber,
            quantity: trade.quantity,
            status: trade.status
        }
    
        var newTrades = [...this.state.trades]
        var index = newTrades.indexOf(newTrades.find(t => t.fundNumber === newTrade.fundNumber))

        if (index!=-1){
            newTrades.splice(index, 1)
            newTrades.push(newTrade)
        } else {
            newTrades.push(newTrade)
        }

        this.setState({
            trades: newTrades   
        })
    
    }
    


    render() {
        console.log(this.props.funds);
        return (
            <div className="page-content">
                <h3 align="center">Trade Blotter</h3>
                <table className='centered' id="trade-blotter-table">
                    <thead>
                        <tr>
                            <th>Fund Name</th>
                            <th>Fund Number</th>
                            <th>Investment Manager</th>
                            <th>Quantity</th>
                            <th>Current NAV</th>
                            <th>Buy/Sell</th>
                        </tr>
                    </thead>

                    <tbody> {
                        // Render all the funds 
                        this.state.funds.map((f) => (
                            <FundItem className = "fund-item" key={f.fundNumber} fundName={f.fundName} fundNumber={f.fundNumber} 
                            invManager={f.invManager} callBack={this.callBackFund} delFund = {this.delTrade} />
                        ))
                        }                          
                    </tbody>
                </table>
                        
                <AddTrade addTrade={this.addTrade} numberOfTrades={this.state.funds.length} unverify={this.unVerify}/>
                <VerifyButton numberOfTrades={this.props.funds.length} trades={this.state.trades} 
                verified={this.state.verified} onRef={ref => (this.child = ref)}/> 
    
            </div>
        )
    }
}

export default TradeBlotter;