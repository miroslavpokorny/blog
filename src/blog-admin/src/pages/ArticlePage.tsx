import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { UserRole } from "../api/UserRole";
import { observer } from "mobx-react";
import WysiwygEditor from "../components/WysiwygEditor";
import { FormGroup, ControlLabel, FormControl, Button, Alert, Table, ButtonToolbar } from "react-bootstrap";
import { GalleryListDto, GetGalleryListAction } from "../api/GalleryControllerApi";
import { GetCategoriesListAction, CategoryListDto } from "../api/CategoryControllerApi";
import { Validation } from "../helpers/ValidationHelper";
import { State } from "../BlogAdminStore";
import {
    AddArticleAction,
    GetArticlesListAction,
    ArticleListDto,
    RemoveArticleAction,
    EditArticleAction
} from "../api/ArticleControllerApi";
import { RouteName } from "../Router";
import FileUpload from "../components/FileUpload";
import { ImageFile } from "react-dropzone";
import LoadingOverlay from "../components/LoadingOverlay";

interface ArticlePageParams {
    action?: string;
    id?: string;
}

interface PageProps extends RouteComponentProps<ArticlePageParams> {}

const previewImageStyle = {
    maxWidth: 150,
    maxHeight: 150
};

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
        articleList?: ArticleListDto;
        selectedArticleContent: string;
        selectedArticleGalleryId: number;
        selectedArticleCategoryId: number;
        selectedArticleName: string;
        selectedArticlePreviewImage: string | ImageFile;
        articleId?: number;
    };

    private _isMounted: boolean = false;

    private _runningActions: number = 0;

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
            articlePreviewImage: undefined,
            articleList: undefined,
            selectedArticleName: "",
            selectedArticleContent: "",
            selectedArticleCategoryId: 0,
            selectedArticleGalleryId: 0,
            selectedArticlePreviewImage: "",
            articleId: parseInt(props.match.params.id ? props.match.params.id : "", 10)
        };
        if (
            this.props.match.params.action === "edit" &&
            this.state.articleId !== undefined &&
            isNaN(this.state.articleId)
        ) {
            State.mainNavigation.redirectLink = RouteName.about;
            return;
        }
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
                        <LoadingOverlay display={State.isLoading || this._runningActions > 0} />
                    </div>
                </div>
            );
        });
    }

    private renderDefault(): JSX.Element | JSX.Element[] {
        return (
            <div>
                <h2>Articles</h2>
                <ButtonToolbar>
                    <Button
                        bsStyle="primary"
                        bsSize="large"
                        onClick={() => {
                            State.mainNavigation.redirectLink = RouteName.articleAdd;
                        }}
                    >
                        Add article
                    </Button>
                </ButtonToolbar>
                {(this.state.articleList === undefined || this.state.articleList.articles.length === 0) && (
                    <p>No data to display</p>
                )}
                {this.state.articleList !== undefined &&
                    this.state.articleList.articles !== undefined &&
                    this.state.articleList.articles.length > 0 && (
                        <Table striped={true} bordered={true} condensed={true} hover={true}>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Preview image</th>
                                    <th>Author</th>
                                    <th>Category</th>
                                    <th>Gallery</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.articleList.articles.map((item, i) => {
                                    return (
                                        <tr key={i}>
                                            <td>{item.name}</td>
                                            <td>
                                                <img
                                                    alt={item.previewImage}
                                                    src={`${State.endpoint}/images/${item.previewImage}`}
                                                    style={previewImageStyle}
                                                />
                                            </td>
                                            <td>{item.author.nickname}</td>
                                            <td>{item.categoryId}</td>
                                            <td>{item.galleryId}</td>
                                            <td>
                                                <Button
                                                    bsStyle="danger"
                                                    onClick={() => {
                                                        this.removeArticle(item.id);
                                                    }}
                                                >
                                                    Remove
                                                </Button>
                                                <Button
                                                    onClick={() => {
                                                        this.setState({
                                                            selectedArticleCategoryId: item.categoryId,
                                                            selectedArticleContent: item.content,
                                                            selectedArticleGalleryId:
                                                                item.galleryId !== undefined && item.galleryId !== null
                                                                    ? item.galleryId
                                                                    : 0,
                                                            selectedArticleName: item.name,
                                                            selectedArticlePreviewImage: item.previewImage,
                                                            articleId: item.id
                                                        });
                                                        State.mainNavigation.redirectId = item.id;
                                                        State.mainNavigation.redirectLink = RouteName.articleEdit;
                                                    }}
                                                >
                                                    Edit
                                                </Button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </Table>
                    )}
            </div>
        );
    }

    private renderEditArticle(): JSX.Element | JSX.Element[] {
        return (
            <div>
                <h2>Edit article</h2>
                <FormGroup
                    controlId="formControlName"
                    validationState={Validation.notEmpty(this.state.selectedArticleName).validate()}
                >
                    <ControlLabel>Name</ControlLabel>
                    <FormControl
                        type="text"
                        placeholder="Name"
                        onChange={event => {
                            this.setState({
                                selectedArticleName: (event.target as HTMLInputElement).value
                            });
                        }}
                        value={this.state.selectedArticleName}
                    />
                </FormGroup>
                <FormGroup controlId="formControlsSelectPreviewImage">
                    <ControlLabel>Preview image</ControlLabel>
                    <FileUpload
                        mimeType="image/jpeg,image/png"
                        text="Drop preview of article image here"
                        multiple={false}
                        onDropAccepted={images => {
                            if (images.length > 0) {
                                this.setState({ selectedArticlePreviewImage: images[0] });
                            }
                        }}
                    />
                    {typeof this.state.selectedArticlePreviewImage === "string" &&
                        this.state.selectedArticlePreviewImage.trim() !== "" && (
                            <div>
                                <h3>Preview</h3>
                                <img
                                    alt={this.state.selectedArticlePreviewImage}
                                    src={`${State.endpoint}/images/${this.state.selectedArticlePreviewImage}`}
                                    style={previewImageStyle}
                                />
                            </div>
                        )}
                </FormGroup>
                <FormGroup controlId="formControlsSelectArticle">
                    <ControlLabel>Article</ControlLabel>
                    <WysiwygEditor
                        text={this.state.selectedArticleContent}
                        onChange={value => this.setState({ selectedArticleContent: value })}
                    />
                </FormGroup>
                <FormGroup
                    controlId="formControlsSelectCategory"
                    validationState={Validation.graterThan(this.state.selectedArticleCategoryId, 0).validate()}
                >
                    <ControlLabel>Category</ControlLabel>
                    <FormControl
                        componentClass="select"
                        placeholder="choose role"
                        onChange={event => {
                            this.setState({
                                selectedArticleCategoryId: (event.target as HTMLInputElement).value
                            });
                        }}
                        value={this.state.selectedArticleCategoryId}
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
                                selectedArticleGalleryId: (event.target as HTMLInputElement).value
                            });
                        }}
                        value={this.state.selectedArticleGalleryId}
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
                            Validation.notEmpty(this.state.selectedArticleName).isValid() &&
                            Validation.graterThan(this.state.selectedArticleCategoryId, 0).isValid() &&
                            this.state.articleId !== undefined
                        ) {
                            this.editArticle(
                                this.state.articleId,
                                this.state.selectedArticleName,
                                this.state.selectedArticleContent,
                                this.state.selectedArticleCategoryId,
                                this.state.selectedArticleGalleryId,
                                typeof this.state.selectedArticlePreviewImage === "string"
                                    ? undefined
                                    : this.state.selectedArticlePreviewImage
                            );
                        }
                    }}
                >
                    Edit article
                </Button>
            </div>
        );
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
                        onDropAccepted={images => {
                            if (images.length > 0) {
                                this.setState({ articlePreviewImage: images[0] });
                            }
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
        const errors: Array<string | object> = [];
        this._runningActions++;
        GetGalleryListAction((error, result) => {
            this._runningActions--;
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                errors.push(error);
            } else {
                this.setState({ galleryList: result });
            }
            if (this._runningActions === 0) {
                this.handleLoadDataFinish(errors);
            }
        });
        this._runningActions++;
        GetCategoriesListAction((error, result) => {
            this._runningActions--;
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                errors.push(error);
            } else {
                this.setState({ categoryList: result });
            }
            if (this._runningActions === 0) {
                this.handleLoadDataFinish(errors);
            }
        });
        this._runningActions++;
        GetArticlesListAction((error, result) => {
            this._runningActions--;
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                errors.push(error);
            } else {
                this.setState({ articleList: result });
                if (result !== undefined && this.state.articleId !== undefined && !isNaN(this.state.articleId)) {
                    const selected = result.articles.filter(item => {
                        return item.id === this.state.articleId;
                    });
                    if (selected.length === 0) {
                        State.mainNavigation.redirectLink = RouteName.about;
                    } else {
                        this.setState({
                            selectedArticleCategoryId: selected[0].categoryId,
                            selectedArticleContent: selected[0].content,
                            selectedArticleGalleryId: selected[0].galleryId !== null ? selected[0].galleryId : 0,
                            selectedArticleName: selected[0].name,
                            selectedArticlePreviewImage: selected[0].previewImage
                        });
                    }
                }
            }
            if (this._runningActions === 0) {
                this.handleLoadDataFinish(errors);
            }
        });
    }

    private handleLoadDataFinish(errors: Array<string | object>) {
        if (errors.length === 0) {
            return;
        }
        let errorMessage = "Errors ocurred during loading of data, page will not work properly";
        errors.forEach(error => {
            errorMessage += "; " + error;
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
            this.setDefaultValuesForAdd();
            this.loadData();
            State.mainNavigation.redirectLink = RouteName.article;
        });
    }

    private removeArticle(id: number) {
        RemoveArticleAction(id, error => {
            if (error !== undefined) {
                return this.handleError(error);
            }
            this.handleSuccess("Article was removed!");
            this.loadData();
        });
    }

    private editArticle(
        articleId: number,
        name: string,
        content: string,
        categoryId: number,
        galleryId: number,
        previewImage: ImageFile | undefined
    ) {
        EditArticleAction(articleId, name, content, categoryId, galleryId, previewImage, error => {
            if (error !== undefined) {
                return this.handleError(error);
            }
            this.handleSuccess("Article was edited");
            this.loadData();
            State.mainNavigation.redirectLink = RouteName.article;
        });
    }

    private handleError(error: string | object) {
        this.setState({ errorMessage: error, successMessage: undefined });
    }

    private handleSuccess(message: string) {
        this.setState({ errorMessage: undefined, successMessage: message });
    }

    private setDefaultValuesForAdd() {
        this.setState({
            articleName: "",
            articleContent: "",
            articleCategoryId: 0,
            articleGalleryId: 0,
            articlePreviewImage: undefined,
            isArticleNameFilled: false,
            isArticleCategoryIdSelected: false
        });
    }
}
