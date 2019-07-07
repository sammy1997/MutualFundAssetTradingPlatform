import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import './css/header.css'
import getCookie from './Cookie'
import axios from 'axios'
import M from 'materialize-css'

class Header extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             li1: "FUND FINDER",
             li2: "ADD FUND",
             li3: "ADD ENTITLEMENTS"
        }
    }


    
    render() {
        M.updateTextFields();
        return (
            <nav>
                <div className="nav-wrapper">
                    <a className="right username">{this.props.name}</a>
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
