import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import { UserRole } from "../api/UserRole";
import PageHelper from "../helpers/PageHelper";
import { observer } from "mobx-react";
import { UsersListDto, GetUsersListAction, SwitchUserEnabledStateAction, ChangeUserRoleAction } from "../api/UsersControllerApi";
import * as Alert from "react-bootstrap/lib/Alert";
import { Table, Modal, FormGroup, ControlLabel, FormControl } from "react-bootstrap";
import * as Button from "react-bootstrap/lib/Button";
import LoadingOverlay from "../components/LoadingOverlay";
import { State } from "../BlogAdminStore";
// import { State } from '../BlogAdminStore';

interface UserPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<UserPageParams> {}

@observer
export default class UserPage extends React.Component<PageProps> {
    state: {
        usersList?: UsersListDto;
        errorMessage?: string;
        successMessage?: string;
        showModal: boolean;
        selectedUserId?: number;
        selectedRole?: number;
    };

    private _isMounted: boolean = false;

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            usersList: undefined,
            successMessage: undefined,
            showModal: false,
            selectedUserId: undefined,
            selectedRole: undefined
        };
        setTimeout(() => this.loadUsers(), 0); // this.loadUsers();
    }

    componentDidMount() {
        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(UserRole.Administrator, this.props.location.pathname, () => {
            return (
                <div>
                    <MainNavigation pathName={this.props.location.pathname} />
                    <div className="container">{!this.props.match.params.action && this.renderDefault()}</div>
                </div>
            );
        });
    }

    private loadUsers() {
        GetUsersListAction((error, result) => {
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                this.setState({
                    errorMessage: error,
                    successMessage: undefined,
                    usersList: undefined
                });
            } else {
                this.setState({
                    errorMessage: undefined,
                    usersList: result
                });
            }
        });
    }

    private renderDefault(): JSX.Element {
        return (
            <div className="container">
                <h2>Users</h2>
                {this.state.errorMessage !== undefined &&
                    this.state.errorMessage.trim().length !== 0 && (
                        <Alert bsStyle="danger">{this.state.errorMessage}</Alert>
                    )}
                {this.state.successMessage !== undefined &&
                    this.state.successMessage.trim().length !== 0 && (
                        <Alert bsStyle="success">{this.state.successMessage}</Alert>
                    )}
                {this.state.usersList !== undefined &&
                    this.state.usersList.users !== undefined &&
                    this.state.usersList.users.length > 0 && (
                        <Table striped={true} bordered={true} condensed={true} hover={true}>
                            <thead>
                                <tr>
                                    <th>Email</th>
                                    <th>Nickname</th>
                                    <th>Name</th>
                                    <th>Surname</th>
                                    <th>Role</th>
                                    <th>Enabled</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.usersList.users.map((item, i) => {
                                    return (
                                        <tr key={i}>
                                            <td>{item.email}</td>
                                            <td>{item.nickname}</td>
                                            <td>{item.name}</td>
                                            <td>{item.surname}</td>
                                            <td>{UserRole[item.role]}</td>
                                            <td>{item.enabled ? "true" : "false"}</td>
                                            <td>
                                                <Button
                                                    bsStyle={!item.enabled ? "primary" : "danger"}
                                                    onClick={() => this.switchEnabledStateOfUser(item.id)}
                                                    disabled={State.loggedUser.id === item.id}
                                                >
                                                    {!item.enabled ? "Enable user" : "Disable user"}
                                                </Button>
                                                <Button
                                                    disabled={State.loggedUser.id === item.id}
                                                    onClick={() => {
                                                        this.setState({
                                                            showModal: true,
                                                            selectedUserId: item.id,
                                                            selectedRole: item.role
                                                        });
                                                    }}
                                                >
                                                    Change role
                                                </Button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </Table>
                    )}
                <LoadingOverlay display={State.isLoading} />
                {this.state.showModal && (
                    <div className="static-modal">
                        <Modal.Dialog>
                            <Modal.Header>
                                <Modal.Title>Choose role</Modal.Title>
                            </Modal.Header>

                            <Modal.Body>
                                <FormGroup controlId="formControlsSelect">
                                    <ControlLabel>Role</ControlLabel>
                                    <FormControl
                                        componentClass="select"
                                        placeholder="choose role"
                                        onChange={event => {
                                            this.setState({
                                                selectedRole: (event.target as HTMLInputElement).value
                                            });
                                        }}
                                        value={this.state.selectedRole}
                                    >
                                        <option value={1}>User</option>
                                        <option value={2}>Editor</option>
                                        <option value={3}>Moderator</option>
                                        <option value={4}>Administrator</option>
                                    </FormControl>
                                </FormGroup>
                            </Modal.Body>

                            <Modal.Footer>
                                <Button
                                    onClick={() => {
                                        this.setState({ showModal: false });
                                    }}
                                >
                                    Close
                                </Button>
                                <Button
                                    bsStyle="primary"
                                    onClick={() => {
                                        if (
                                            this.state.selectedUserId !== undefined &&
                                            this.state.selectedRole !== undefined
                                        ) {
                                            this.changeRoleOfUser(this.state.selectedUserId, this.state.selectedRole);
                                        }
                                        this.setState({ showModal: false });
                                    }}
                                >
                                    Change role
                                </Button>
                            </Modal.Footer>
                        </Modal.Dialog>
                    </div>
                )}
            </div>
        );
    }

    private switchEnabledStateOfUser(id: number) {
        SwitchUserEnabledStateAction(id, error => {
            if (error !== undefined) {
                this.handleError(error);
                return;
            } 
            this.setState({ errorMessage: undefined, successMessage: "Enabled state was updated!" });
            this.loadUsers();
        });
    }

    private changeRoleOfUser(userId: number, role: number) {
        ChangeUserRoleAction(userId, role, error => {
            if (error !== undefined) {
                this.handleError(error);
                return;
            } 
            this.setState({ errorMessage: undefined, successMessage: "Role of user was changed!" });
            this.loadUsers();
        });
    }

    private handleError(error: string | object) {
        this.setState({
            errorMessage: error,
            successMessage: undefined,
            usersList: undefined
        });
    }
}
