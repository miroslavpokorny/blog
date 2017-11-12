import * as React from 'react';
import { UserRole } from '../api/UserRole';
import { State } from '../BlogAdminStore';
import { Redirect } from 'react-router';
import { RouteName } from '../Router';

export default class PageHelper {
    static hasUserRightToAccessOrRedirect(
        minimalAccessRight: UserRole, 
        renderCallback: () => JSX.Element | JSX.Element[] | React.ReactPortal | string | number | null | false
    ): JSX.Element | JSX.Element[] | React.ReactPortal | string | number | null | false {
        if (State.userRole === undefined || State.userRole < minimalAccessRight) {
            return <Redirect push={true} to={RouteName.home} />;
        }
        return renderCallback();
    }

    static isUserSignedIn(): boolean {
        return State.userRole !== undefined;
    }
}
