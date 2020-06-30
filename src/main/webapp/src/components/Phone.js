import React, {Component} from "react";

import {Card, Form, Button, Col} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSave, faPlusSquare, faUndo, faList, faEdit} from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import {MyToast} from './MyToast';

export class Phone extends Component {

    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state = {
            show: false,
            showErrors: false
        };
        this.phoneChange = this.phoneChange.bind(this);
        this.submitPhone = this.submitPhone.bind(this);
    }

    initialState = {
        id: '', name: '', number: ''
    };

    componentDidMount() {
        const phoneId = this.props.match.params.id;
        if (phoneId) {
            this.findPhoneById(phoneId);
        }
    }

    findPhoneById = (id) => {
        axios.get("http://localhost:8080//api/phone/" + id)
            .then(response => {
                if (response.data != null) {
                    this.setState({
                        id: response.data.id,
                        name: response.data.name,
                        number: response.data.number,
                    })
                }
            }).catch((error) => {
            console.error("Error - " + error)
        });
    };

    resetPhone = () => {
        this.setState(() => this.initialState);
    };

    submitPhone = event => {
        event.preventDefault();

        const phone = {
            id: this.state.id,
            name: this.state.name,
            number: this.state.number,
        };

        axios.post("http://localhost:8080//api/phone/", phone)
            .then(response => {
                if (response.data != null) {
                    this.setState({"show": true, "method": "post"});
                    setTimeout(() => this.setState({"show": false}), 3000);
                    setTimeout(() => this.phones, 3000);
                } else {
                    this.setState({"show": false});
                }
            })
            .catch(error => this.handleErrors(error));

        this.setState(this.initialState);
    };

    handleErrors = error => {
        this.setState({
            "showErrors": true, "errors": error.response.data.errors.map(function (error) {
                return "Поле " + error.field + " " + error.defaultMessage + "\n\n";
            })
        });
        setTimeout(() => this.setState({"showErrors": false}), 3000);
        setTimeout(() => this.phones, 3000);
    };

    updatePhone = event => {
        event.preventDefault();

        const phone = {
            id: this.state.id,
            name: this.state.name,
            number: this.state.number,
        };

        axios.put("http://localhost:8080/api/phone/", phone)
            .then(response => {
                if (response.data != null) {
                    this.setState({"show": true, "method": "put"});
                    setTimeout(() => this.setState({"show": false}), 3000);
                } else {
                    this.setState({"show": false});
                }
                this.setState(this.initialState);
            })
            .catch(error => {
                this.handleErrors(error)
                this.findPhoneById(this.state.id)
                }
            );
    };

    phoneChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    onChangeCheck = () => {
        this.setState(initialState => ({
            isTraded: !initialState.isTraded,
        }));
    };

    phoneList = () => {
        return this.props.phone.push("/phone-list");
    };

    render() {
        const {name, number} = this.state;
        return (
            <div>
                <div style={{"display": this.state.show ? "block" : "none"}}>
                    <MyToast show={this.state.show}
                             message={this.state.method === "put" ? "Phone Updated Successfully." : "Phone Saved Successfully."}
                             type={"success"}/>
                </div>
                <div style={{"display": this.state.showErrors ? "block" : "none"}}>
                    <MyToast show={this.state.showErrors}
                             message={this.state.errors}
                             type={"danger"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header><FontAwesomeIcon
                        icon={this.state.id ? faEdit : faPlusSquare}/>{this.state.id ? " Update Phone" : " Add New Phone"}
                    </Card.Header>
                    <Form onReset={this.resetPhone}
                          onSubmit={this.props.match.params.id ? this.updatePhone : this.submitPhone}
                          id="securityFormId">
                        <Card.Body>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridName">
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control required autoComplete="off"
                                                  type="text" name="name"
                                                  value={name}
                                                  onChange={this.phoneChange}
                                                  className={"bg-dark text-white"}
                                                  placeholder="Enter name"/>
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridNumber">
                                    <Form.Label>Number</Form.Label>
                                    <Form.Control required autoComplete="off"
                                                  type="text" name="number"
                                                  value={number} onChange={this.phoneChange}
                                                  className={"bg-dark text-white"}
                                                  placeholder="Enter number like: +7<999><9999999>"/>
                                </Form.Group>
                            </Form.Row>
                        </Card.Body>
                        <Card.Footer style={{"textAlign": "right"}}>
                            <Button size="sm" variant="success" type="submit">
                                <FontAwesomeIcon icon={faSave}/> {this.props.match.params.id ? "Update" : "Save"}
                            </Button>{' '}
                            <Button size="sm" variant="info" type="reset">
                                <FontAwesomeIcon icon={faUndo}/> Reset
                            </Button>{' '}
                            <Button size="sm" variant="info" type="button" onClick={this.phoneList.bind()}>
                                <FontAwesomeIcon icon={faList}/> Phone List
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
}
