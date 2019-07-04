// component button for verifying 
import React, { Component } from 'react'
import Modal from 'react-responsive-modal';
import axios from 'axios'
import './css/tradeBlotter.css'

class VerifyButton extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             numberOfFunds: 0,
             verified: false, 
             open: false 
        }
    }

    onCloseModal = () => {
        this.setState({ open: false });
    };

    onOpenModal = () => {
        this.setState({ open: true });
    };

    verifyHandler = () => {
        this.props.numberOfTrades < 6 ? this.setState({
            verified: true 
        }) :    alert (`Max trades to place is only 5`) 

        //axios post request to check for verification
        axios.post('http://localhost:8762/trade/verify', {headers: {}, body: this.props.funds})
        .then(Response => {
            Response.equals(`Verified Trades`) ? (
            this.setState({
                verified: true  
            })
        ) : (console.log("Not verified"))
        }).catch(error => console.log(error))
    }

    submitHandler = (e) => {
        e.preventDefault();
        this.setState({
            open: false
        })
        
        //axios post request to place trades from trade blotter
        this.state.verified === true ? (
            axios.post(`http://localhost:8762/trade/exchange`, {headers: {}, body: this.props.funds})
            .then(Response => {
                Response.status === 201 ? console.log(`Exchanged trades`) : console.log(`Error occurred`)
            })) : (
                console.log(`Not verified`)
            ) 
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
                            <button className='submitTrade' type='submit'>No</button>
                        </form>
                    </div>
                </Modal>
            </div> )
        : ( <div><button className='verifyTrade' onClick={this.verifyHandler}>VERIFY TRADES</button> </div> )
    }
}

export default VerifyButton