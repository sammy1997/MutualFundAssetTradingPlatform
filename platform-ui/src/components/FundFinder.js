import React, { Component } from 'react'
import './css/fundFinder.css'
import SearchBar from './SearchBar';
import axios from 'axios';
import getCookie from './Cookie';
import searchContent from './utility/SearchTableContent';


class FundFinder extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
            searchableFields: [0,1,2,5],
            fundList: [],
            errorResponse: []
        }
    }

    componentDidMount(){
        var baseUrl = "http://localhost:8762/fund-handling/api/funds/";
        var token =getCookie('token');
        if(!token){
            this.props.history.push('/');
        }
        var headers ={
            Authorization: 'Bearer ' + token
        }
        axios({
            method: 'get',
            url: baseUrl + 'all',
            headers: headers
        }).then(response =>{
            this.setState({
                fundList: response.data
            })
        }).catch(error =>{
            // console.log(error);
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }else if (error.response.status === 500){
                this.setState({
                    errorResponse: [<p>The server is down. Please try again later.</p>]
                });
            }
        });
    }
    
    render() {
        return (
            <div className="table-wrapper">
                <div class="error-response">
                    {this.state.errorResponse}
                </div>
                <table id='all-funds'>
                    <thead>
                        <tr>
                            <th scope="col">Fund Number</th>
                            <th scope="col">Name</th>
                            <th scope="col">Inv. Manager</th>
                            <th scope="col">Set. Cycle</th>
                            <th scope="col">Nav</th>
                            <th scope="col">Currency</th>
                            <th scope="col">S and P Rating</th>
                            <th scope="col">Moody's Rating</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th scope="row">
                                <SearchBar index={0} searchHandler={searchContent} tableId='all-funds' finder={true}
                                    searchableFields={this.state.searchableFields} searchTerm = "Search Fund Number">
                                </SearchBar>
                            </th>
                            <th scope="row">
                                <SearchBar index={1} searchHandler={searchContent} tableId='all-funds' finder={true}
                                    searchableFields={this.state.searchableFields} searchTerm = "Search Fund Name"></SearchBar>
                            </th>
                            <th scope="row">
                                <SearchBar index={2} searchHandler={searchContent} tableId='all-funds' finder={true}
                                    searchableFields={this.state.searchableFields} searchTerm = "Search Manager"></SearchBar>
                            </th>
                            <td>-N/A-</td>
                            <td>-N/A-</td>
                            <th scope="row">
                                <SearchBar index={3} searchHandler={searchContent} tableId='all-funds' finder={true}
                                    searchableFields={this.state.searchableFields} searchTerm = "Search Currency"></SearchBar>
                            </th>
                            <td>-N/A-</td>
                            <td>-N/A-</td>
                        </tr>
                        
                        {
                            this.state.fundList.map(item => 
                                <tr key={item.fundNumber}>
                                    <td>{item.fundNumber}</td>
                                    <td>{item.fundName}</td>
                                    <td>{item.invManager}</td>
                                    <td>{item.setCycle}</td>
                                    <td>{item.nav}</td>
                                    <td>{item.invCurrency}</td>
                                    <td>{item.sAndPRating}</td>
                                    <td>{item.moodysRating}</td>
                                </tr>
                            )
                        }
                    </tbody>
                </table>
            </div>
        )
    }
}

export default FundFinder
