import React, { Component } from "react";
import { Form, Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";

class Login extends Component{
    constructor(props) {
        super(props);
    
        this.state = {
            userId :"",
            password : ""
        }
    }

    validateForm() {
        return this.state.userId.length > 0 && this.state.password.length > 0;
    }

    handleChange = (event) => {
        this.setState({
            [event.target.id]: event.target.value
        });
    }

    handleSubmit = (event) => {
        event.preventDefault();
    }

    render() {
        return (
            <div className="Login">
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group controlId="userId" bsSize="large">
                        <FormControl
                        autoFocus
                        type="text"
                        value={this.state.userId}
                        onChange={this.handleChange}
                        />
                        </Form.Group>
                        <Form.Group controlId="password" bsSize="large">
                        <Form.Control
                        value={this.state.password}
                        onChange={this.handleChange}
                        type="password"
                        />
                    </Form.Group>
                    <Button block bsSize="large" disabled={!this.validateForm()} type="submit">Login</Button>
                </Form>
            </div>
        );
    }
}

export default Login;