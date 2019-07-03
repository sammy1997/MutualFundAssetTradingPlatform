import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css';
import Chips from 'react-chips'
import './css/addEntitlements.css'
import SearchBar from './SearchBar.js'
import axios from 'axios';
import getCookie from './Cookie';
// import 'materialize-css/dist/js/materialize.min.js'

class AddEntitlements extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             chips: [],
             fundSuggestions: [],
             userSuggestions: [],
             funds: [],
             users: []
        }
    }

    onChipsChange = chips => {
        this.setState({ 
            chips: chips 
        });
    }

    autocomplete(inp, handleSearchItemClick, arr, funds) {
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
                    funds[i].fundName.toUpperCase().indexOf(val.toUpperCase())!==-1) {
                b = document.createElement("DIV");
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length) + " : " + funds[i].fundName;
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
            } else if (e.keyCode === 38) { //up
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
        this.autocomplete(document.getElementById("searchId"), this.handleSearchItemClick, 
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
            // console.log(event.target.parentNode.id)
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

        console.log(this.state.chips)
        var listOfFunds = document.getElementById("added-funds").getElementsByTagName('li');
        var entitledTo = [];
        for(var i=0; i< listOfFunds.length; i++){
            entitledTo.push(listOfFunds[i].innerText || listOfFunds[i].textContent);
        }
        var count = 0;
        for(var i=0; i< this.state.chips.length; i++){
            var payload ={
                userId: this.state.chips[i],
                entitledTo: entitledTo
            };
            
            axios({
                method: 'post',
                url: baseUrl + 'fund-handling/api/entitlements/add',
                headers: headers,
                data: payload
            }).then(response =>{
                console.log(response.data)
            }).catch(error =>{
                console.log(error);
                if(error.response.status === 401 || error.response.status === 403){
                    document.cookie = "token=;"
                    window.location = "/";
                }else{
                    count = 1;
                    alert(error.response.data)
                }
            });
        }
        if(count === 0){
            alert("All additions successful");
        }else{
            alert("Some additions faced errors");
        }
        document.getElementById("added-funds").innerHTML = "";
    }
    
    render() {
        return (
            <div className="wrapper">
                <div className="user-search">
                        <label>Search users by user id</label>
                        <Chips value={this.state.chips} onChange={this.onChipsChange} 
                                suggestions={this.state.userSuggestions} >

                        </Chips>
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
                                Add Entitlements
                            </button>
                        </div>
                    </div>    
                </div>           
            </div>
        )
    }
}

export default AddEntitlements
