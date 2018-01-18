import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import { UserRole } from "../api/UserRole";
import PageHelper from "../helpers/PageHelper";
import { State } from "../BlogAdminStore";
import ProfileEdit from "../components/ProfileEdit";
import { Validation } from "../helpers/ValidationHelper";
import { EditProfileAction, ChangePasswordAction } from "../api/ProfileControllerApi";
import * as Alert from "react-bootstrap/lib/Alert";
import { observer } from "mobx-react";
import { FormGroup, Modal, ControlLabel, FormControl, Button, ButtonToolbar } from "react-bootstrap";

interface ProfilePageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<ProfilePageParams> {}

@observer
export default class ProfilePage extends React.Component<PageProps> {
    state: {
        errorMessage?: string;
        successMessage?: string;
        oldPassword: string;
        isOldPasswordFilled: boolean;
        newPassword: string;
        isNewPasswordFilled: boolean;
        confirmNewPassword: string;
        isConfirmNewPasswordFilled: boolean;
        showModalChangePassword: boolean;
    };

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            successMessage: undefined,
            oldPassword: "",
            newPassword: "",
            confirmNewPassword: "",
            showModalChangePassword: false,
            isNewPasswordFilled: false,
            isOldPasswordFilled: false,
            isConfirmNewPasswordFilled: false
        };
    }

    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(UserRole.User, this.props.location.pathname, () => {
            return (
                <div>
                    <MainNavigation pathName={this.props.location.pathname} />
                    <div className="container">
                        {!this.props.match.params.action && this.renderDefault()}
                        {this.props.match.params.action === "edit" && this.renderEditProfile()}
                    </div>
                </div>
            );
        });
    }

    private renderDefault(): JSX.Element {
        return <ProfileEdit heading="Profile" />;
    }

    private renderEditProfile(): JSX.Element | JSX.Element[] | string | number | null | false {
        return (
            <div>
                <ButtonToolbar>
                    <Button
                        bsStyle="primary"
                        bsSize="large"
                        onClick={() => {
                            this.setState({ showModalChangePassword: true });
                        }}
                    >
                        Change password
                    </Button>
                </ButtonToolbar>
                {this.state.errorMessage !== undefined &&
                    this.state.errorMessage.trim().length !== 0 && (
                        <Alert bsStyle="danger">{this.state.errorMessage}</Alert>
                    )}
                {this.state.successMessage !== undefined &&
                    this.state.successMessage.trim().length !== 0 && (
                        <Alert bsStyle="success">{this.state.successMessage}</Alert>
                    )}
                <ProfileEdit
                    heading="Edit Profile"
                    handleClickEdit={(nickname, name, surname) => this.handleEditProfile(nickname, name, surname)}
                />
                {this.state.showModalChangePassword && this.renderChangePassword()}
            </div>
        );
    }

    private renderChangePassword(): JSX.Element {
        return (
            <div>
                <Modal.Dialog>
                    <Modal.Header>
                        <Modal.Title>Change password</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <FormGroup
                            controlId="formOriginalPassword"
                            validationState={
                                this.state.isOldPasswordFilled
                                    ? Validation.notEmpty(this.state.oldPassword).validate()
                                    : undefined
                            }
                        >
                            <ControlLabel>Original password</ControlLabel>
                            <FormControl
                                type="password"
                                placeholder="Name"
                                onChange={event => {
                                    this.setState({
                                        oldPassword: (event.target as HTMLInputElement).value,
                                        isOldPasswordFilled: true
                                    });
                                }}
                                value={this.state.oldPassword}
                            />
                        </FormGroup>
                        <FormGroup
                            controlId="formOriginalPassword"
                            validationState={
                                this.state.isNewPasswordFilled
                                    ? Validation.notEmpty(this.state.newPassword).validate()
                                    : undefined
                            }
                        >
                            <ControlLabel>New password</ControlLabel>
                            <FormControl
                                type="password"
                                placeholder="Name"
                                onChange={event => {
                                    this.setState({
                                        newPassword: (event.target as HTMLInputElement).value,
                                        isNewPasswordFilled: true
                                    });
                                }}
                                value={this.state.newPassword}
                            />
                        </FormGroup>
                        <FormGroup
                            controlId="formOriginalPassword"
                            validationState={
                                this.state.isConfirmNewPasswordFilled
                                    ? Validation.notEmpty(this.state.confirmNewPassword)
                                          .sameAs(this.state.newPassword)
                                          .validate()
                                    : undefined
                            }
                        >
                            <ControlLabel>Confirm new password</ControlLabel>
                            <FormControl
                                type="password"
                                placeholder="Name"
                                onChange={event => {
                                    this.setState({
                                        confirmNewPassword: (event.target as HTMLInputElement).value,
                                        isConfirmNewPasswordFilled: true
                                    });
                                }}
                                value={this.state.confirmNewPassword}
                            />
                        </FormGroup>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={() => this.clearPasswordState()}>Cancel</Button>
                        <Button
                            bsStyle="primary"
                            onClick={() => {
                                if (
                                    Validation.notEmpty(this.state.oldPassword).isValid() &&
                                    Validation.notEmpty(this.state.newPassword).isValid() &&
                                    Validation.notEmpty(this.state.confirmNewPassword)
                                        .sameAs(this.state.newPassword)
                                        .isValid() &&
                                    State.loggedUser.id !== undefined
                                ) {
                                    this.changePassword(
                                        State.loggedUser.id,
                                        this.state.oldPassword,
                                        this.state.newPassword
                                    );
                                    this.clearPasswordState();
                                }
                            }}
                        >
                            Change password
                        </Button>
                    </Modal.Footer>
                </Modal.Dialog>
            </div>
        );
    }

    private changePassword(userId: number, oldPassword: string, newPassword: string) {
        ChangePasswordAction(userId, oldPassword, newPassword, error => {
            if (error !== undefined) {
                this.setState({ successMessage: undefined, errorMessage: error });
            } else {
                this.setState({ successMessage: "Password was successfully changed!", errorMessage: undefined });
            }
        });
    }

    private clearPasswordState() {
        this.setState({
            showModalChangePassword: false,
            oldPassword: "",
            newPassword: "",
            confirmNewPassword: "",
            isNewPasswordFilled: false,
            isConfirmNewPasswordFilled: false,
            isOldPasswordFilled: false
        });
    }

    private handleEditProfile(nickname: string, name: string, surname: string) {
        if (!Validation.notEmpty(nickname).isValid() || State.loggedUser.id === undefined) {
            return;
        }
        EditProfileAction(State.loggedUser.id, nickname, name, surname, error => {
            if (error !== undefined) {
                this.setState({ errorMessage: error, successMessage: undefined });
            } else {
                this.setState({ successMessage: "Successfully edited!", errorMessage: undefined });
            }
        });
    }
}
