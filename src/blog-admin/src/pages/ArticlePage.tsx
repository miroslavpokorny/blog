import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { UserRole } from "../api/UserRole";
import { observer } from "mobx-react";
import WysiwygEditor from "../components/WysiwygEditor";
import { FormGroup, ControlLabel, FormControl, Button, Alert } from "react-bootstrap";
import { GalleryListDto, GetGalleryListAction } from "../api/GalleryControllerApi";
import { GetCategoriesListAction, CategoryListDto } from "../api/CategoryControllerApi";
import { Validation } from "../helpers/ValidationHelper";
import { State } from "../BlogAdminStore";
import { AddArticleAction, GetArticlesListAction } from "../api/ArticleControllerApi";
import { RouteName } from "../Router";
import FileUpload from "../components/FileUpload";
import { ImageFile } from "react-dropzone";

interface ArticlePageParams {
    action?: string;
    id?: number;
}

interface PageProps extends RouteComponentProps<ArticlePageParams> {}

@observer
export default class ArticlePage extends React.Component<PageProps> {
    state: {
        articleContent: string;
        articleGalleryId: number;
        articleCategoryId: number;
        articleName: string;
        errorMessage?: string;
        successMessage?: string;
        galleryList?: GalleryListDto;
        categoryList?: CategoryListDto;
        isArticleNameFilled: boolean;
        isArticleCategoryIdSelected: boolean;
        articlePreviewImage?: ImageFile;
    };

    private _isMounted: boolean = false;

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            successMessage: undefined,
            articleContent: "",
            articleGalleryId: 0,
            articleCategoryId: 0,
            isArticleCategoryIdSelected: false,
            articleName: "",
            isArticleNameFilled: false,
            galleryList: undefined,
            categoryList: undefined,
            articlePreviewImage: undefined
        };
        setTimeout(() => this.loadData(), 0);
    }

    componentDidMount() {
        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(UserRole.Editor, this.props.location.pathname, () => {
            return (
                <div>
                    <MainNavigation pathName={this.props.location.pathname} />
                    <div className="container">
                        {this.state.errorMessage !== undefined &&
                            this.state.errorMessage.trim().length !== 0 && (
                                <Alert bsStyle="danger">{this.state.errorMessage}</Alert>
                            )}
                        {this.state.successMessage !== undefined &&
                            this.state.successMessage.trim().length !== 0 && (
                                <Alert bsStyle="success">{this.state.successMessage}</Alert>
                            )}
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
        return (
            <div>
                <h2>Add article</h2>
                <FormGroup
                    controlId="formControlName"
                    validationState={
                        this.state.isArticleNameFilled
                            ? Validation.notEmpty(this.state.articleName).validate()
                            : undefined
                    }
                >
                    <ControlLabel>Name</ControlLabel>
                    <FormControl
                        type="text"
                        placeholder="Name"
                        onChange={event => {
                            this.setState({
                                articleName: (event.target as HTMLInputElement).value,
                                isArticleNameFilled: true
                            });
                        }}
                        value={this.state.articleName}
                    />
                </FormGroup>
                <FormGroup controlId="formControlsSelectPreviewImage">
                    <ControlLabel>Preview image</ControlLabel>
                    <FileUpload
                        mimeType="image/jpeg,image/png"
                        text="Drop preview of article image here"
                        multiple={false}
                        onDropAccepted={image => {
                            this.setState({ articlePreviewImage: image });
                        }}
                    />
                </FormGroup>
                <FormGroup controlId="formControlsSelectArticle">
                    <ControlLabel>Article</ControlLabel>
                    <WysiwygEditor
                        text={this.state.articleContent}
                        onChange={value => this.setState({ articleContent: value })}
                    />
                </FormGroup>
                <FormGroup
                    controlId="formControlsSelectCategory"
                    validationState={
                        this.state.isArticleCategoryIdSelected
                            ? Validation.graterThan(this.state.articleCategoryId, 0).validate()
                            : undefined
                    }
                >
                    <ControlLabel>Category</ControlLabel>
                    <FormControl
                        componentClass="select"
                        placeholder="choose role"
                        onChange={event => {
                            this.setState({
                                articleCategoryId: (event.target as HTMLInputElement).value,
                                isArticleCategoryIdSelected: true
                            });
                        }}
                        value={this.state.articleCategoryId}
                        disabled={
                            this.state.categoryList === undefined || this.state.categoryList.categories.length === 0
                        }
                    >
                        <option value={0} />
                        {this.state.categoryList !== undefined &&
                            this.state.categoryList.categories.map((item, i) => {
                                return (
                                    <option key={i} value={item.id}>
                                        {item.name}
                                    </option>
                                );
                            })}
                    </FormControl>
                </FormGroup>
                <FormGroup controlId="formControlsSelectGallery">
                    <ControlLabel>Gallery</ControlLabel>
                    <FormControl
                        componentClass="select"
                        placeholder="choose role"
                        onChange={event => {
                            this.setState({
                                articleGalleryId: (event.target as HTMLInputElement).value
                            });
                        }}
                        value={this.state.articleGalleryId}
                        disabled={this.state.galleryList === undefined || this.state.galleryList.galleries.length === 0}
                    >
                        <option value={0} />
                        {this.state.galleryList !== undefined &&
                            this.state.galleryList.galleries.map((item, i) => {
                                return (
                                    <option key={i} value={item.id}>
                                        {item.name} ({item.id})
                                    </option>
                                );
                            })}
                    </FormControl>
                </FormGroup>
                <Button
                    bsStyle="primary"
                    onClick={() => {
                        if (
                            Validation.notEmpty(this.state.articleName).isValid() &&
                            Validation.graterThan(this.state.articleCategoryId, 0).isValid() &&
                            this.state.articlePreviewImage !== undefined
                        ) {
                            this.createArticle(
                                this.state.articleName,
                                this.state.articleContent,
                                this.state.articleCategoryId,
                                this.state.articleGalleryId,
                                State.loggedUser.id as number,
                                this.state.articlePreviewImage
                            );
                        }
                    }}
                >
                    Create article
                </Button>
            </div>
        );
    }

    private loadData() {
        let runningActions = 1;
        const errors: Array<string | object> = [];
        GetGalleryListAction((error, result) => {
            runningActions--;
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                errors.push(error);
            } else {
                this.setState({ galleryList: result });
            }
            if (runningActions === 0) {
                this.handleLoadDataFinish(errors);
            }
        });
        runningActions++;
        GetCategoriesListAction((error, result) => {
            runningActions--;
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                errors.push(error);
            } else {
                this.setState({ categoryList: result });
            }
            if (runningActions === 0) {
                this.handleLoadDataFinish(errors);
            }
        });
        runningActions++;
        GetArticlesListAction((error, result) => {
            runningActions--;
        });
    }

    private handleLoadDataFinish(errors: Array<string | object>) {
        if (errors.length === 0) {
            return;
        }
        let errorMessage = "Errors ocurred during loading of data, page could not work properly: ";
        errors.forEach(error => {
            errorMessage += "<br/>" + error;
        });
        this.handleError(errorMessage);
    }

    private createArticle(
        name: string,
        content: string,
        categoryId: number,
        galleryId: number,
        authorId: number,
        previewImage: File
    ) {
        AddArticleAction(name, content, authorId, categoryId, galleryId, previewImage, error => {
            if (error !== undefined) {
                return this.handleError(error);
            }
            this.handleSuccess("Article was created!");
            State.mainNavigation.redirectLink = RouteName.article;
        });
    }

    private handleError(error: string | object) {
        this.setState({ errorMessage: error, successMessage: undefined });
    }

    private handleSuccess(message: string) {
        this.setState({ errorMessage: undefined, successMessage: message });
    }
}
