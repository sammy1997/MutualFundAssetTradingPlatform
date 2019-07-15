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
             active: false,
             disabled: false,
             submitLoading: false,    
             verifyLoading: false
        }
    }

    componentWillMount(){
        this.props.onRef(this)
    }

    componentDidMount = () => {
        this.setState({
            verified: false,
            submitLoading: false,    
            verifyLoading: false,
            trades: this.props.trades
        }, () => {console.log(this.state.trades)})
        this.props.onRef(this)
    }

    componentWillUnmount() {
        this.props.onRef(undefined)
    }

    onCloseModal = () => {
        this.setState({ open: false });
    };

    onOpenModal = () => {
        this.setState({ open: true });
    };

    verifyHandler = () => {
        this.setState({
            verifyLoading: true,
        })
        var jwt = getCookie('token');
        if (!jwt) {
            this.props.history.push('/');
        } else {
            this.setState({
                trades: this.props.trades
                }, () => {
                    var getTrades = [...this.state.trades]
                    console.log("props.trade: " + this.props.trades);
                    var index = getTrades.indexOf(getTrades.find(o => o.quantity <= 0))
                    if (index!=-1){
                        alert(`Please enter valid quantity`)
                        console.log(index)
                    } else {
                    // console.log(jwt) 
                    console.log(this.state.trades)
                    this.props.numberOfTrades < 6 ? ( 
                    axios({
                        method: `POST`,
                        url: 'http://localhost:8762/trade/verify',
                        headers: {Authorization: `Bearer ${jwt}`}, 
                        data: getTrades 
                    }).then(Response => {
                        (Response.data === `Trades are verified`) ? (
                            this.setState({
                                verified: true  
                            })
                        ) : (alert(`${Response.data}`))
                        }).catch(error => {
                            if(error.response) 
                                if(error.response.status === 403 ){
                                    alert('You are not authorized to place any trades');
                                }else if(error.response.status === 400){
                                    alert("You are not entitled to any one (or more) of the fund (s)");
                                }
                        })
                    ) : alert(`Max Trades that can be placed is 5`)
                    }
                }
            )
        }
    }
    
    unVerifyHandler = () =>{
        this.setState({
            verified: false, 
            verifyLoading: false 
        })
    }

    noClickhandler = (e) => {
        e.preventDefault()
        this.setState({
            open: false 
        })
    }

    submitHandler = (event) => {
        event.preventDefault();     
        this.setState({
            disabled: true,
            submitLoading: true 
        }) 
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
        const {open, submitLoading, verifyLoading} = this.state  
        let submitContent;
        if (submitLoading) {
            submitContent = <div><p align="center">Placing requested Trade...</p>
            <div className="loader-container">
                <img className="loading" src={require('./loader2.gif')}/>
            </div>
        </div>
        } else {
            submitContent = <div><p align="center">Are you sure you want to place trades?</p><form align="center"><button className='submitTrade' onClick={this.submitHandler} disabled={this.state.disabled}>Yes</button> <button className='submitTrade' onClick={this.noClickhandler}>No</button></form></div>
        }
        
        return this.state.verified ? 
        (
            <div>
                <button className='verifyTrade' onClick={this.onOpenModal}>PLACE TRADES</button> 
                <p align='center'> Successfully Verified trades! </p>
                <Modal open={open} onClose={this.onCloseModal} center>
                    {submitContent} 
                </Modal>
            </div> )
        : ( <div>
                <button className='verifyTrade' onClick={this.verifyHandler}>VERIFY TRADES</button>
            </div> )
    }
}

export default VerifyButton