import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import './css/header.css'
import M from 'materialize-css'

class Header extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             li1: "FUND FINDER",
             li2: "ADD FUND",
             li3: "ADD ENTITLEMENTS",
             li4: "ADD USER",
             li5: "ADD CURRENCY"
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
        M.updateTextFields();
        return (
            <nav>
                <div className="nav-wrapper">
                    <a href="/" onClick={this.logout}className="right sign-out-icon">
                        <div className="separator"></div>
                        <i class="fa fa-sign-out" aria-hidden="true"></i>
                    </a>
                    <a href="#" className="right username">{this.props.name}</a>

                    <ul id="nav-mobile" className="left hide-on-med-and-down">
                        {/* <li onClick = {()=> this.props.tabHandler(0)}><a>{this.state.li1}</a></li>
                        <li onClick = {()=> this.props.tabHandler(1)}><a>{this.state.li2}</a></li>
                        <li onClick = {()=> this.props.tabHandler(2)}><a>{this.state.li3}</a></li> */}
                        <li className="tab" onClick = {()=> this.props.tabHandler(0)}><a>{this.state.li1}</a></li>
                        <li className="tab active" onClick = {()=> this.props.tabHandler(1)}><a>{this.state.li2}</a></li>
                        <li className="tab" onClick = {()=> this.props.tabHandler(2)}><a>{this.state.li3}</a></li>
                        <li className="tab" onClick = {()=> this.props.tabHandler(3)}><a>{this.state.li4}</a></li>
                        <li className="tab" onClick = {()=> this.props.tabHandler(4)}><a>{this.state.li5}</a></li>
                    </ul>
                </div>
            </nav>
        )
    }
}

export default Header
