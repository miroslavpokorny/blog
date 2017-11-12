import * as React from 'react';
import * as Navbar from 'react-bootstrap/lib/Navbar';
import * as Nav from 'react-bootstrap/lib/Nav';
import * as NavItem from 'react-bootstrap/lib/NavItem';
import * as NavDropdown from 'react-bootstrap/lib/NavDropdown';
import * as MenuItem from 'react-bootstrap/lib/MenuItem';
import { Link } from 'react-router-dom';
import { RouteName } from '../Router';
import { Redirect } from 'react-router';
import { observer } from 'mobx-react';
import { State } from '../BlogAdminStore';

interface MainNavigationProps {
    pathName: string;
}

@observer
class MainNavigation extends React.Component<MainNavigationProps> /*<NavigationParams, {}>*/ {
    constructor(props: MainNavigationProps) {
        super(props);
        State.mainNavigation.currentLink = props.pathName;
    }
    
    handleSelect(route: {}) {
        State.mainNavigation.redirectLink = route as RouteName;
    }

    componentDidUpdate() {
        // After redirect set current link and unset redirect link
        if (State.mainNavigation.redirectLink !== undefined &&
            State.mainNavigation.redirectLink !== State.mainNavigation.currentLink) {
            State.mainNavigation.currentLink = State.mainNavigation.redirectLink;
            State.mainNavigation.redirectLink = undefined;
        }
    }

    render() {
        if (State.mainNavigation.redirectLink !== undefined &&
            State.mainNavigation.currentLink !== State.mainNavigation.redirectLink) {
            let redirect = <Redirect push={true} to={State.mainNavigation.redirectLink} />;
            return redirect;
        }
        return (
            <Navbar onSelect={(eventKey) => this.handleSelect(eventKey)}>
                <Navbar.Header>
                <Navbar.Brand>
                    <Link to="/">Administration</Link>
                </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                <NavItem eventKey={RouteName.about} href="#">About</NavItem>
                <NavDropdown title="Profile" id="basic-nav-dropdown">
                    <MenuItem eventKey={RouteName.profile}>Profile</MenuItem>
                    <MenuItem eventKey={RouteName.profileEdit}>Edit profile</MenuItem>
                    <MenuItem eventKey={RouteName.profileUploadAvatar}>Upload avatar</MenuItem>
                    <MenuItem eventKey={RouteName.profileChangePassword}>Change password</MenuItem>
                </NavDropdown>
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