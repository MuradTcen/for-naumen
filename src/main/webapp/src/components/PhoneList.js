import React, {Component} from "react";

import {Button, ButtonGroup, Card, FormControl, InputGroup, Table} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faEdit,
    faFastBackward,
    faFastForward,
    faList,
    faSearch,
    faStepBackward,
    faStepForward,
    faTimes,
    faTrash
} from "@fortawesome/free-solid-svg-icons";
import axios from 'axios';
import {MyToast} from "./MyToast";
import {Link} from "react-router-dom";

export class PhoneList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            phones: [],
            search: '',
            currentPage: 1,
            phonesPerPage: 5,
            totalElements: 0
        };
    }

    componentDidMount() {
        this.findAllPhones(this.state.currentPage);
    }

    findAllPhones(currentPage) {
        currentPage -= 1;

        const data = {
            "ascending": this.state.sortToggle,
            "desiredContent": "",
            "filterField": this.state.filterField,
            "page": currentPage,
            "size": this.state.phonesPerPage
        };

        axios.post("http://localhost:8080/api/phone/list/", data)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    phones: data.content,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1,
                })
            });
    }

    deletePhone = (phoneId) => {
        axios.delete("http://localhost:8080/api/phone/" + phoneId)
            .then((response) => {
                if (response.data != null) {
                    this.setState({"show": true});
                    setTimeout(() => this.setState({"show": false}), 3000);
                    this.setState({
                        phones: this.state.phones.filter(phone => phone.id !== phoneId)
                    })
                } else {
                    this.setState({"show": false});
                }
            });
    };

    changePage = event => {
        let targetPage = parseInt(event.target.value);
        if (this.state.search) {
            this.searchData(targetPage);
        } else {
            this.findAllPhones(targetPage);
        }
        this.state({
            [event.target.name]: targetPage
        });
    };

    firstPage = () => {
        let firstPage = 1;
        if (this.state.currentPage > firstPage) {
            if (this.state.search) {
                this.searchData(firstPage);
            } else {
                this.findAllPhones(firstPage);
            }
        }
    };

    prevPage = () => {
        let prevPage = 1;
        if (this.state.currentPage > prevPage) {
            if (this.state.search) {
                this.searchData(this.state.currentPage - prevPage);
            } else {
                this.findAllPhones(this.state.currentPage - prevPage);
            }
        }
    };

    lastPage = () => {
        let condition = Math.ceil(this.state.totalElements / this.state.phonesPerPage);
        if (this.state.currentPage < condition) {
            if (this.state.search) {
                this.findAllPhones(condition);
            } else {
                this.findAllPhones(condition);
            }
        }
    };

    nextPage = () => {
        if (this.state.currentPage < Math.ceil(this.state.totalElements / this.state.phonesPerPage)) {
            if (this.state.search) {
                this.searchData(this.state.currentPage + 1);
            } else {
                this.findAllPhones(this.state.currentPage + 1);
            }
        }
    };

    searchChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    cancelSearch = event => {
        this.setState({'search': ''});
        this.findAllPhones(this.state.currentPage);
    };

    searchDataNumber = currentPage => {
        let field = 'NUMBER';
        this.setState({
            filterField: field,
        });
        this.searchData(currentPage, field);
    };

    searchDataName = currentPage => {
        let field = 'NAME';
        this.setState({
            filterField: field,
        });
        this.searchData(currentPage, field);
    };

    searchData = (currentPage, filterField) => {
        currentPage -= 1;

        const data = {
            "ascending": this.state.sortToggle,
            "desiredContent": this.state.search,
            "filterField": filterField ? filterField : this.state.filterField,
            "page": currentPage,
            "size": this.state.phonesPerPage
        };

        axios.post("http://localhost:8080/api/phone/list", data)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    phones: data.content,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1,
                })
            });
    };

    render() {
        const {phones, currentPage, totalPages, search} = this.state;

        return (
            <div>
                <div style={{"display": this.state.show ? "block" : "none"}}>
                    <MyToast show={this.state.show} message={"Phone Deleted Successfully."} type={"danger"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <div style={{"float": "left"}}><FontAwesomeIcon icon={faList}/> Phones</div>
                        <div style={{"float": "right"}}>
                            <InputGroup size="sm">
                                <FormControl placeholder="Search" name="search" value={search}
                                             className={"bg-dark text-white"}
                                             onChange={this.searchChange}/>
                                <InputGroup.Append>
                                    <Button size="sm" variant="outline-info" type="button"
                                            onClick={this.searchDataName}>
                                        <FontAwesomeIcon icon={faSearch}/> Name
                                    </Button>
                                    <Button size="sm" variant="outline-info" type="button"
                                            onClick={this.searchDataNumber}>
                                        <FontAwesomeIcon icon={faSearch}/> Number
                                    </Button>
                                    <Button size="sm" variant="outline-danger" type="button"
                                            onClick={this.cancelSearch}>
                                        <FontAwesomeIcon icon={faTimes}/>
                                    </Button>
                                </InputGroup.Append>
                            </InputGroup>
                        </div>
                    </Card.Header>
                    <Card.Body>
                        <Table bordered hover striped variant="dark" size="sm">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Number</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.phones.length === 0 ?
                                <tr align="center">
                                    <td colSpan="9"> Phones Available</td>
                                </tr> :
                                this.state.phones.map((phone) => (
                                    <tr key={phone.id}>
                                        <td>{phone.name}</td>
                                        <td>{phone.number}</td>
                                        <td>
                                            <ButtonGroup>
                                                <Link to={"edit-phone/" + phone.id}
                                                      className="btn btn-sm btn-outline-primary"><FontAwesomeIcon
                                                    icon={faEdit}/></Link>{' '}
                                                <Button size="sm" variant="outline-danger"
                                                        onClick={this.deletePhone.bind(this, phone.id)}>
                                                    <FontAwesomeIcon icon={faTrash}/>
                                                </Button>{' '}
                                            </ButtonGroup>
                                        </td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </Table>
                    </Card.Body>
                    {phones.length > 0 ?
                        <Card.Footer>
                            <div style={{"float": "left"}}>
                                Showing Page {currentPage} of {totalPages}
                            </div>
                            <div style={{"float": "right"}}>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === 1 ? true : false}
                                                onClick={this.firstPage}>
                                            <FontAwesomeIcon icon={faFastBackward}/> First
                                        </Button>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === 1 ? true : false}
                                                onClick={this.prevPage}>
                                            <FontAwesomeIcon icon={faStepBackward}/> Prev
                                        </Button>
                                    </InputGroup.Prepend>
                                    <FormControl className={"page-num bg-dark"} name="currentPage" value={currentPage}
                                                 onChange={this.changePage}/>
                                    <InputGroup.Append>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === totalPages ? true : false}
                                                onClick={this.nextPage}>
                                            <FontAwesomeIcon icon={faStepForward}/> Next
                                        </Button>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === totalPages ? true : false}
                                                onClick={this.lastPage}>
                                            <FontAwesomeIcon icon={faFastForward}/> Last
                                        </Button>
                                    </InputGroup.Append>
                                </InputGroup>
                            </div>
                        </Card.Footer> : null
                    }
                </Card>
            </div>
        );
    }
}
