import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css';
import Chips from 'react-chips'
import './css/addEntitlements.css'
import SearchBar from './SearchBar.js'
import axios from 'axios';
import getCookie from './Cookie';
import FileUpload from './FileUploadComponent';
import autocomplete from './utility/Autocomplete';
import Modal from 'react-responsive-modal';

class AddEntitlements extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             chips: [],
             fundSuggestions: [],
             userSuggestions: [],
             funds: [],
             users: [],
             updateUser: false,
             open: false,
             errorResponse: [],
             entitledUser: undefined,
             entitledToList: []
        }
    }

    onChipsChange = chips => {
        if(!this.state.updateUser){
            this.setState({ 
                chips: chips 
            });
        }else{
            var chip = [];
            if(chips.length > 0){
                chip.push(chips[chips.length-1])
            }
            this.setState({
                chips: chip
            })
            if(chip.length > 0) {
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
                    url: baseUrl + 'fund-handling/api/funds/entitlements?userId=' + chip[0],
                    headers: headers
                }).then(res => {
                    document.getElementById("added-funds").innerHTML = "";
                    res.data.forEach(fund =>{
                        var currentList = document.getElementById("added-funds");
                        var length = currentList.getElementsByTagName('li').length;
                        var li= document.createElement('li');
                        li.id = 'select' + length;
                        console.log(fund);
                        li.innerHTML = fund.fundNumber;
                        var i = document.createElement('i');
                        i.className = 'fa fa-trash';
                        li.append(i);
                        i.onclick = (event) => {
                            var elem = event.target.parentNode;
                            var parent = elem.parentNode;
                            parent.removeChild(elem);
                            var updateIdList = parent.getElementsByTagName("li")
                            for(var i=0; i<updateIdList.length; i++){
                                updateIdList[i].id = 'select' + i;
                            }
                        }
                        currentList.appendChild(li);
                    })
                }).catch(err => {
                    console.log(err)
                })
            }
        }
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

        // Send request for fetching all funds to set fund suggestions
        axios({
            method: 'get',
            url: baseUrl + 'fund-handling/api/funds/all',
            headers: headers
        }).then(response =>{
            console.log(response.data);
            var fundNumbers = [];
            response.data.forEach(fund => {
                fundNumbers.push(fund.fundNumber);
            });
            this.setState({
                funds: response.data,
                fundSuggestions: fundNumbers
            })
        }).catch(error =>{
            console.log(error);
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }else if (error.response.status === 500){
                this.setState({
                    errorResponse: [<p>The server is down. Please try again later.</p>]
                })
            }
        });

        // Send request to fetch all users to set user suggestions
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
    componentDidUpdate(){
        autocomplete(document.getElementById("searchId"), this.handleSearchItemClick, 
                        this.state.fundSuggestions, this.state.funds);
    }
    handleSearchItemClick(content){
        var ul = document.getElementById("selected-funds")
        var length = ul.getElementsByTagName("li").length;
        var li = document.createElement("li");
        li.id = 'select' + length;
        var i = document.createElement('i');
        i.className = 'fa fa-times';
        li.innerHTML = content;
        li.append(i);
        i.onclick = event => {
            var elem = event.target.parentNode;
            var parent = elem.parentNode;
            parent.removeChild(elem);
            var updateIdList = parent.getElementsByTagName("li")
            for(var i=0; i<updateIdList.length; i++){
                updateIdList[i].id = 'select' + i;
            }
        }
        ul.appendChild(li);
    }

    selectFunds(){
        var list = document.getElementById("selected-funds").getElementsByTagName("li")
        for(var it=0; it<list.length; it++){
            var currentList = document.getElementById("added-funds");
            var length = currentList.getElementsByTagName('li').length;
            var elem = list[it].innerText;
            // console.log(elem);
            elem = elem.split("<i")[0].trim();
            var li= document.createElement('li');
            li.id = 'select' + length;
            li.innerHTML = elem;
            var i = document.createElement('i');
            i.className = 'fa fa-trash';
            li.append(i);
            i.onclick = (event) => {
                var elem = event.target.parentNode;
                var parent = elem.parentNode;
                parent.removeChild(elem);
                var updateIdList = parent.getElementsByTagName("li")
                for(var i=0; i<updateIdList.length; i++){
                    updateIdList[i].id = 'select' + i;
                }
            }
            currentList.appendChild(li);
        }
        document.getElementById("selected-funds").innerHTML = "";
    }

    addEntitlementsToUsers= () =>{
        var baseUrl = "http://localhost:8762/";
        var token = getCookie('token');
        if(!token){
            this.props.history.push('/');
        }
        // Setting headers
        var headers ={
            Authorization: 'Bearer ' + token
        }

        // console.log(this.state.chips)
        var listOfFunds = document.getElementById("added-funds").getElementsByTagName('li');
        var entitledTo = [];
        for(var i=0; i< listOfFunds.length; i++){
            entitledTo.push(listOfFunds[i].innerText || listOfFunds[i].textContent);
        }

        for(var i=0; i< this.state.chips.length; i++){
            var payload ={
                userId: this.state.chips[i],
                entitledTo: entitledTo,
            };
            
            axios({
                method: 'post',
                url: baseUrl + 'fund-handling/api/entitlements/' + (this.state.updateUser? 'update': 'add'),
                headers: headers,
                data: payload
            // eslint-disable-next-line no-loop-func
            }).then(response =>{
                console.log()
                this.setState({
                    open: true,
                    entitledUser: payload.userId,
                    entitledToList: payload.entitledTo
                })
            }).catch(error =>{
                console.log(error);
                if(error.response.status === 401 || error.response.status === 403){
                    document.cookie = "token=;"
                    window.location = "/";
                }else{
                    alert(error.response.data)
                }
            });
        }

        // document.getElementById("added-funds").innerHTML = "";
    }
    
    onSwitchChanged = (event) =>{
        this.setState({
            updateUser: event.target.checked
        })
        this.setState({
            chips: []
        })
    }

    closeModalHandler = () => {
        this.setState({
            open: false
        })
    }

    closePopUp = () => {
        window.location="/admin";
    }

    render() {       
        return (
            <div className="wrapper">
                <div class="error-response">
                    {this.state.errorResponse}
                </div>
                <div className="user-search">
                        <label id = "search-text">Search users by user id</label>
                        <Chips value={this.state.chips} onChange={this.onChipsChange} 
                                suggestions={this.state.userSuggestions} >

                        </Chips>
                        <div className="switch">
                            <label>
                                Add Entitlements
                                <input type="checkbox" onChange = {this.onSwitchChanged}/>
                                <span className="lever"></span>
                                Update Entitlements
                            </label>
                        </div>
                </div>
                
                <div className="entitlements">
                    <div className="content-wrapper">
                        <div className="search-and-add">
                            <SearchBar suggestions={this.state.fundSuggestions} index="" finder={false}
                                searchTerm = "Search Fund Number"></SearchBar>
                            <div className="funds">
                                <ul id="selected-funds">
                                </ul>
                            </div>
                            <button className="btn waves-effect waves-light add-entitlements" onClick={this.selectFunds}>
                                Select funds
                            </button>
                        </div>
                        <div className="finalized-funds">
                            <ul id="added-funds">
                            </ul>
                            <button className="btn waves-effect waves-light add-entitlements" onClick={this.addEntitlementsToUsers}>
                                {this.state.updateUser? "Update Entitlements": "Add Entitlements"}
                            </button>
                        </div>
                    </div>    
                </div>
                <FileUpload endUrl='entitlements/addEntitlements' buttonText='Add Entitlements from File'></FileUpload>         
                <Modal classNames="modal" open ={this.state.open} onClose={this.closeModalHandler} center >
                    <div>
                        <h5 align="center"><b>{this.state.entitledUser}</b> has been entitled to the following funds</h5>
                        <ul>
                            {
                                this.state.entitledToList.map(entitled => 
                                    <li align="center"> {entitled} </li>
                                )
                            }
                        </ul>
                        <div align="center">
                            <button type="button" className="btn" onClick={this.closePopUp}>OK</button>
                        </div>
                    </div>
                </Modal>
            </div>
        )
    }
}

export default AddEntitlements
