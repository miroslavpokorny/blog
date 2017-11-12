import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface SignPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<SignPageParams> {
}

export default class SignPage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <p>Sign page</p>
                </div>
            </div>
        );
    }
}
