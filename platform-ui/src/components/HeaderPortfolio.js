import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import './css/headerPortfolio.css'
import axios from 'axios'
import getCookie from './Cookie';


function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};


class HeaderPortfolio extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
            li1: "PORTFOLIO",
            li2: "FUND FINDER",
            li3: "PREFERENCES",
            currBal: undefined,
            baseCurr: undefined,
            fullName: undefined,
        }
    }

    componentDidMount(){
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else{
            axios.get('http://localhost:8762/portfolio', {headers : { Authorization: `Bearer ${jwt}` } })
            .then( res => {
                this.setState({
                    currBal: res.data.currBal,
                    baseCurr: res.data.baseCurr,
                    fullName: parseJwt(jwt).name
                })
                console.log(parseJwt(jwt));
            })
        }
    }

    logout(){
        document.cookie = "token=";
        this.props.history.push('/');
    }

    render() {
        var tabClass=["tab"];
        if(this.props.active){
            tabClass.push('active');
        }
        return (
            <nav>
                <div className="nav-wrapper teal lighten-1">
                    <a href="/" onClick={this.logout}className="right sign-out-icon">
                        <div className="separator"></div>
                        <i class="fa fa-sign-out" aria-hidden="true"></i>
                    </a>
                    <a href="#" className="right username">{this.state.fullName}</a>
                        <ul id="nav-mobile" className="left user-info">
                            <li><a>Total Assets : {this.props.totalAssets}</a></li>
                            <li><a>Total Balance : {this.state.currBal}</a></li>
                            <li><a>Base Currency : {this.state.baseCurr}</a></li>
                        </ul>
                        
                </div>
                <div className="nav-content">
                    {/* <div className="nav-wrapper teal lighten-1" text-align = "center"> */}
                            <ul id="nav-mobile" className="left tabs ">
                                <li className="tab active" onClick = {()=> this.props.tabHandler(0)}><a>{this.state.li1}</a></li>
                                <li className="tab" onClick = {()=> this.props.tabHandler(1)}><a>{this.state.li2}</a></li>
                                <li className="tab" onClick = {()=> this.props.tabHandler(2)}><a>{this.state.li3}</a></li>
                            </ul>
                        
                    {/* </div> */}
                </div>  
                
            </nav>
        )
    }
}

export default HeaderPortfolio
