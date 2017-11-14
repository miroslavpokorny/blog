import * as React from 'react';
import { Link } from 'react-router-dom';
import { RouteName } from '../Router';
import * as Button from 'react-bootstrap/lib/Button';
import * as FormControl from 'react-bootstrap/lib/FormControl';
import * as Alert from 'react-bootstrap/lib/Alert';
import * as FormGroup from 'react-bootstrap/lib/FormGroup';
import { State } from '../BlogAdminStore';
import { observer } from 'mobx-react';
import './SignIn.css';
import { Validation } from '../helpers/ValidationHelper';

export interface SignInProps {
    handleClick: () => void;
    errorMessage?: string;
}

@observer
class SignIn extends React.Component<SignInProps> {
    state: {
        emailFilled: boolean;
        passwordFilled: boolean;
    };
    
    constructor(props: SignInProps) {
        super(props);
        State.signInForm.email = '';
        State.signInForm.password = '';
        this.state = {
            emailFilled: false,
            passwordFilled: false
        };
    }
    
    render() {
        return (
            <div className="container">
                <form className="form-signin">
                    <h2 className="form-signin-heading">Please sign in</h2>
                    {this.props.errorMessage !== undefined && this.props.errorMessage.trim().length !== 0 &&
                        <Alert bsStyle="danger">
                            {this.props.errorMessage}
                        </Alert>
                    }
                    <FormGroup className="one-px-margin-bottom" validationState={this.state.emailFilled 
                        ? Validation.notEmpty(State.signInForm.email).email().toString() 
                        : undefined} >
                        <label className="sr-only">Email address</label>
                        <FormControl 
                            type="email" 
                            value={State.signInForm.email} 
                            placeholder="Email address" 
                            onChange={(event) => this.handleEmailChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.props.handleClick(); }}} />
                    </FormGroup>
                    <FormGroup validationState={this.state.passwordFilled
                        ? Validation.notEmpty(State.signInForm.password).toString()
                        : undefined} >
                        <label className="sr-only">Password</label>
                        <FormControl
                            type="password"
                            value={State.signInForm.password}
                            placeholder="Password"
                            onChange={(event) => this.handlePasswordChange((event.target as HTMLInputElement).value)}
                            onKeyUp={(event) => {if (event.keyCode === 13) { this.props.handleClick(); }}} />
                    </FormGroup>
                        <Link to={RouteName.signUp}>Sign up</Link>&nbsp;|&nbsp;
                        <Link to={RouteName.signPassword}>Restore password</Link>
                    <Button 
                        bsStyle="primary" 
                        bsSize="large" 
                        block={true}
                        disabled={State.isLoading}
                        onClick={State.isLoading ? undefined : () => this.props.handleClick()}>
                        Sign in
                    </Button>
                </form>
            </div>
        );
    }

    private handleEmailChange(value: string) {
        State.signInForm.email = value;
        this.state.emailFilled = true;
    }

    private handlePasswordChange(value: string) {
        State.signInForm.password = value;
        this.state.passwordFilled = true;
    }
}

export default SignIn;
