import React, { Component } from 'react'
import './css/userFundTable.css'
import 'materialize-css/dist/css/materialize.min.css'
import getCookie from './Cookie';
import axios from 'axios';
import { withRouter } from 'react-router-dom';
import searchContent from './utility/SearchTableContent'

class UserFunds extends Component
{
    constructor()
    {
        super()
        this.state = {
            list : [],
            searchableFields: []
        }
    }

    placeTradeClicked(){
        var checked = document.querySelectorAll('input:checked');
        if (checked.length === 0) {
            alert('Please select atleast 1 fund to trade');
        } else {
            for(var i = 0; i< checked.length; i++){
                var tr = checked[i].parentNode.parentNode.parentNode;
                var table_cells = tr.getElementsByTagName('td')
                for(var j = 0; j< table_cells.length; j++){
                    console.log(table_cells[j].textContent);
                }
            }
        }
    }


    componentDidMount(){
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else if(this.props.portfolio){
            axios.get('http://localhost:8762/portfolio', {headers : { Authorization: `Bearer ${jwt}` } })
            .then(res => {
                console.log(res.data.all_funds);
                this.setState({
                    list : res.data.all_funds,
                    searchableFields: [0,1,2,4]
                })
            }).catch( err => {
                if(err.response.status === 401){
                    document.cookie = "token=";
                    this.props.history.push('/');
                }
            });    
        }else if(!(this.props.portfolio)){
            axios.get('http://localhost:8762/fund-handling/api/entitlements/get', {headers : { Authorization: `Bearer ${jwt}` } })
            .then(res => {
                this.setState({
                    list : res.data,
                    searchableFields: [0,1,2,4]
                })
            }).catch( err => {
                if(err.response.status === 401){
                    document.cookie = "token=";
                    this.props.history.push('/');
                }
            });   
        }
    }

    render(){
        var spanStyle = {
            fontSize: '18px'  
        };
        return (   
            <div>
                <table align = "center" id = "myTable">
                    <thead>
                    <tr className = "software">
                        <th>Fund Name</th>
                        <th>Investment Manager</th>
                        <th>Fund Number</th>
                        <th>Settlement Cycle</th>
                        <th>Investment Currency</th>
                        <th>S&P Rating</th>
                        <th>Moody's Rating</th>
                        {this.props.portfolio?<th>Quantity</th>:""}
                        {this.props.portfolio?<th>Purchase NAV</th>:""}
                        <th>Current NAV</th>
                        {this.props.portfolio?<th>Profit/Loss Difference Amount</th>:""}
                        {this.props.portfolio?<th>Profit %</th>:""}
                        {this.props.portfolio?<th>Indicator</th>:""}
                    </tr>
                    </thead>

                    <tbody>
                        <tr>
                            <td><input type="text" id="myInput0"  
                                onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td> 
                            <td><input type="text" id="myInput1" 
                                onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td>
                            <td><input type="text" id="myInput2"  
                                onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td>
                            <td></td>
                            {this.props.portfolio?<td></td>:""}
                            <td></td>
                            <td></td>
                            {/* <td><input type="text" id="myInput3"  onKeyUp={() => this.searchContent()} /></td> */}
                            {this.props.portfolio?<td></td>:""}
                            {this.props.portfolio?<td></td>:""}
                            <td></td>
                            <td></td>
                            {/* <td><input type="text" id="myInput3"  
                                onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td> */}
                            {this.props.portfolio?<td></td>:""}
                            {this.props.portfolio?<td></td>:""}
                        </tr>
                        {               
                            this.state.list.map(item => <tr key={item.fundNumber}>
                            <td>
                                <label>
                                    <input type="checkbox" />
                                    <span id="fund-name" style={spanStyle} className="fundname-label">{item.fundName}</span>
                                </label>
                            </td>
                            <td>{item.invManager}</td>
                            <td>{item.fundNumber}</td>
                            <td>{item.setCycle}</td>
                            <td>{item.invCurrency}</td>
                            <td>{item.sAndPRating}</td>
                            <td>{item.moodysRating}</td>
                            {this.props.portfolio?<td>{item.quantity}</td>:""}
                            {this.props.portfolio?<td>{item.originalNav}</td>:""}
                            {this.props.portfolio?<td>{item.presentNav}</td>:<td>{item.nav}</td>}
                            {this.props.portfolio?<td>{item.profitAmount}</td>:""}
                            {this.props.portfolio?<td>{item.profitPercent}</td>:""}
                            {this.props.portfolio?
                                <td>{
                                    (item.profitAmount>0 && item.profitPercent>0)?<i className="fa fa-arrow-up"></i>: <i className="fa fa-arrow-down"></i>
                                    }
                                </td>:""
                            }
                            </tr>) 
                        }
                    </tbody>
                </table>

                <button className="btn waves-effect waves-light" onClick = {() => this.placeTradeClicked()}>
                    Place trade
                </button>
          </div>
        )
    }
}


export default withRouter(UserFunds)