import React, { Component } from 'react'
import Header from './HeaderCustomerService';
import FundFinder from './FundFinder';
import AddFund from './AddFund';
import AddEntitlements from './AddEntitlements';

class CustomerService extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             childComponents: [<FundFinder/>, <AddFund/>, <AddEntitlements/>],
             currTab: 0
        }
    }

    tabHandler = tab =>{
        this.setState({
            currTab: tab
        });
    }

    render() {
        var component = this.state.childComponents[this.state.currTab];
        return (
            <div>
                <Header tabHandler={this.tabHandler}></Header>
                {component}
            </div>
        )
    }
}

export default CustomerService
