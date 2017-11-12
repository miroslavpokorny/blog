import * as React from 'react';
import { RouteComponentProps, Redirect } from 'react-router';
import PageHelper from '../helpers/PageHelper';
import { RouteName } from '../Router';
import { State } from '../BlogAdminStore';
import SignIn from '../components/SignIn';
import { observer } from 'mobx-react';

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
            case 'out':
            default:
                // TODO sing out API and remove state
                return <Redirect push={true} to={RouteName.home} />;
        }
    }

    private onSignInClick(): void {
        if (State.signInForm.email.trim().length !== 0 && State.signInForm.password.length !== 0) {
            // TODO call api
            State.isLoading = true;
            setTimeout(
                () => {
                    State.isLoading = false;
                    this.setState({ errorMessage: 'Incorrect'});
                }, 
                3000
            );
        }
    }
}
