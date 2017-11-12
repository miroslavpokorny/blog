import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface ProfilePageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<ProfilePageParams> {
}

export default class ProfilePage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    
                    <p>Profile page. Action: {this.props.match.params.action} </p>
                </div>
            </div>
        );
    }
}
