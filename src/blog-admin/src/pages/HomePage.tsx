import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface HomePageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<HomePageParams> {
}

export default class HomePage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <p>Home page</p>
                </div>
            </div>
        );
    }
}