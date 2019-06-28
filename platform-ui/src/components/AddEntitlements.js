import React, { Component } from 'react'
import 'materialize-css/dist/css/materialize.min.css';
import Chips, { Chip } from 'react-chips'
import './css/addEntitlements.css'
import SearchBar from './SearchBar.js'
// import 'materialize-css/dist/js/materialize.min.js'

class AddEntitlements extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
             chips: [],
             suggestions: ["harsh123", "kp7"]
        }
    }

    onChipsChange = chips => {
        this.setState({ 
            chips: chips 
        });
    }

    autocomplete(inp, arr, handleSearchItemClick) {
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
              if (arr[i].substr(0, val.length).toUpperCase() === val.toUpperCase()) {
                b = document.createElement("DIV");
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
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
        this.autocomplete(document.getElementById("searchId"), this.state.suggestions, this.handleSearchItemClick)
    }

    handleSearchItemClick(content){
        var ul = document.getElementById("selected-funds")
        ul.innerHTML+="<li>" + content +"<i class='fa fa-times' onclick='alert(1)'></i></li>";
    }
    
    render() {
        return (
            <div className="wrapper">
                <div className="user-search">
                        <label>Search users by name or user id</label>
                        <Chips value={this.state.chips} onChange={this.onChipsChange} 
                                suggestions={this.state.suggestions} >

                        </Chips>
                </div>
                
                <div className="entitlements">
                    <div className="content-wrapper">
                        <div className="search-and-add">
                            <SearchBar suggestions={this.state.suggestions} searchTerm = "Search Fund Number"></SearchBar>
                            <div className="funds">
                                <ul id="selected-funds">
                                </ul>
                            </div>
                            <button className="btn waves-effect waves-light" id="csv-add" name="action">Select funds
                            </button>
                        </div>
                        <div className="finalized-funds"></div>
                    </div>    
                </div>           
            </div>
        )
    }
}

export default AddEntitlements
