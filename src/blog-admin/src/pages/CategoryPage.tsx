import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
import PageHelper from '../helpers/PageHelper';
import { UserRole } from '../api/UserRole';
// import { State } from '../BlogAdminStore';

interface CategoryPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<CategoryPageParams> {
}

export default class CategoryPage extends React.Component<PageProps> {
    constructor(props: PageProps) {
        super(props);
        
    }
    
    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(UserRole.User, this.props.location.pathname, () => { 
            return (
                <div>
                    <MainNavigation pathName={this.props.location.pathname} />
                    <div className="container">
                        {!this.props.match.params.action && this.renderDefault()}
                        {this.props.match.params.action === 'add' && this.renderAddCategory()}
                    </div>
                </div>
            );
        });
    }

    private renderDefault(): JSX.Element {
        // TODO implement
        return <div>Default</div>;
    }

    private renderAddCategory(): JSX.Element {
        // TODO implement
        return (
            <div>
                {this.renderDefault()}
                Add modal should be here
            </div>
        );
    }
}
