import * as React from 'react';
import { RouteComponentProps, Redirect } from 'react-router';
import PageHelper from '../helpers/PageHelper';
import { RouteName } from '../Router';
import { State } from '../BlogAdminStore';
import SignIn from '../components/SignIn';
import { observer } from 'mobx-react';
import { SignInAction } from '../api/SignControllerApi';

interface SignPageParams {
    action: string;
}

interface PageProps extends RouteComponentProps<SignPageParams> {
}

@observer
export default class SignPage extends React.Component<PageProps> {
    state: {
        errorMessage?: string;
    };

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined
        };
    }
    
    render() {
        var cookieValue = document.cookie.replace(/(?:(?:^|.*;\s*)tokenId\s*\=\s*([^;]*).*$)|^.*$/, '$1');
        console.log(cookieValue);
        switch (this.props.match.params.action) {
            case 'in':
                if (PageHelper.isUserSignedIn()) {
                    return <Redirect push={true} to={RouteName.home} />;
                }
                return <SignIn handleClick={() => this.onSignInClick()} errorMessage={this.state.errorMessage}/>;
            case 'up':
                
                return false;
            case 'password':

                return false;
            default:
                return <Redirect push={true} to={RouteName.signIn} />;
        }
    }

    private onSignInClick(): void {
        if (State.signInForm.email.trim().length !== 0 && State.signInForm.password.length !== 0) {
            SignInAction(State.signInForm.email, State.signInForm.password, (error) => {
                if (error !== undefined) {
                    return this.setState({ errorMessage: error });
                }
            });
        }
    }
}
