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
        
    }

    render(){
        return(
            <div className="preferencesScreen">
                
            </div>
        );
    }
}


export default PreferencesScreen;