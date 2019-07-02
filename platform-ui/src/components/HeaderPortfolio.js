import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import './css/headerPortfolio.css'

class HeaderPortfolio extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             li1: "PORTFOLIO",
             li2: "FUND FINDER",
             li3: "PREFERENCES",
             assets: 25000,
             balance: 15000,
             baseCurr: "INR",
             user: "Shikhar"
        }
    }

    render() {
        return (
            
            <nav>
                <div className="nav-wrapper teal lighten-1">
                    <a href="#" className="right username">{this.state.user}</a>
                        <ul id="nav-mobile" className="left hide-on-med-and-down">
                            <li><a>Total Assets : {this.state.assets}</a></li>
                            <li><a>Total Balance : {this.state.balance}</a></li>
                            <li><a>Base Currency : {this.state.baseCurr}</a></li>
                           
                        </ul>
                        
                </div>
                <div className="nav-wrapper teal lighten-1" text-align = "center">
                        <ul id="nav-mobile" className="left hide-on-med-and-down">
                            <li onClick = {()=> this.props.tabHandler(0)}><a>{this.state.li1}</a></li>
                            <li onClick = {()=> this.props.tabHandler(1)}><a>{this.state.li2}</a></li>
                            <li onClick = {()=> this.props.tabHandler(2)}><a>{this.state.li3}</a></li>
                        </ul>
                      
                </div>
                
            </nav>
        )
    }
}

export default HeaderPortfolio
