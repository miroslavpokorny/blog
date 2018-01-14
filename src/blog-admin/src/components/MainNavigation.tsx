import * as React from "react";
import * as Navbar from "react-bootstrap/lib/Navbar";
import * as Nav from "react-bootstrap/lib/Nav";
import * as NavItem from "react-bootstrap/lib/NavItem";
import * as NavDropdown from "react-bootstrap/lib/NavDropdown";
import * as MenuItem from "react-bootstrap/lib/MenuItem";
import { RouteName } from "../Router";
import { Redirect } from "react-router";
import { observer } from "mobx-react";
import { State } from "../BlogAdminStore";
import { UserRole } from "../api/UserRole";
import PageHelper from "../helpers/PageHelper";
import { SignOutAction } from "../api/SignControllerApi";

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
        if (
            State.mainNavigation.redirectLink !== undefined &&
            State.mainNavigation.redirectLink !== State.mainNavigation.currentLink
        ) {
            State.mainNavigation.currentLink = State.mainNavigation.redirectLink;
            State.mainNavigation.redirectLink = undefined;
            State.mainNavigation.redirectId = undefined;
        }
    }

    render() {
        if (
            State.mainNavigation.redirectLink !== undefined &&
            State.mainNavigation.currentLink !== State.mainNavigation.redirectLink
        ) {
            let redirect: JSX.Element;
            if (State.mainNavigation.redirectId !== undefined) {
                redirect = (
                    <Redirect
                        push={true}
                        to={`${State.mainNavigation.redirectLink}/${State.mainNavigation.redirectId}`}
                    />
                );
            } else {
                redirect = <Redirect push={true} to={State.mainNavigation.redirectLink} />;
            }
            return redirect;
        }
        return (
            <Navbar onSelect={eventKey => this.handleSelect(eventKey)}>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a
                            href="#"
                            onClick={() => {
                                State.mainNavigation.redirectLink = RouteName.home;
                            }}
                        >
                            Blog Administration
                        </a>
                    </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                    <NavItem eventKey={RouteName.about} href="#">
                        About
                    </NavItem>
                    {this.canDisplay(UserRole.User) && (
                        <NavDropdown title="Profile" id="basic-nav-dropdown">
                            <MenuItem eventKey={RouteName.profile}>Profile</MenuItem>
                            <MenuItem eventKey={RouteName.profileEdit}>Edit profile</MenuItem>
                            {/* <MenuItem eventKey={RouteName.profileUploadAvatar}>Upload avatar</MenuItem>
                        <MenuItem eventKey={RouteName.profileChangePassword}>Change password</MenuItem> */}
                        </NavDropdown>
                    )}
                    {this.canDisplay(UserRole.Editor) && (
                        <NavDropdown title="Articles" id="basic-nav-dropdown">
                            <MenuItem eventKey={RouteName.article}>All articles</MenuItem>
                            <MenuItem eventKey={RouteName.articleAdd}>Add article</MenuItem>
                        </NavDropdown>
                    )}
                    {this.canDisplay(UserRole.Editor) && (
                        <NavDropdown title="Galleries" id="basic-nav-dropdown">
                            <MenuItem eventKey={RouteName.gallery}>All galleries</MenuItem>
                            <MenuItem eventKey={RouteName.galleryAdd}>Add gallery</MenuItem>
                        </NavDropdown>
                    )}
                    {this.canDisplay(UserRole.Moderator) && (
                        <NavDropdown title="Categories" id="basic-nav-dropdown">
                            <MenuItem eventKey={RouteName.category}>All categories</MenuItem>
                            <MenuItem eventKey={RouteName.categoryAdd}>Add category</MenuItem>
                        </NavDropdown>
                    )}
                    {this.canDisplay(UserRole.Administrator) && <NavItem eventKey={RouteName.user}>Users</NavItem>}
                </Nav>
                <Nav pullRight={true}>
                    {this.isUserSignedIn() ? (
                        <NavItem
                            onClick={() => {
                                this.setState({ signingOut: true });
                                SignOutAction(error => {
                                    if (error) {
                                        this.setState({ signingOut: false });
                                    }
                                });
                            }}
                            href="#"
                        >
                            {this.state.signingOut ? "Signing out..." : "Sign out"}
                        </NavItem>
                    ) : (
                        <NavItem eventKey={RouteName.signIn} href="#">
                            Sign in
                        </NavItem>
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
