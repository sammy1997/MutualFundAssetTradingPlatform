import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css'
import M from 'materialize-css/dist/js/materialize.min.js'
import axios from 'axios';
import getCookie from './Cookie';
import { withRouter } from 'react-router-dom';
import Modal from 'react-responsive-modal';

class AddUser extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             update: false,
             userId: null,
             password: "",
             fullName: null,
             baseCurr: null,
             role: null,
             currBal: 0,
             users: [],
             userSuggestions: [],
             open: false
        }
    }

    closeModalHandler = () => {
        this.setState({
            open: false
        })
    }

    closePopUp = () => {
        window.location="/admin";
    }

    autocomplete(inp, handleSearchItemClick, arr, users) {
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
              if (arr[i].toUpperCase().indexOf(val.toUpperCase())!==-1 || 
                    users[i].fullName.toUpperCase().indexOf(val.toUpperCase())!==-1) {
                b = document.createElement("DIV");
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length) + " : " + users[i].fullName;
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
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

    onChange = (event) =>{
        this.setState({
            [event.target.id]: event.target.value
        })
        if(event.target.id === "currBal"){
            this.setState({
                currBal: parseFloat(event.target.value)
            })
        }
    }

    onSwitchChanged = (event) =>{
        if(event.target.checked){
            document.getElementById("balance").style="display: none";
            document.getElementById("currency").style="display: none";
        }else{
            document.getElementById("currency").style="display: block";
            document.getElementById("balance").style="display: block";
        }
        this.setState({
            update: event.target.checked
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
            url: baseUrl + 'create/list-all',
            headers: headers
        }).then(response =>{
            console.log(response.data);
            var userIds = [];
            response.data.forEach(user => {
                userIds.push(user.userId);
            });
            this.setState({
                users: response.data,
                userSuggestions: userIds
            })
        }).catch(error =>{
            console.log(error);
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }
        });
    }

    handleSearchItemClick = userId =>{
        document.getElementById('userId').value = userId;
        for(var i=0; i< this.state.users.length; i++){
            var user = this.state.users[i];
            if(userId === user.userId){
                this.setState({
                    userId: user.userId,
                    password: "",
                    baseCurr: user.baseCurr,
                    fullName: user.fullName,
                    role: user.role
                })
                break;
            }
        }
    }

    addUserRequest = (e) =>{
        e.preventDefault();
        if(this.state.baseCurr === null || this.state.baseCurr === undefined){
            this.setState({
                baseCurr: "INR"
            })
        }
        this.setState({
            userId: document.getElementById("userId").value
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
                url: "http://localhost:8762/create/" + (this.state.update? 'update': ''),
                headers: headers,
                data: this.state
            }).then(
                this.setState({
                    open: true
                })
            ).catch(error =>{
                console.log(error);
                if(error.response.status === 401 || error.response.status === 403){
                    document.cookie = "token=;"
                    window.location = "/";
                }
                if(error.response.status === 400){
                    alert("Some of the fields might be missing");
                }
                if(error.response.status === 404){
                    alert("User not found");
                }
            });

        })
    }
    
    componentDidUpdate(){
        this.autocomplete(document.getElementById("userId"), this.handleSearchItemClick, 
        this.state.userSuggestions, this.state.users)
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
                                    Add user
                                    <input type="checkbox" onChange = {this.onSwitchChanged}/>
                                    <span className="lever"></span>
                                    Update user
                                </label>
                            </div>
                        </div>
                        <div className="row">
                            <div className="input-field col s12 autocomplete">
                                <input id="userId" type="text" className="validate" autoComplete="off"/>
                                <label htmlFor="userId">User ID</label>
                            </div>
                        </div>
                        <div className="row">
                            <div className="input-field col s12">
                                <input id="fullName" value={this.state.fullName} type="text" 
                                    onChange={this.onChange} className="validate"/>
                                <label htmlFor="fullName">Name</label>
                            </div>
                        </div>
                        <div className="row">
                            <div className="input-field col s12">
                                <input id="password" value={this.state.password} type="password" 
                                    onChange={this.onChange} className="validate"/>
                                <label htmlFor="password">Password</label>
                            </div>
                        </div>
                        <div className="row">
                            <div className="input-field col s12">
                                <select id="role" defaultValue="" onChange={this.onChange}>
                                    <option value="" disabled>Select Role</option>
                                    <option value="ROLE_ADMIN">Admin</option>
                                    <option value="ROLE_TRADER">Trader</option>
                                    <option value="ROLE_VIEWER">Viewer</option>
                                </select>
                            </div>
                        </div>
                        
                        <div className="row" id= "balance">
                            <div className="input-field col s12">
                                <input id="currBal" value={this.state.currBal} type="number" 
                                    onChange={this.onChange} className="validate"/>
                                <label htmlFor="currBal">Balance</label>
                            </div>
                        </div>
                        <div className="row" id= "currency">
                            <div className="input-field col s12">
                                <select id="baseCurr" defaultValue="" onChange={this.onChange}>
                                        <option value="" disabled>Base Currency</option>
                                        <option value="INR">INR</option>
                                        <option value="USD">USD</option>
                                        <option value="EUR">EUR</option>
                                        <option value="AED">AED</option>
                                        <option value="GBP">GBP</option>
                                        <option value="SAR">SAR</option>
                                        <option value="JPY">JPY</option>
                                </select>
                            </div>
                        </div>
                        <button className="btn waves-effect waves-light" type="submit" 
                            onClick={this.addUserRequest}>Submit</button>
                    </form>
                    <Modal classNames="modal" open ={this.state.open} onClose={this.closeModalHandler} center >
                    <div>
                        {this.state.update ? <h5 align="center"><b>{this.state.userId}</b> has been updated</h5> : <h5 align="center"><b>{this.state.userId}</b> created</h5>}
                        <div align="center">
                            <button type="button" className="btn" onClick={this.closePopUp}>OK</button>
                        </div>
                    </div>
                </Modal>
                </div>        
            </div>
        )
    }
}

export default withRouter(AddUser)
