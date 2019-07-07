import React, { Component } from 'react'
import './css/searchBar.css'


class SearchBar extends Component {
    render() {
        var searchBarStyle={
            height: '1.5rem'
        }
        return (
            <div className="wrap">
                <div className="search autocomplete">
                    <input type="text" style={searchBarStyle} id={"searchId" + this.props.index} 
                        className="searchTerm" placeholder={this.props.searchTerm}
                        onKeyUp={() => {
                                if(this.props.finder){
                                    this.props.searchHandler('searchId', this.props.tableId, this.props.searchableFields)
                                }
                            }
                        }/>
                    <button type="submit" className="searchButton">
                        <i className="fa fa-search"></i>
                    </button>
                </div>
            </div>
        )
    }
}

export default SearchBar
