import * as React from "react";
import { UserRole } from "../api/UserRole";
import { State } from "../BlogAdminStore";
import { Redirect } from "react-router";
import { RouteName } from "../Router";

export default class PageHelper {
    static hasUserRightToAccessOrRedirect(
        minimalAccessRight: UserRole,
        currentPath: string,
        renderCallback: () => JSX.Element | JSX.Element[] | React.ReactPortal | string | number | null | false
    ): JSX.Element | JSX.Element[] | React.ReactPortal | string | number | null | false {
        if (State.loggedUser.role === undefined || State.loggedUser.role < minimalAccessRight) {
            if (State.mainNavigation.redirectAfterSignIn === undefined) {
                State.mainNavigation.redirectAfterSignIn = currentPath;
            }
            return <Redirect push={true} to={RouteName.home} />;
        }
        return renderCallback();
    }

    static isUserSignedIn(): boolean {
        return State.loggedUser.role !== undefined;
    }
}
