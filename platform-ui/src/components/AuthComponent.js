import React , { Component } from 'react';
import axios from 'axios';
import { withRouter } from 'react-router-dom';
import getCookie from './Cookie';


function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};



class AuthComponent extends Component {
    constructor(props){
        super(props);

        this.state = {
            user: undefined
        };
    }

    componentDidMount(){
        var jwt = getCookie('token');
        // console.log(jwt);
        if(!jwt){
            this.props.history.push('/');
        }else{
            axios.get('http://localhost:8762/portfolio', {headers : { Authorization: `Bearer ${jwt}` } })
            .then( res => {
                this.setState({
                    user : res.data
                })
                // console.log(parseJwt(jwt).authorities[0]);
                // console.log(this.state.user);
            }).catch( err => {
                document.cookie = "";
                this.props.history.push('/');
            });
        }
    }

    render(){
        if(this.state.user === undefined){
            return(
                <div>
                    <h1>Loading....</h1>
                </div>
            )
        }

        return(
            <div>
                {this.props.children}
            </div>
        )
    }
}

export default withRouter(AuthComponent);