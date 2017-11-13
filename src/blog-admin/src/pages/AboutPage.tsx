import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface AboutPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<AboutPageParams> {
}

export default class AboutPage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <p>About page</p>
                </div>
            </div>
        );
    }
}