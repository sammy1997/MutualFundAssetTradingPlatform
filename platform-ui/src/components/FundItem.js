// component for creating fund item  
import React, { Component } from 'react';
import './css/tradeBlotter.css'

export class FundItem extends Component {

    constructor(props) {
        super(props)
    
        this.state = {
            quantity: 0,
            status: "sell"
        }
    }
    
    

    onChangeHandlerQuantity = (event) => {
        this.setState({
            quantity: event.target.value
        }, () => {
            console.log(this.state.quantity)
            const trade = {
                fundNumber: this.props.fundNumber,
                quantity: this.state.quantity,
                status: this.state.status
            }
            this.props.callBack(trade) 
        })
    }

    onChangeHandlerStatus = (event) => {
        this.setState({
            status: event.target.value
        }, () => {
            console.log(this.state.status)
            const trade = {
                fundNumber: this.props.fundNumber,
                quantity: this.state.quantity,
                status: this.state.status
            }
            this.props.callBack(trade)
        })       
    }

    delHandler = () => {
        this.props.delFund(this.props.fundNumber)
    }

    componentDidMount(){
        const trade = {
            fundNumber: this.props.fundNumber,
            quantity: this.state.quantity,
            status: this.state.status
        }
        this.props.callBack(trade)
    }

    render() {
        const {fundName, fundNumber, invManager, currentNav} = this.props;
        return (
            <tr id="trading-fund-list" key={fundNumber}> 
                <td>{fundName}</td>
                <td>{fundNumber}</td>
                <td>{invManager}</td>
                <td><input id={"quantity-" + fundNumber} className="quantity-field" type="number" placeholder="Enter quantity"  onChange={this.onChangeHandlerQuantity}></input></td>
                <td>{currentNav}</td>
                <td class="purchase-sell-switch">
                    <select onChange={this.onChangeHandlerStatus}>
                        <option value="sell">Sell</option>
                        <option value="purchase">Buy</option>
                    </select>
                </td>
                <td className="remove-button"><button id="x-button" class="delete-button" onClick={this.delHandler}>x</button></td>
            </tr> 
              
        )
    }


    
}

export default FundItem