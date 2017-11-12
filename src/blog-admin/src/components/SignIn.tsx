import * as React from 'react';
import { Link } from 'react-router-dom';
import { RouteName } from '../Router';
import * as Button from 'react-bootstrap/lib/Button';
import * as FormControl from 'react-bootstrap/lib/FormControl';
import * as Alert from 'react-bootstrap/lib/Alert';
import { State } from '../BlogAdminStore';
import { observer } from 'mobx-react';
import './SignIn.css';

export interface SignInProps {
    handleClick: () => void;
    errorMessage?: string;
}

@observer
class SignIn extends React.Component<SignInProps> {
    constructor(props: SignInProps) {
        super(props);
        State.signInForm.email = '';
        State.signInForm.password = '';
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
                    <label className="sr-only">Email address</label>
                    <FormControl 
                        type="email" 
                        value={State.signInForm.email} 
                        placeholder="Email address" 
                        onChange={(event) => this.handleEmailChange((event.target as HTMLInputElement).value)} />
                    <label className="sr-only">Password</label>
                    <FormControl
                        type="password"
                        value={State.signInForm.password}
                        placeholder="Password"
                        onChange={(event) => this.handlePasswordChange((event.target as HTMLInputElement).value)} />
                        <Link to={RouteName.signUp}>Sign up</Link>&nbsp;|&nbsp;
                        <Link to={RouteName.signPassword}>Restore password</Link>
                    <Button 
                        bsStyle="primary" 
                        bsSize="large" 
                        block={true}
                        disabled={State.isLoading}
                        onClick={State.isLoading ? undefined : () => this.props.handleClick()}>
                        {State.isLoading ? 'Signing in...' : 'Sign in'}
                    </Button>
                </form>
            </div>
        );
    }

    private handleEmailChange(value: string) {
        State.signInForm.email = value;
    }

    private handlePasswordChange(value: string) {
        State.signInForm.password = value;
    }
}

export default SignIn;
