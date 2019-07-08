import React, { Component } from 'react'

class Loader extends Component {
    render() {
        return (
            <div>
                <img data-src={require('./images/Loader.gif')} />  
            </div>
        )
    }
}

export default Loader
