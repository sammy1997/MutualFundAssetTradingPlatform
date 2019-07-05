import React from 'react'
import { post } from 'axios';
import getCookie from './Cookie';
import './css/fileUploadComponent.css'

class FileUpload extends React.Component {

    constructor(props) {
        super(props);
    
        this.state ={
            file:null
        }
    
        this.onFormSubmit = this.onFormSubmit.bind(this)
        this.onChange = this.onChange.bind(this)
        this.fileUpload = this.fileUpload.bind(this)
    }

    onFormSubmit(e){
        e.preventDefault();
        console.log(this.state.file);
        this.fileUpload(this.state.file);
    }
    
    onChange(e) {
        this.setState({file:e.target.files[0]})
    }
    
    fileUpload(file){
        var token = getCookie('token')
        if(!token){
            this.props.history.push('/');
        }

        var baseUrl = 'http://localhost:8762/fund-handling/api/';
        const formData = new FormData();
        formData.append('file',file)
        if(file===null){
            alert("Select a file");
            return;
        }
        const config = {
            headers: {
                Authorization: 'Bearer '+ token,
                'content-type': 'multipart/form-data'
            }
        }
        
        post(baseUrl + this.props.endUrl, formData, config).then(response =>{
            console.log(response.data)
            alert(response.data);
            document.getElementById("file-submit").value = "";
        }).catch(error => {
            console.log(error.response)
            if(error.response.status === 401 || error.response.status === 403){
                document.cookie = "token=;"
                window.location = "/";
            }else{
                alert(error.response.data);
            }
            
        })
    }

    render() {
        return (
            <div className="form-wrapper">
                <form onSubmit={this.onFormSubmit}>
                <button className="btn" id="csv-add" type="submit">
                    {this.props.buttonText}
                </button>
                <input id="file-submit" type="file" onChange={this.onChange} />
            </form>
            </div>
        )
    }
}



export default FileUpload