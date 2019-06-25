import React from "react";
import {Route} from "react-router-dom";
import Login from "./components/Login";

export default () => 
    <Route path="/auth" exact component={Login} />
