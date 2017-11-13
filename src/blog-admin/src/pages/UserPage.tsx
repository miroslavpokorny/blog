import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface UserPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<UserPageParams> {
}

export default class UserPage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <p>User page</p>
                </div>
            </div>
        );
    }
}