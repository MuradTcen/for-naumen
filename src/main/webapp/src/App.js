import React from 'react';
import './App.css';
import {NavigationBar} from './components/NavigationBar';

import {Col, Container, Row} from "react-bootstrap";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'

import Welcome from "./components/Welcome";
import {Phone} from "./components/Phone";
import {PhoneList} from "./components/PhoneList";

function App() {

    return (
        <Router>
            <NavigationBar/>
            <Container>
                <Row>
                    <Col lg={14} className={"margin-top"}>
                        <Switch>
                            <Route path="/" exact component={Welcome}/>
                            <Route path="/add-phone" exact component={Phone}/>
                            <Route path="/edit-phone/:id" exact component={Phone}/>
                            <Route path="/phone-list" exact component={PhoneList}/>
                            //todo: разобраться с редиректом на страницу сваггера
                            <Route path='/swagger' component={() => { window.location = 'https://naumen-phones.herokuapp.com/swagger-ui.html#'; return null;} }/>
                            <Route path='/github' component={() => { window.location = 'https://github.com/MuradTcen/for-naumen'; return null;} }/>
                        </Switch>
                    </Col>
                </Row>
            </Container>
        </Router>
    );
}

export default App;
