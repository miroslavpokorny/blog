import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
// import { State } from '../BlogAdminStore';

interface GalleryPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<GalleryPageParams> {
}

export default class GalleryPage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <p>Gallery page</p>
                </div>
            </div>
        );
    }
}
