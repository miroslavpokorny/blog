import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import { observer } from "mobx-react";
import { State } from "../BlogAdminStore";

interface AboutPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<AboutPageParams> {}

@observer
export default class AboutPage extends React.Component<PageProps> {
    render() {
        return (
            <div>
                <MainNavigation pathName={this.props.location.pathname} />
                <div className="container">
                    <h1>Blog administration</h1>
                    <p />
                    <a href={State.endpoint}>Go to blog home!</a>
                </div>
            </div>
        );
    }
}
