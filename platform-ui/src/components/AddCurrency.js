import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import M from 'materialize-css/dist/js/materialize.min.js'
import axios from 'axios';
import getCookie from './Cookie';
import { withRouter } from 'react-router-dom';


class AddCurrency extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
            update: false,
            currencies: [],
            currency: "",
            rate: 0.0
        }
    }

    onSwitchChanged = (event) =>{
        this.setState({
            update: event.target.checked
        })
    }
    
    onChange = (event) =>{
        this.setState({
            [event.target.id]: parseFloat(event.target.value)
        })
    }

    componentDidMount(){
        var baseUrl = "http://localhost:8762/";
        var token = getCookie('token');
        if(!token){
            this.props.history.push('/');
        }
        // Setting headers
        var headers ={
            Authorization: 'Bearer ' + token
        }

        axios({
            method: 'get',
            url: baseUrl + 'trade/currency/all',
            headers: headers
        }).then(response =>{
            console.log(response.data);
            if(Array.isArray(response.data)){
                this.setState({
                    currencies: response.data
                })
            }
        }).catch(error =>{
            console.log(error);
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }
        });
    }

    autocomplete(inp, handleSearchItemClick, arr) {
        var currentFocus;
        inp.addEventListener("input", function(e) {
            var a, b, i, val = this.value;
            closeAllLists();
            if (!val) { return false;}
            currentFocus = -1;
            a = document.createElement("DIV");
            a.setAttribute("id", this.id + "autocomplete-list");
            a.setAttribute("class", "autocomplete-items");
            this.parentNode.appendChild(a);
            for (i = 0; i < arr.length; i++) {
              if (arr[i].currency.toUpperCase().indexOf(val.toUpperCase())!==-1) {
                b = document.createElement("DIV");
                b.innerHTML = "<strong>" + arr[i].currency.substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].currency.substr(val.length);
                b.innerHTML += "<input type='hidden' value='" + arr[i].currency + "'>";
                    b.addEventListener("click", function(e) {
                        handleSearchItemClick(this.getElementsByTagName("input")[0].value);
                        closeAllLists();
                    });
                a.appendChild(b);
              }
            }
        });
        
        inp.addEventListener("keydown", function(e) {
            var x = document.getElementById(this.id + "autocomplete-list");
            if (x) x = x.getElementsByTagName("div");
            if (e.keyCode === 40) {
              currentFocus++;
              addActive(x);
            } else if (e.keyCode === 38) {
              currentFocus--;
              addActive(x);
            } else if (e.keyCode === 13) {
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
                if (elmnt !== x[i] && elmnt !== inp) {
                    x[i].parentNode.removeChild(x[i]);
                }
            }
        }
        document.addEventListener("click", function (e) {
            closeAllLists(e.target);
        });
    }

    addCurrencyRequest = (e) =>{
        e.preventDefault();
        this.setState({
            currency: document.getElementById("currency").value
        }, () =>{
            var token = getCookie('token');
            if(!token){
                this.props.history.push('/');
            }
            // Setting headers
            var headers ={
                Authorization: 'Bearer ' + token
            }

            console.log(this.state);
            axios({
                method: 'post',
                url: "http://localhost:8762/trade/currency/" + (this.state.update? 'update': 'add'),
                headers: headers,
                data: this.state
            }).then(response =>{
                alert(response.data);
            }).catch(error =>{
                console.log(error);
                if(error.response.status === 401 || error.response.status === 403){
                    document.cookie = "token=;"
                    window.location = "/";
                }
                if(error.response.status === 400){
                    alert("Some of the fields might be missing");
                }
                if(error.response.status === 404){
                    alert("Currenct not found");
                }
            });

        })
    }

    handleSearchItemClick = currency =>{
        document.getElementById('currency').value = currency;
        for(var i=0; i< this.state.currencies.length; i++){
            var currencyObj = this.state.currencies[i];
            if(currency === currencyObj.currency){
                this.setState({
                    currency: currency,
                    rate: currencyObj.rate
                })
                break;
            }
        }
    }

    componentDidUpdate(){
        this.autocomplete(document.getElementById("currency"), this.handleSearchItemClick, 
        this.state.currencies)
    }
    
    render() {
        M.updateTextFields();
        return (
            <div className="form-card">
                <div className="row">
                    <form className="col s12">
                        <div className="row">
                            <div className="switch">
                                <label>
                                    Add currency
                                    <input type="checkbox" onChange = {this.onSwitchChanged}/>
                                    <span className="lever"></span>
                                    Update Currency
                                </label>
                            </div>
                        </div>
                        <div className="row">
                            <div className="input-field col s12 autocomplete">
                                <input id="currency" type="text" className="validate" autoComplete="off"/>
                                <label htmlFor="currency">Currency name</label>
                            </div>
                        </div>
                        <div className="row">
                            <div className="input-field col s12">
                                <input id="rate" value={this.state.rate} type="number" 
                                    onChange={this.onChange} className="validate"/>
                                <label htmlFor="rate">Value against Dollar</label>
                            </div>
                        </div>
                        <button className="btn waves-effect waves-light" type="submit" 
                            onClick={this.addCurrencyRequest}>Submit</button>
                    </form>
                </div>        
            </div>
        )
    }
}

export default withRouter(AddCurrency)
