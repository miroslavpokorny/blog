import * as React from 'react';
import { Link } from 'react-router-dom';
import { RouteName } from '../Router';
import * as Button from 'react-bootstrap/lib/Button';
import * as FormControl from 'react-bootstrap/lib/FormControl';
import * as Alert from 'react-bootstrap/lib/Alert';
import * as FormGroup from 'react-bootstrap/lib/FormGroup';
import { State } from '../BlogAdminStore';
import { observer } from 'mobx-react';
import './SignUp.css';
import { Validation } from '../helpers/ValidationHelper';

export interface SignUpProps {
    handleSignUp: (
        email: string, 
        nickname: string, 
        password: string, 
        confirmPassword: string, 
        name: string, 
        surname: string) => void;
    errorMessage?: string;
}

@observer
class SignUp extends React.Component<SignUpProps> {
    state: {
        emailFilled: boolean;
        nicknameFilled: boolean;
        passwordFilled: boolean;
        confirmPasswordFilled: boolean;
        nameFilled: boolean;
        surnameFilled: boolean;
        email: string;
        nickname: string;
        password: string;
        confirmPassword: string;
        name: string;
        surname: string;
    };
    
    constructor(props: SignUpProps) {
        super(props);
        this.state = {
            emailFilled: false,
            nicknameFilled: false,
            passwordFilled: false,
            confirmPasswordFilled: false,
            nameFilled: false,
            surnameFilled: false,
            email: '',
            nickname: '',
            password: '',
            confirmPassword: '',
            name: '',
            surname: ''
        };
    }
    
    render() {
        return (
            <div className="container">
                <form className="form-signup">
                    <h2 className="form-signup-heading">Please sign up</h2>
                    {this.props.errorMessage !== undefined && this.props.errorMessage.trim().length !== 0 &&
                        <Alert bsStyle="danger">
                            {this.props.errorMessage}
                        </Alert>
                    }
                    <FormGroup validationState={this.state.emailFilled 
                        ? Validation.notEmpty(this.state.email).email().toString() 
                        : undefined} >
                        <label className="sr-only">Email address</label>
                        <FormControl 
                            type="email" 
                            value={this.state.email} 
                            placeholder="Email address" 
                            onChange={(event) => this.handleEmailChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.callHandleSignUp(); }}} />
                    </FormGroup>
                    <FormGroup validationState={this.state.nicknameFilled 
                        ? Validation.notEmpty(this.state.nickname).toString() 
                        : undefined} >
                        <label className="sr-only">Nickname</label>
                        <FormControl 
                            type="text" 
                            value={this.state.nickname} 
                            placeholder="Nickname" 
                            onChange={(event) => this.handleNicknameChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.callHandleSignUp(); }}} />
                    </FormGroup>
                    <FormGroup validationState={this.state.passwordFilled
                        ? Validation.notEmpty(this.state.password).toString()
                        : undefined} >
                        <label className="sr-only">Password</label>
                        <FormControl
                            type="password"
                            value={this.state.password}
                            placeholder="Password"
                            onChange={(event) => this.handlePasswordChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.callHandleSignUp(); }}} />
                    </FormGroup>
                    <FormGroup validationState={this.state.confirmPasswordFilled
                        ? Validation.notEmpty(this.state.confirmPassword).sameAs(this.state.password).toString()
                        : undefined} >
                        <label className="sr-only">Confirm password</label>
                        <FormControl
                            type="password"
                            value={this.state.confirmPassword}
                            placeholder="Confirm password"
                            onChange={(event) => 
                                this.handleConfirmPasswordChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.callHandleSignUp(); }}} />
                    </FormGroup>
                    <FormGroup>
                        <label className="sr-only">Name</label>
                        <FormControl 
                            type="text" 
                            value={this.state.name} 
                            placeholder="Name" 
                            onChange={(event) => this.handleNameChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.callHandleSignUp(); }}} />
                    </FormGroup>
                    <FormGroup>
                        <label className="sr-only">Surname</label>
                        <FormControl 
                            type="text" 
                            value={this.state.surname} 
                            placeholder="Name" 
                            onChange={(event) => this.handleSurnameChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.callHandleSignUp(); }}} />
                    </FormGroup>
                        Already have account? <Link to={RouteName.signIn}>Sign in</Link>
                    <Button 
                        bsStyle="primary" 
                        bsSize="large" 
                        block={true}
                        onClick={State.isLoading ? undefined : () => { this.callHandleSignUp(); }}>
                        Sign up
                    </Button>
                </form>
            </div>
        );
    }

    private handleEmailChange(value: string) {
        this.setState({ email: value, emailFilled: true });
    }

    private handlePasswordChange(value: string) {
        this.setState({ password: value, passwordFilled: true });
    }

    private handleNicknameChange(value: string) {
        this.setState({ nickname: value, nicknameFilled: true });
    }

    private handleConfirmPasswordChange(value: string) {
        this.setState({ confirmPassword: value, confirmPasswordFilled: true });
    }

    private handleNameChange(value: string) {
        this.setState({ name: value, nameFilled: true });
    }

    private handleSurnameChange(value: string) {
        this.setState({ surname: value, surnameFilled: true });
    }

    private callHandleSignUp() {
        this.props.handleSignUp(
            this.state.email, 
            this.state.nickname,
            this.state.password, 
            this.state.confirmPassword, 
            this.state.name, 
            this.state.surname
        ); 
    }
}

export default SignUp;
