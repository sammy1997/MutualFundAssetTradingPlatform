import React , { Component } from 'react';
import { withRouter } from 'react-router-dom';
import getCookie from './Cookie';


class AuthComponent extends Component {
    constructor(props){
        super(props);

        this.state = {
            user: undefined
        };
    }

    componentDidMount(){
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else{
            this.setState({
                user: "hey"
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