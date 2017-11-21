import * as React from 'react';
import * as Button from 'react-bootstrap/lib/Button';
import * as FormControl from 'react-bootstrap/lib/FormControl';
import * as Alert from 'react-bootstrap/lib/Alert';
import * as FormGroup from 'react-bootstrap/lib/FormGroup';
import { observer } from 'mobx-react';
import './SignIn.css';
import { Validation } from '../helpers/ValidationHelper';
import LoadingOverlay from '../components/LoadingOverlay';
import { State } from '../BlogAdminStore';
import { ProfileInfo, LoadProfileAction } from '../api/ProfileControllerApi';

export interface ProfileEditProps {
    handleClickEdit?: (nickname: string, name: string, surname: string) => void;
    heading: string;
}

@observer
class ProfileEdit extends React.Component<ProfileEditProps> {
    state: {
        nickname: string;
        name: string;
        surname: string;
        errorMessage?: string;
    };
    
    constructor(props: ProfileEditProps) {
        super(props);
        this.state = {
            name: '',
            nickname: '',
            surname: ''
        };
        if (State.loggedUser.id !== undefined) {
            LoadProfileAction(State.loggedUser.id, (error?: string | object, result?: ProfileInfo) => {
                if (error !== undefined) {
                    this.setState({ errorMessage: error });
                }
                if (result !== undefined) {
                    this.setState({
                        name: result.name,
                        nickname: result.nickname,
                        surname: result.surname
                    });
                }
            });
        }
    }
    
    render() {
        return (
            <div className="container">
                <form>
                    <h2>{this.props.heading}</h2>
                    {this.state.errorMessage !== undefined && this.state.errorMessage.trim().length !== 0 &&
                        <Alert bsStyle="danger">
                            {this.state.errorMessage}
                        </Alert>
                    }
                    <FormGroup validationState={Validation.notEmpty(this.state.nickname).toString()}>
                        <label>Nickname</label>
                        <FormControl 
                            type="text" 
                            value={this.state.nickname} 
                            placeholder="Nickname" 
                            disabled={this.props.handleClickEdit === undefined}
                            onChange={(event) => this.handleNicknameChange((event.target as HTMLInputElement).value)}/>
                    </FormGroup>
                    <FormGroup>
                        <label>Name</label>
                        <FormControl 
                            type="text" 
                            value={this.state.name} 
                            placeholder="Name" 
                            disabled={this.props.handleClickEdit === undefined}
                            onChange={(event) => this.handleNameChange((event.target as HTMLInputElement).value)}/>
                    </FormGroup>
                    <FormGroup>
                        <label>Surname</label>
                        <FormControl 
                            type="text" 
                            value={this.state.surname} 
                            placeholder="Surname" 
                            disabled={this.props.handleClickEdit === undefined}
                            onChange={(event) => this.handleSurnameChange((event.target as HTMLInputElement).value)}/>
                    </FormGroup>
                    {this.props.handleClickEdit &&
                        <Button 
                            bsStyle="primary" 
                            bsSize="large" 
                            block={true}
                            onClick={() => {
                                if (this.props.handleClickEdit !== undefined) {
                                    this.props.handleClickEdit(
                                        this.state.nickname, 
                                        this.state.name, 
                                        this.state.surname
                                    ); 
                                }   
                            }}>
                            Edit profile
                        </Button>
                    }
                </form>
                <LoadingOverlay display={State.isLoading}/>
            </div>
        );
    }

    private handleNameChange(value: string) {
        this.setState({ name: value });
    }

    private handleNicknameChange(value: string) {
        this.setState({ nickname: value });
    }

    private handleSurnameChange(value: string) {
        this.setState({ surname: value });
    }
}

export default ProfileEdit;
