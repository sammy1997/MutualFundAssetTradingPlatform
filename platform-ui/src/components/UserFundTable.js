import React, { Component } from 'react'
import './css/userFundTable.css'
import 'materialize-css/dist/css/materialize.min.css'
import getCookie from './Cookie';
import axios from 'axios';
import { withRouter } from 'react-router-dom';
import searchContent from './utility/SearchTableContent';
import TradeBlotter from './TradeBlotter';
import Modal from 'react-responsive-modal';
import parseJwt from './utility/JwtParser';

class UserFunds extends Component
{
    constructor()
    {
        super()
        this.state = {
            selectedFunds:undefined,
            numberOfSelectedFunds: 0,
            list : [],
            searchableFields: [],
            open: false,
            errorResponse: [],
            role: undefined,
            currencies: []
        }
    }

    closeModalHandler = () => {
        this.setState({
            open: false
        })
    }

    onSelect = () =>{
        this.setState({
            numberOfSelectedFunds: document.querySelectorAll('input[type="checkbox"]:checked').length
        })
    }
    placeTradeClicked(){
        var checked = document.querySelectorAll('input:checked');
        // if (checked.length === 0) {
        //     alert('Please select atleast 1 fund to trade');
        // } else {
            var selected = [];
            for(var i = 0; i< checked.length; i++){
                var tr = checked[i].parentNode.parentNode.parentNode;
                var table_cells = tr.getElementsByTagName('td')
                
                var fieldName = ['fundName', 'invManager', 'fundNumber', 'presentNav']
                var temp = {};
                for(var j = 0; j< table_cells.length; j++){
                    console.log(table_cells[j].textContent);
                    if(j<4){
                        temp[fieldName[j]] = table_cells[j].textContent;
                    }
                    
                    this.setState({
                        open: true,
                    })
                }
                
                selected.push(temp);
            }
            this.setState({
                selectedFunds: selected
            }, () => {console.log(this.state.selectedFunds)})

    }

    loadAssets(res){
        var baseUrl = "http://localhost:8762/";
        var token = getCookie('token');
        var dict = {};
        if(!token){
            this.props.history.push('/');
        }
        // Setting headers
        var headers ={
            Authorization: 'Bearer ' + token
        }
        axios({
            method: 'get',
            url: baseUrl + 'trade/currency/all',
            headers: headers
        }).then(response =>{
            console.log(response.data);
            if(Array.isArray(response.data)){
                for(var x=0;x<response.data.length; x++){
                    dict["" + response.data[x].currency] = response.data[x].rate;
                }
                this.setState({
                    currencies: response.data
                })
                console.log(dict);
                var assetsValue = 0;
                var baseVal = dict[res.data.baseCurr]
                console.log(baseVal);
                for(var i=0; i<res.data.all_funds.length; i++){
                    var currFund = res.data.all_funds[i]
                    console.log(currFund);

                    console.log(dict[currFund.invCurrency]);
                    assetsValue += currFund.quantity*currFund.presentNav*baseVal/dict[currFund.invCurrency];
                }
                this.props.updateAssets(assetsValue.toFixed(2));
            }
        }).catch(error =>{
            console.log(error);
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }
        });
        
        
    }

    componentDidMount(){
        var jwt = getCookie('token');
        if(!jwt){
            this.props.history.push('/');
        }else if(this.props.portfolio){
            axios.get('http://localhost:8762/portfolio', {headers : { Authorization: `Bearer ${jwt}` } })
            .then(res => {
                this.loadAssets(res);
                console.log(res);
                
                this.setState({
                    list : res.data.all_funds,
                    searchableFields: [0,1,2,4],
                    role: parseJwt(jwt).authorities[0]
                })
                console.log(this.state.role);
            })
            .catch( error => {
                if(error.response){
                    if(error.response.status === 500){
                        this.setState({
                            errorResponse: [<p>The server is down at the moment. Please try again later.</p>]
                        })
                    }else if (error.response.status === 403){
                        this.setState({
                            errorResponse: [<p>You are not authorized to access this page. Please Logout.</p>]
                        })
                    }else if (error.response.status === 401){
                        document.cookie = "token= ";
                        window.location = "/"
                    }
                }
            });  
        }else if(!(this.props.portfolio)){
            axios.get('http://localhost:8762/fund-handling/api/entitlements/get', {headers : { Authorization: `Bearer ${jwt}` } })
            .then(res => {
                if(res.status == 200){
                    this.setState({
                        list : res.data,
                        role: parseJwt(jwt).authorities[0]
                    })
                }
                this.setState({
                    searchableFields: [0,1,2,4]
                })
            }).catch( error => {
                if(error.response){
                    if(error.response.status === 500){
                        this.setState({
                            errorResponse: [<p>The server is down at the moment. Please try again later.</p>]
                        })
                    }else if (error.response.status === 403){
                        this.setState({
                            errorResponse: [<p>You are not authorized to access this page. Please Logout.</p>]
                        })
                    }else if (error.response.status === 401){
                        document.cookie = "token= ";
                        window.location = "/"
                    }
                }
            });   
        }
    }

    render(){
        var spanStyle = {
            fontSize: '18px'  
        };
        let content1, content2, content, content3;
        content3 = <button className="btn waves-effect waves-light"  onClick = {() => this.placeTradeClicked()}>
        Place trade
        </button>
        this.state.list.length!=0 ? (content1 = <h5>You have selected {this.state.numberOfSelectedFunds} fund(s).</h5>) :(content1 =<h5>You don't have any funds to trade at the moment. Please head to the fund finder page</h5>)
        content = <p/>
        this.state.role === "ROLE_VIEWER" ? ( content2 = <h5 className ="alert">You are not allowed to place any trades</h5>):(content2 = content1)
        return (   
            <div class="table-container">
                
                <div className="error-response">
                    {this.state.errorResponse}
                </div>

                {/* <div className="row ">
                    <form className = "search-form">
                        <div className = "input-field col s4">
                            <label htmlFor="myInput0">Search By Fund Name</label>
                            <i class="fa fa-search search-icon" aria-hidden="true"></i>
                            <input type="text" palceholder="Search By Fund Name" id="myInput0" onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} />
                            
                        </div>  
                        <div className = "input-field col s4">
                            <label htmlFor="myInput1">Search By Investment Manager</label>
                            <i class="fa fa-search search-icon" aria-hidden="true"></i>
                            <input type="text" palceholder="Search By Investment Manager" id="myInput1" onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} />
                        </div>  
                        <div className = "input-field col s4">  
                            <label htmlFor="myInput2">Search By Fund Number</label>
                            <i class="fa fa-search search-icon" aria-hidden="true"></i>
                            <input type="text" palceholder="Search By Fund Number" id="myInput2" onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} />
                        </div>
                    </form>
                </div> */}
                <table align = "center" id = "myTable">
                    <thead>
                    <tr className = "software">
                        <th>Fund Name</th>
                        <th>Investment Manager</th>
                        <th>Fund Number</th>
                        <th>Current NAV</th>
                        {this.props.portfolio?<th>Purchase NAV</th>:""}
                        <th>Settlement Cycle</th>
                        <th>Investment Currency</th>
                        <th>S&P Rating</th>
                        <th>Moody's Rating</th>
                        {this.props.portfolio?<th>Quantity</th>:""}
                        {this.props.portfolio?<th>Expected Profit/Loss</th>:""}
                        {this.props.portfolio?<th>Profit %</th>:""}
                        {this.props.portfolio?<th>Indicator</th>:""}
                    </tr>
                    </thead>

                    <tbody>
                        <tr>
                            <td>
                                <div className="search-icon-container">
                                    <i class="fa fa-search search-icon" aria-hidden="true"></i>
                                </div>
                                <input type="text" id="myInput0"  
                                onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td> 
                            <td>
                                <div className="search-icon-container">
                                    <i class="fa fa-search search-icon" aria-hidden="true"></i>
                                </div><input type="text" id="myInput1" 
                                onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td>
                            { <td>
                                <div className="search-icon-container">
                                    <i class="fa fa-search search-icon" aria-hidden="true"></i>
                                </div><input type="text" id="myInput2"  
                            onKeyUp={() => searchContent('myInput', 'myTable', this.state.searchableFields)} /></td> }
                            { <td></td>}
                            {this.props.portfolio?<td></td>:""}
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            {this.props.portfolio?<td></td>:""}
                            {this.props.portfolio?<td></td>:""}
                            {this.props.portfolio?<td></td>:""}
                            {this.props.portfolio?<td></td>:""} 
                        </tr>
                        {               
                            this.state.list.map(item => <tr key={item.fundNumber}>
                            <td>
                                <label>
                                    {this.state.role==="ROLE_VIEWER"?<input type="checkbox" disabled/>:<input type="checkbox" onChange={this.onSelect}/>}
                                    <span id="fund-name" style={spanStyle} className="fundname-label">{item.fundName}</span>
                                </label>
                            </td>
                            <td>{item.invManager}</td>
                            <td>{item.fundNumber}</td>
                            {this.props.portfolio?<td>{item.presentNav}</td>:<td>{item.nav}</td>}
                            {this.props.portfolio?<td>{item.originalNav}</td>:""}
                            <td>{item.setCycle}</td>
                            <td>{item.invCurrency}</td>
                            <td>{item.sAndPRating}</td>
                            <td>{item.moodysRating}</td>
                            {this.props.portfolio?<td>{item.quantity}</td>:""}
                            {this.props.portfolio?(item.profitAmount == 0?<td>{item.profitAmount}</td>:
                            (item.profitAmount>0)?<td className="profit">{item.profitAmount}</td>:<td className="loss">{item.profitAmount}</td>):""}
                            {/* {this.props.portfolio?<td>{item.profitPercent * 100}</td>:""} */}
                            {this.props.portfolio?(item.profitAmount == 0?<td>{item.profitAmount}</td>:
                            (item.profitAmount > 0)?<td className="profit">{item.profitPercent*100}</td>:<td className="loss">{item.profitPercent*100}</td>):""}
                            {this.props.portfolio?(item.profitAmount == 0?<td> - </td>:
                                <td>{
                                    (item.profitAmount>0)?<i className="fa fa-arrow-up profit"></i>: <i className="fa fa-arrow-down loss"></i>
                                    }
                            </td>):""
                            }
                            </tr>) 
                        }
                    </tbody>
                </table>

                
                
                {content2}
                {this.state.role !== "ROLE_VIEWER" ? content3 : content}
                <Modal classNames="modal" open ={this.state.open} onClose={this.closeModalHandler} center >
                    <div> 
                        <TradeBlotter funds = {this.state.selectedFunds}/> 
                    </div> 
                </Modal>
          </div>
        )
    }
}


export default withRouter(UserFunds)