import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
import PageHelper from '../helpers/PageHelper';
import { State } from '../BlogAdminStore';
import { RouteName } from '../Router';
import { Link } from 'react-router-dom';

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
                    <h1>Welcome to blog administration!</h1>
                    {PageHelper.isUserSignedIn() ? (
                        <p>You are signed as <strong>{State.profile.nickname}</strong></p>
                    ) : (
                        <p>Click <Link to={RouteName.signIn}>here to sign in</Link></p>
                    )}
                </div>
            </div>
        );
    }
}