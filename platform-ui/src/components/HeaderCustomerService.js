import React, { Component } from 'react'
import './css/materialize.css'
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
                <div className="nav-wrapper deep-purple lighten-1">
                    <a href="x.html" className="right username">{this.state.user}</a>
                    <ul id="nav-mobile" className="left hide-on-med-and-down">
                        <li><a href="sass.html">{this.state.li1}</a></li>
                        <li><a href="badges.html">{this.state.li2}</a></li>
                        <li><a href="collapsible.html">{this.state.li3}</a></li>
                    </ul>
                </div>
            </nav>
        )
    }
}

export default Header
