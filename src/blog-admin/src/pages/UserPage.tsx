import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import { UserRole } from "../api/UserRole";
import PageHelper from "../helpers/PageHelper";
import { observer } from "mobx-react";
import {
    UsersListDto,
    GetUsersListAction,
    SwitchUserEnabledStateAction
} from "../api/UsersControllerApi";
import * as Alert from "react-bootstrap/lib/Alert";
import { Table } from "react-bootstrap";
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
    };

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            usersList: undefined
        };
        this.loadUsers();
    }

    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(
            UserRole.Administrator,
            this.props.location.pathname,
            () => {
                return (
                    <div>
                        <MainNavigation
                            pathName={this.props.location.pathname}
                        />
                        <div className="container">
                            {!this.props.match.params.action &&
                                this.renderDefault()}
                        </div>
                    </div>
                );
            }
        );
    }

    private loadUsers() {
        GetUsersListAction((error, result) => {
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
                        <Alert bsStyle="danger">
                            {this.state.errorMessage}
                        </Alert>
                    )}
                {this.state.successMessage !== undefined &&
                    this.state.successMessage.trim().length !== 0 && (
                        <Alert bsStyle="success">
                            {this.state.successMessage}
                        </Alert>
                    )}
                {this.state.usersList !== undefined &&
                    this.state.usersList.users !== undefined &&
                    this.state.usersList.users.length > 0 && (
                        <Table
                            striped={true}
                            bordered={true}
                            condensed={true}
                            hover={true}
                        >
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
                                            <td>
                                                {item.enabled
                                                    ? "true"
                                                    : "false"}
                                            </td>
                                            <td>
                                                <Button
                                                    bsStyle={
                                                        !item.enabled
                                                            ? "primary"
                                                            : "danger"
                                                    }
                                                    onClick={() =>
                                                        this.switchEnabledStateOfUser(
                                                            item.id
                                                        )
                                                    }
                                                >
                                                    {!item.enabled
                                                        ? "Enable user"
                                                        : "Disable user"}
                                                </Button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </Table>
                    )}
                <LoadingOverlay display={State.isLoading} />
            </div>
        );
    }

    private switchEnabledStateOfUser(id: number) {
        SwitchUserEnabledStateAction(id, error => {
            if (error !== undefined) {
                this.setState({
                    errorMessage: error,
                    successMessage: undefined,
                    usersList: undefined
                });
            } else {
                this.setState({ successMessage: "Enabled state was updated!" });
                this.loadUsers();
            }
        });
    }
}
