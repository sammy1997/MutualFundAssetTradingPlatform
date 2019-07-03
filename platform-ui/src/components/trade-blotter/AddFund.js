//component for adding fund

import React, { Component } from 'react'
import './tradeBlotter.css' 
import Modal from 'react-responsive-modal';

class AddFund extends Component {
    
    constructor(props) {
        super(props)
    
        this.state = {
            open: false,
            fundName: '',
            fundNumber: '',
            invManager: '',
            invCurr: '',
            setCycle: '',
            nav: '',
            sAndPRating: '',
            moodyRating: '',
            quantity: ''  
        }
    }
    
    onOpenModal = () => {
        this.props.numberOfTrades < 5 ? (
            this.setState({ open: true })) : alert("MAX TRADES THAT CAN BE PLACED IS 5")
    };

    onCloseModal = () => {
        this.setState({ open: false });
    };

    onSubmit = (event) => {
        event.preventDefault();
        console.log(this.state.fundName, this.state.fundNumber, this.state.invManager, this.state.invCurr,
            this.state.setCycle, this.state.nav, this.state.sAndPRating, this.state.moodyRating, this.state.quantity);
            
            this.props.addFund(this.state.fundName, this.state.fundNumber, this.state.invManager, this.state.invCurr, 
            this.state.setCycle, this.state.nav, this.state.sAndPRating, this.state.moodyRating, this.state.quantity)
        
        this.setState ({
            fundName: '',
            fundNumber:  '',
            invManager: 'GS',
            invCurr: 'INR',
            setCycle: '12',
            nav: '10',
            sAndPRating: '6',
            moodyRating: '9',
            quantity: '10'
        });
        this.onCloseModal() 
    }

    onChange = (event) => 
    this.setState({ 
        [event.target.name]: event.target.value
    });

    render() {
        const { open } = this.state;
        return (
            <div>
                <button className="add-fund-btn" onClick={this.onOpenModal}>+ Add Fund</button>

                <Modal open={open} onClose={this.onCloseModal} center>
                    <form onSubmit={this.onSubmit}>
                        <div>
                            <label>Fund Name</label>
                            <input id="fundName" type='text' name='fundName' value={this.state.fundName} onChange={this.onChange}/>
                        </div>
                        
                        <div>
                            <label>Fund Number</label>
                            <input id="fundNumber" type='text' name='fundNumber' value={this.state.fundNumber} onChange={this.onChange}/>
                        </div>
                        
                        <button className='fundSubmit' type='submit'>Submit</button>
                    </form>
                </Modal>
            </div>
        )
    }
}

export default AddFund