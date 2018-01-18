import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps, Redirect } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { State } from "../BlogAdminStore";
import { RouteName } from "../Router";
import { observer } from "mobx-react";

interface HomePageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<HomePageParams> {}

@observer
export default class HomePage extends React.Component<PageProps> {
    render() {
        if (!PageHelper.isUserSignedIn()) {
            return <Redirect to={RouteName.signIn} />;
        }
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <h1>Welcome to blog administration!</h1>
                    <p>
                        You are signed as <strong>{State.loggedUser.nickname}</strong>
                    </p>
                    <a href={State.endpoint}>Go to blog home!</a>
                </div>
            </div>
        );
    }
}
