import React, { Component } from 'react'
import Header from './HeaderCustomerService';
import FundFinder from './FundFinder';
import AddFund from './AddFund';
import AddEntitlements from './AddEntitlements';
import parseJwt from './utility/JwtParser';
import getCookie from './Cookie';

class CustomerService extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             childComponents: [<FundFinder/>, <AddFund/>, <AddEntitlements/>],
             userId: "-",
             name: "",
             currTab: 0
        }
    }

    tabHandler = tab =>{
        this.setState({
            currTab: tab
        });
    }
    componentDidMount(){
        var token = getCookie("token");
        if(!token){
            this.props.history.push("/")
        }
        
        var jwtInfo = parseJwt(token);
        // console.log(jwtInfo);
        this.setState({
            userId: jwtInfo.sub,
            name: jwtInfo.name
        })
    }

    render() {
        var component = this.state.childComponents[this.state.currTab];
        return (
            <div>
                <Header name={this.state.name} tabHandler={this.tabHandler}></Header>
                {component}
            </div>
        )
    }
}

export default CustomerService
