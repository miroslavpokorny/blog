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
import { UserRole } from '../api/UserRole';
import PageHelper from '../helpers/PageHelper';
import { SignOutAction } from '../api/SignControllerApi';

interface MainNavigationProps {
    pathName: string;
}

@observer
class MainNavigation extends React.Component<MainNavigationProps> {
    state: {
        signingOut: boolean;
    };
    constructor(props: MainNavigationProps) {
        super(props);
        State.mainNavigation.currentLink = props.pathName;
        this.state = {
            signingOut: false
        };
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
                    <Link to={RouteName.home}>Blog Administration</Link>
                </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                <NavItem eventKey={RouteName.about} href="#">About</NavItem>
                {this.canDisplay(UserRole.User) &&
                    <NavDropdown title="Profile" id="basic-nav-dropdown">
                        <MenuItem eventKey={RouteName.profile}>Profile</MenuItem>
                        <MenuItem eventKey={RouteName.profileEdit}>Edit profile</MenuItem>
                        <MenuItem eventKey={RouteName.profileUploadAvatar}>Upload avatar</MenuItem>
                        <MenuItem eventKey={RouteName.profileChangePassword}>Change password</MenuItem>
                    </NavDropdown>
                }
                <NavDropdown eventKey={3} title="Dropdown" id="basic-nav-dropdown">
                    <MenuItem eventKey={3.1}>Action</MenuItem>
                    <MenuItem eventKey={3.2}>Another action</MenuItem>
                    <MenuItem eventKey={3.3}>Something else here</MenuItem>
                    <MenuItem divider={true} />
                    <MenuItem eventKey={3.4}>Separated link</MenuItem>
                </NavDropdown>
                </Nav>
                <Nav pullRight={true}>
                {this.isUserSignedIn() ? (
                    <NavItem onClick={
                        () => {
                            this.setState({ signingOut: true });
                            SignOutAction((error) => {
                                if (error) {
                                    this.setState({ signingOut: false });
                                }
                            });
                        }
                    } href="#">{this.state.signingOut ? 'Signing out...' : 'Sign out'}</NavItem>
                ) : (
                    <NavItem eventKey={RouteName.signIn} href="#">Sign in</NavItem>
                )}
                </Nav>
            </Navbar>
        );
    }

    private handleSelect(route: {}) {
        State.mainNavigation.redirectLink = route as RouteName;
    }

    private canDisplay(minUserRole: UserRole): boolean {
        return State.loggedUser.role !== undefined && State.loggedUser.role >= minUserRole;
    }

    private isUserSignedIn(): boolean {
        return PageHelper.isUserSignedIn();
    }
}

export default MainNavigation;