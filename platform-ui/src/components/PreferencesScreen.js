import React, { Component } from 'react';
import 'materialize-css/dist/css/materialize.min.css';
import Preferences from './Preferences';
import Header from './HeaderCustomerService';

class PreferencesScreen extends Component{
    constructor(props){
        super(props);
        this.state={
            userId:'',
            fullName:'',
            preferencesScreen:[]
        }
    }

    componentDidMount(){
        var preferencesScreen = [];
        preferencesScreen.push(
        <Header key="1" />,
        <Preferences key="2"/>
        );
        this.setState({
            preferencesScreen:preferencesScreen
        })
    }

    render(){
        return(
            <div className="preferencesScreen">
                {this.state.preferencesScreen}
            </div>
        );
    }
}


export default PreferencesScreen;