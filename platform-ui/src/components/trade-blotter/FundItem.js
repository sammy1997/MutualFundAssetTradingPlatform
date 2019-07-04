// component for creating fund item  
import React, { Component } from 'react';

export class FundItem extends Component {

    render() {
        const {fundName, fundNumber, invManager, invCurr, 
            setCycle, nav, sAndPRating, moodyRating, quantity} = this.props;
        return (
            <tr> 
                <td>{fundName}</td>
                <td>{fundNumber}</td>
                <td>{invManager}</td>
                <td>{invCurr}</td>
                <td>{setCycle}</td>
                <td>{nav}</td>
                <td>{sAndPRating}</td>
                <td>{moodyRating}</td>
                <td>{quantity}</td>
            </tr> 
        )
    }
}

export default FundItem