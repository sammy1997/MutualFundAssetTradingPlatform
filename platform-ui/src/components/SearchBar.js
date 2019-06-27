import React, { Component } from 'react'
import './css/searchBar.css'


class SearchBar extends Component {
    render() {
        return (
            <div className="wrap">
                <div className="search">
                    <input type="text" id="searchId" className="searchTerm" placeholder={this.props.searchTerm}/>
                    <button type="submit" className="searchButton">
                        <i className="fa fa-search"></i>
                    </button>
                </div>
            </div>
        )
    }
}

export default SearchBar
