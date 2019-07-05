// component for creating fund item  
import React, { Component } from 'react';
import './css/tradeBlotter.css'

export class FundItem extends Component {

    render() {
        const {fundName, fundNumber, invManager} = this.props;
        return (
            <tr id="trading-fund-list"> 
                <td>{fundName}</td>
                <td>{fundNumber}</td>
                <td>{invManager}</td>
                <td><input id={"quantity-" + fundNumber} type="number"></input></td>
                <td>
                    <select>
                        <option value="sell">Sell</option>
                        <option value="purchase">Buy</option>
                    </select>
                </td>
            </tr> 
        )
    }
}

export default FundItem