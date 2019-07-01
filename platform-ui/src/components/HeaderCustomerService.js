import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import './css/header.css'

class Header extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             li1: "FUND FINDER",
             li2: "ADD FUND",
             li3: "ADD ENTITLEMENTS",
             user: "Sammy"
        }
    }
    
    render() {
        return (
            <nav>
                <div className="nav-wrapper">
                    <a href="x.html" className="right username">{this.state.user}</a>
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

export default Header
