import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface CategoryPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<CategoryPageParams> {
}

export default class CategoryPage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <p>Category page</p>
                </div>
            </div>
        );
    }
}
