import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface ArticlePageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<ArticlePageParams> {
}

export default class ArticlePage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <p>Article page</p>
                </div>
            </div>
        );
    }
}
