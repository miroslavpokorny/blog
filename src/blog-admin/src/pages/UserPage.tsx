import * as React from 'react';
import MainNavigation from '../components/MainNavigation';
import { RouteComponentProps } from 'react-router';
import { UserRole } from '../api/UserRole';
import PageHelper from '../helpers/PageHelper';
// import { State } from '../BlogAdminStore';

interface UserPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<UserPageParams> {
}

export default class UserPage extends React.Component<PageProps> {
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
                    </div>
                </div>
            );
        });
    }

    private renderDefault(): JSX.Element {
        return <div>Default</div>;
    }
}
