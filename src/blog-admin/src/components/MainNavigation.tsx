import * as React from 'react';
import * as Navbar from 'react-bootstrap/lib/Navbar';
import * as Nav from 'react-bootstrap/lib/Nav';
import * as NavItem from 'react-bootstrap/lib/NavItem';
import * as NavDropdown from 'react-bootstrap/lib/NavDropdown';
import * as MenuItem from 'react-bootstrap/lib/MenuItem';

class MainNavigation extends React.Component {
    render() {
        return (
            <Navbar>
                <Navbar.Header>
                <Navbar.Brand>
                    <a href="#">Administration</a>
                </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                <NavItem eventKey={1} href="#">Link</NavItem>
                <NavItem eventKey={2} href="#">Link2</NavItem>
                <NavDropdown eventKey={3} title="Dropdown" id="basic-nav-dropdown">
                    <MenuItem eventKey={3.1}>Action</MenuItem>
                    <MenuItem eventKey={3.2}>Another action</MenuItem>
                    <MenuItem eventKey={3.3}>Something else here</MenuItem>
                    <MenuItem divider={true} />
                    <MenuItem eventKey={3.4}>Separated link</MenuItem>
                </NavDropdown>
                </Nav>
            </Navbar>
        );
    }
}

export default MainNavigation;