import React, { Component } from 'react'
import './css/fundFinder.css'
import SearchBar from './SearchBar';


class FundFinder extends Component {
    render() {
        return (
            <div className="table-wrapper">
                <table>
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
                            <th scope="row"><SearchBar searchTerm = "Search Fund Number"></SearchBar></th>
                            <th scope="row"><SearchBar searchTerm = "Search Fund Name"></SearchBar></th>
                            <th scope="row"><SearchBar searchTerm = "Search Manager"></SearchBar></th>
                            <td>-N/A-</td>
                            <td>-N/A-</td>
                            <th scope="row"><SearchBar searchTerm = "Search Currency"></SearchBar></th>
                            <td>-N/A-</td>
                            <td>-N/A-</td>
                        </tr>
                        <tr>
                            <th scope="row">12345</th>
                            <td>GS Funds</td>
                            <td>Goldman Sachs</td>
                            <td>2</td>
                            <td>23.3</td>
                            <td>USD</td>
                            <td>21.5</td>
                            <td>200</td>
                        </tr>
                        <tr>
                            <th scope="row">12345</th>
                            <td>GS Funds</td>
                            <td>Goldman Sachs</td>
                            <td>2</td>
                            <td>23.3</td>
                            <td>USD</td>
                            <td>21.5</td>
                            <td>200</td>
                        </tr>
                        <tr>
                            <th scope="row">12345</th>
                            <td>GS Funds</td>
                            <td>Goldman Sachs</td>
                            <td>2</td>
                            <td>23.3</td>
                            <td>USD</td>
                            <td>21.5</td>
                            <td>200</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        )
    }
}

export default FundFinder
