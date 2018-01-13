import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { UserRole } from "../api/UserRole";
import { observer } from "mobx-react";
import WysiwygEditor from "../components/WysiwygEditor";
// import { State } from '../BlogAdminStore';

interface ArticlePageParams {
    action?: string;
    id?: number;
}

interface PageProps extends RouteComponentProps<ArticlePageParams> {}

@observer
export default class ArticlePage extends React.Component<PageProps> {
    state: {
        articleContent: string;
        errorMessage?: string;
        successMessage?: string;
    };

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            successMessage: undefined,
            articleContent: ""
        };
    }

    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(UserRole.Editor, this.props.location.pathname, () => {
            return (
                <div>
                    <MainNavigation pathName={this.props.location.pathname} />
                    <div className="container">
                        {!this.props.match.params.action && this.renderDefault()}
                        {this.props.match.params.action === "edit" && this.renderEditArticle()}
                        {this.props.match.params.action === "add" && this.renderAddArticle()}
                    </div>
                </div>
            );
        });
    }

    private renderDefault(): JSX.Element | JSX.Element[] {
        // TODO implement
        return <div>Default</div>;
    }

    private renderEditArticle(): JSX.Element | JSX.Element[] {
        // TODO implement
        return <div>Edit article</div>;
    }

    private renderAddArticle(): JSX.Element | JSX.Element[] {
        // TODO implement
        return (
            <div>
                <h2>Add article</h2>
                <WysiwygEditor
                    text={this.state.articleContent}
                    onChange={value => this.setState({ articleContent: value })}
                />
            </div>
        );
    }
}
