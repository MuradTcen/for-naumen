import React from "react";
import {Nav, Navbar} from "react-bootstrap";
import {Link} from "react-router-dom";

export class NavigationBar extends React.Component {
    render() {
        return (
            <Navbar bg="dark" variant="dark">
                <Link to={""} className="navbar-brand">
                    <img src="/logo192.png" width="25" height="25" alt="Brand"/> PHONE BOOK
                </Link>
                <Nav className="mr-auto">
                    <Link to={"add-phone"} className="nav-link">Add Phone</Link>
                    <Link to={"phone-list"} className="nav-link">Phone List</Link>
                    <Link to={"swagger"} className="nav-link">Swagger</Link>
                    <Link to={"github"} className="nav-link">GitHub</Link>
                </Nav>
            </Navbar>

        );
    }
}



