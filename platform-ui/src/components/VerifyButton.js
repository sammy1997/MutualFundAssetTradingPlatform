// component button for verifying 
import React, { Component } from 'react'
import Modal from 'react-responsive-modal';
import axios from 'axios'
import './css/tradeBlotter.css'
import getCookie from './Cookie';
import { Resolver } from 'dns';
import LoadingOverlay from 'react-loading-overlay'
import Loader from './Loader';

class VerifyButton extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             numberOfFunds: 0,
             verified: false, 
             open: false,
             trades: [], 
             active: true  
        }
    }


    componentDidMount = () => {
        this.setState({
            active: false  
        })
    }

    onCloseModal = () => {
        this.setState({ open: false });
    };

    onOpenModal = () => {
        this.setState({ open: true });
    };

    verifyHandler = () => {
        this.setState({
            active: true 
        })
        var jwt = getCookie('token');
        if (!jwt) {
            this.props.history.push('/');
        } else {
            this.setState({
            trades: this.props.trades
        }, () => {
            var getTrades = [...this.state.trades] 
            // console.log(jwt) 
            console.log(this.state.trades)
            this.props.numberOfTrades < 6 ? ( 
                axios({
                    method: `POST`,
                    url: 'http://localhost:8762/trade/verify',
                    headers: {Authorization: `Bearer ${jwt}`}, 
                    data: getTrades 
                })
                .then(Response => {
                    console.log(Response);
                    (Response.data === `Verified Trades`) ? (
                    this.setState({
                        verified: true  
                    })
                    ) : (console.log("Not verified"))
                })
                .catch(error => {
                    console.log(error)
                    alert(`Trades not verified, please check again`)
                })
            ) : alert(`Max Trades that can be placed is 5`)
            })
        }
        
    }
    
    noClickhandler = () => {
        this.setState({
            open: false 
        })
    }

    submitHandler = (event) => {
        event.preventDefault();      
        var jwt = getCookie('token')
        if(!jwt){
            this.props.history.push('/');
        } else {
            var getTrades = [...this.state.trades]
            this.state.verified === true ? (
                axios({
                    method: `POST`,
                    url: 'http://localhost:8762/trade/exchange',
                    headers: {Authorization: `Bearer ${jwt}`}, 
                    data: getTrades 
                })
                .then(response => {
                    this.setState({
                        active: true 
                    })
                    if (response.status === 201) {
                        window.location = "/portfolio"; 
                        console.log(`Exchanged trades`) 
                     } else console.log(`Error occurred`)
                })) : (
                    console.log(`Not verified`)
                )   
        
        }
         
    }

    render() { 
        const {open} = this.state 
        return this.state.verified ? 
        (
            <div>
                <button className='verifyTrade' onClick={this.onOpenModal}>PLACE TRADES</button> 
                <p align='center'> Successfully Verified trades! </p>
                <Modal open={open} onClose={this.onCloseModal} center>
                    <div>
                        <p align="center">Are you sure you want to place trades?</p>
                        <form onSubmit={this.submitHandler}>
                            <button className='submitTrade' type="submit">Yes</button> 
                            <button className='submitTrade' onClick={this.noClickhandler}>No</button>
                        </form>
                    </div>
                </Modal>
            </div> )
        : ( <div>
                    <button className='verifyTrade' onClick={this.verifyHandler}>VERIFY TRADES</button> 
            </div> )
    }
}

export default VerifyButton