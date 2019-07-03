import React, { Component } from 'react'
import './css/userFundTable.css'
import 'materialize-css/dist/css/materialize.min.css'
import searchContent from './utility/SearchTableContent'

class UserFunds extends Component
{
    constructor()
    {
        super()
        this.state = {
            searchableFields: [],
            list : [
              {
                fundName : 'fundname',
                fundNumber : '145',
                INV : 'GS',
                rating : 238945394,
                quantity : 10,
                NAV : 400,
                setCycle : 5,
                invCurr : 'INR',
                profit : '5%',
                arrow: 'up'
              },
              {
                fundName : 'fundname',
                fundNumber : '134',
                INV : 'EY',
                rating : 238945394,
                quantity : 10,
                NAV : 400,
                setCycle : 5,
                invCurr : 'INR',
                profit : '5%',
                arrow: 'up'
              },
              {
                fundName : 'hndname',
                fundNumber : '123',
                INV : 'KPMG',
                rating : 238945394,
                quantity : 10,
                NAV : 400,
                setCycle : 5,
                invCurr : 'USD',
                profit : '-5%',
                arrow: 'down'
              }
            ]
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
        if(this.props.portfolio){
            this.setState({
                searchableFields: [0,1,2,7]
            })
        }else{
            this.setState({
                searchableFields: [0,1,2,6]
            })
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
                        <th>Fund Number</th>
                        <th>Investment Manager</th>
                        <th>Rating</th>
                        {this.props.portfolio?<th>Quantity</th>:""}
                        <th>NAV</th>
                        <th>Sett. Cycle</th>
                        <th>Invest. Currency</th>
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
                            <td><input type="text" id="myInput3"  
                                onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td>
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
                            <td>{item.fundNumber}</td>
                            <td>{item.INV}</td>
                            <td>{item.rating}</td>
                            {this.props.portfolio?<td>{item.quantity}</td>:""}
                            <td>{item.NAV}</td>
                            <td>{item.setCycle}</td>
                            <td>{item.invCurr}</td>
                            {this.props.portfolio?<td>{item.profit}</td>:""}
                            {this.props.portfolio?
                                <td>{
                                    item.arrow === 'up'?<i className="fa fa-arrow-up"></i>: <i className="fa fa-arrow-down"></i>
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


export default UserFunds