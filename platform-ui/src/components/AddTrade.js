//component for adding fund

import React, { Component } from 'react'
// import './tradeBlotter.css' 
import Modal from 'react-responsive-modal';
import './css/tradeBlotter.css'
import axios from 'axios';
import getCookie from './Cookie';

class AddTrade extends Component {
    
    constructor(props) {
        super(props)
    
        this.state = {
            open: false,
            list: [],
            suggestions: [],
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
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else{
            axios.get('http://localhost:8762/fund-handling/api/entitlements/get', {headers : { Authorization: `Bearer ${jwt}` } })
            .then(res => {
                if(res.status == 200){
                    console.log(res.data);
                    this.setState({
                        list : res.data
                    })
                    var temp = [];
                    for(var i =0; i< this.state.list.length; i++){
                        temp.push(this.state.list[i].fundNumber);
                    }
                    this.setState({
                        suggestions: temp
                    })
                    console.log(this.state.suggestions);
                    this.autocomplete(document.getElementById("fundNumber"), this.state.suggestions, this.onChange);
                }
            }).catch( error => {
                console.log(error);
            });
        }
        this.props.numberOfTrades < 5 ? (
            this.setState({ open: true })) : alert("MAX TRADES THAT CAN BE PLACED IS 5")
    };

    onCloseModal = () => {
        this.setState({ open: false });
    };

    onSubmit = (event) => {
        event.preventDefault();
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else{
        // console.log(this.state.fundName, this.state.fundNumber, this.state.invManager, this.state.invCurr,
        //     this.state.setCycle, this.state.nav, this.state.sAndPRating, this.state.moodyRating, this.state.quantity);
            axios.get('http://localhost:8762/fund-handling/api/entitlements/get/fund?fundNumber=' + this.state.fundNumber, 
                {headers : { Authorization: `Bearer ${jwt}` } })
            .then(res => {
                this.props.addTrade(res.data.fundName, res.data.fundNumber, res.data.invManager)
                // this.props.unverify()
            })
        }
        this.onCloseModal() 
    }

    autocomplete(inp, arr, onChange) {
        /*the autocomplete function takes two arguments,
        the text field element and an array of possible autocompleted values:*/
        var currentFocus;
        /*execute a function when someone writes in the text field:*/
        inp.addEventListener("input", function(e) {
            var a, b, i, val = this.value;
            /*close any already open lists of autocompleted values*/
            closeAllLists();
            if (!val) { return false;}
            currentFocus = -1;
            /*create a DIV element that will contain the items (values):*/
            a = document.createElement("DIV");
            a.setAttribute("id", this.id + "autocomplete-list");
            a.setAttribute("class", "autocomplete-items");
            /*append the DIV element as a child of the autocomplete container:*/
            this.parentNode.appendChild(a);
            /*for each item in the array...*/
            for (i = 0; i < arr.length; i++) {
              /*check if the item starts with the same letters as the text field value:*/
              if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                /*create a DIV element for each matching element:*/
                b = document.createElement("DIV");
                /*make the matching letters bold:*/
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
                /*insert a input field that will hold the current array item's value:*/
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                /*execute a function when someone clicks on the item value (DIV element):*/
                    b.addEventListener("click", function(e) {
                    inp.value = this.getElementsByTagName("input")[0].value;
                    onChange();
                    closeAllLists();
                });
                a.appendChild(b);
              }
            }
        });
        
        inp.addEventListener("keydown", function(e) {
            var x = document.getElementById(this.id + "autocomplete-list");
            if (x) x = x.getElementsByTagName("div");
            if (e.keyCode == 40) {
              currentFocus++;
              addActive(x);
            } else if (e.keyCode == 38) { //up
              currentFocus--;
              addActive(x);
            } else if (e.keyCode == 13) {
              e.preventDefault();
              if (currentFocus > -1) {
                if (x) x[currentFocus].click();
              }
            }
        });
        function addActive(x) {
          if (!x) return false;
          removeActive(x);
          if (currentFocus >= x.length) currentFocus = 0;
          if (currentFocus < 0) currentFocus = (x.length - 1);
          x[currentFocus].classList.add("autocomplete-active");
        }
        function removeActive(x) {
          for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
          }
        }
        function closeAllLists(elmnt) {
          var x = document.getElementsByClassName("autocomplete-items");
          for (var i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != inp) {
            x[i].parentNode.removeChild(x[i]);
          }
        }
      }
      document.addEventListener("click", function (e) {
          closeAllLists(e.target);
      });
      }

    onChange = () => 
    this.setState({ 
        fundNumber: document.getElementById("fundNumber").value
    });

    render() {
        const { open } = this.state;
        return (
            <div>
                <button className="add-fund-btn" onClick={this.onOpenModal}>+ Add Fund</button>

                <Modal open={open} onClose={this.onCloseModal} center>
                    <form onSubmit={this.onSubmit}>
                        {/* <div>
                            <label>Fund Name</label>
                            <input id="fundName" type='text' name='fundName' value={this.state.fundName} onChange={this.onChange}/>
                        </div> */}
                        
                        <div>
                            <label>Fund Number</label>
                            <input autoComplete="off" id="fundNumber" type='text' name='fundNumber' value={this.state.fundNumber} onChange={this.onChange}/>
                        </div>
                        
                        <button className='fundSubmit' type='submit'>Submit</button>
                    </form>
                </Modal>
            </div>
        )
    }
}

export default AddTrade