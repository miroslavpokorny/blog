import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
import { UserRole } from '../api/UserRole';
import PageHelper from '../helpers/PageHelper';
import { State } from '../BlogAdminStore';
import ProfileEdit from '../components/ProfileEdit';
import { Validation } from '../helpers/ValidationHelper';
import { EditProfileAction } from '../api/ProfileControllerApi';
import * as Alert from 'react-bootstrap/lib/Alert';

interface ProfilePageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<ProfilePageParams> {
}

export default class ProfilePage extends React.Component<PageProps> {
    state: {
        errorMessage?: string;
        successMessage?: string;
    };

    constructor(props: PageProps) {
        super(props);
        this.state = { 
            errorMessage: undefined
        };
    }
    
    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(UserRole.User, () => {
            return (
                <div>
                    <MainNavigation pathName={this.props.location.pathname} />
                    <div className="container">
                        {!this.props.match.params.action && this.renderDefault()}
                        {this.props.match.params.action === 'edit' && this.renderEditProfile()}
                        {this.props.match.params.action === 'uploadAvatar' && this.renderUploadAvatar()}
                        {this.props.match.params.action === 'changePassword' && this.renderChangePassword()}
                    </div>
                </div>
            );
        });
    }

    private renderDefault(): JSX.Element {
        return <ProfileEdit heading="Profile"/>;
    }

    private renderEditProfile(): JSX.Element | JSX.Element[] | string | number | null | false {
        return (
            <div>
                {this.state.errorMessage !== undefined && this.state.errorMessage.trim().length !== 0 &&
                    <Alert bsStyle="danger">
                        {this.state.errorMessage}
                    </Alert>
                } 
                {this.state.successMessage !== undefined && this.state.successMessage.trim().length !== 0 &&
                    <Alert bsStyle="success">
                        {this.state.successMessage}
                    </Alert>
                }
                <ProfileEdit 
                    heading="Edit Profile" 
                    handleClickEdit={(nickname, name, surname) => this.handleEditProfile(nickname, name, surname)} />
            </div>
        );
    }

    private renderUploadAvatar(): JSX.Element {
        return (<div>Upload avatar</div>);
    }

    private renderChangePassword(): JSX.Element {
        return (<div>Change password</div>);
    }

    private handleEditProfile(nickname: string, name: string, surname: string) {
        if (!Validation.notEmpty(nickname).isValid() || State.loggedUser.id === undefined) {
            return;
        }
        EditProfileAction(State.loggedUser.id, nickname, name, surname, (error) => {
            if (error !== undefined) {
                this.setState({ errorMessage: error, successMessage: undefined });
            } else {
                this.setState({ successMessage: 'Successfully edited!', errorMessage: undefined });
            }
        });
    }
}
