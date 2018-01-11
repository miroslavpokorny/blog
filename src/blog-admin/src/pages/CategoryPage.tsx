import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { UserRole } from "../api/UserRole";
import {
    CategoryListDto,
    GetCategoriesListAction,
    AddCategoryAction,
    RemoveCategoryAction,
    EditCategoryAction
} from "../api/CategoryControllerApi";
import { Alert, Table, Button, Modal, FormGroup, ControlLabel, FormControl, ButtonToolbar } from "react-bootstrap";
import { State } from "../BlogAdminStore";
import LoadingOverlay from "../components/LoadingOverlay";
import { Validation } from "../helpers/ValidationHelper";
import { RouteName } from "../Router";
import { observer } from "mobx-react";

interface CategoryPageParams {
    action?: string;
}

interface PageProps extends RouteComponentProps<CategoryPageParams> {}

@observer
export default class CategoryPage extends React.Component<PageProps> {
    state: {
        categoriesList?: CategoryListDto;
        errorMessage?: string;
        successMessage?: string;
        showModalEdit: boolean;
        selectedCategoryId?: number;
        selectedCategoryName?: string;
        selectedCategoryDescription?: string;
        addCategoryName: string;
        addCategoryDescription: string;
    };

    private _isMounted: boolean = false;

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            categoriesList: undefined,
            successMessage: undefined,
            showModalEdit: false,
            selectedCategoryId: undefined,
            selectedCategoryName: undefined,
            selectedCategoryDescription: undefined,
            addCategoryName: "",
            addCategoryDescription: ""
        };
        setTimeout(() => this.loadCategories(), 0);
    }

    componentDidMount() {
        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    render() {
        return PageHelper.hasUserRightToAccessOrRedirect(UserRole.Moderator, this.props.location.pathname, () => {
            return (
                <div>
                    <MainNavigation pathName={this.props.location.pathname} />
                    <div className="container">
                        {!this.props.match.params.action && this.renderDefault()}
                        {this.props.match.params.action === "add" && this.renderAddCategory()}
                    </div>
                </div>
            );
        });
    }

    private renderDefault(): JSX.Element {
        return (
            <div className="container">
                <h2>Categories</h2>
                <ButtonToolbar>
                    <Button
                        bsStyle="primary"
                        bsSize="large"
                        onClick={() => {
                            State.mainNavigation.redirectLink = RouteName.categoryAdd;
                        }}
                    >
                        Add category
                    </Button>
                </ButtonToolbar>
                {this.state.errorMessage !== undefined &&
                    this.state.errorMessage.trim().length !== 0 && (
                        <Alert bsStyle="danger">{this.state.errorMessage}</Alert>
                    )}
                {this.state.successMessage !== undefined &&
                    this.state.successMessage.trim().length !== 0 && (
                        <Alert bsStyle="success">{this.state.successMessage}</Alert>
                    )}
                {(this.state.categoriesList === undefined ||
                    this.state.categoriesList.categories === undefined ||
                    this.state.categoriesList.categories.length === 0) && <p>No data to display</p>}
                {this.state.categoriesList !== undefined &&
                    this.state.categoriesList.categories !== undefined &&
                    this.state.categoriesList.categories.length > 0 && (
                        <Table striped={true} bordered={true} condensed={true} hover={true}>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.categoriesList.categories.map((item, i) => {
                                    return (
                                        <tr key={i}>
                                            <td>{item.name}</td>
                                            <td>{item.description}</td>
                                            <td>
                                                <Button bsStyle="danger" onClick={() => this.removeCategory(item.id)}>
                                                    Remove
                                                </Button>
                                                <Button
                                                    onClick={() => {
                                                        this.setState({
                                                            showModalEdit: true,
                                                            selectedCategoryId: item.id,
                                                            selectedCategoryName: item.name,
                                                            selectedCategoryDescription: item.description
                                                        });
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
                <LoadingOverlay display={State.isLoading} />
                {this.state.showModalEdit && (
                    <div className="static-modal">
                        <Modal.Dialog>
                            <Modal.Header>
                                <Modal.Title>Edit category</Modal.Title>
                            </Modal.Header>

                            <Modal.Body>
                                <FormGroup
                                    controlId="formControlName"
                                    validationState={Validation.notEmpty(
                                        this.state.selectedCategoryName !== undefined
                                            ? this.state.selectedCategoryName
                                            : ""
                                    ).toString()}
                                >
                                    <ControlLabel>Name</ControlLabel>
                                    <FormControl
                                        type="text"
                                        placeholder="Name"
                                        onChange={event => {
                                            this.setState({
                                                selectedCategoryName: (event.target as HTMLInputElement).value
                                            });
                                        }}
                                        value={this.state.selectedCategoryName}
                                    />
                                </FormGroup>
                                <FormGroup controlId="formControlDescription">
                                    <ControlLabel>Description</ControlLabel>
                                    <FormControl
                                        type="text"
                                        placeholder="Description"
                                        onChange={event => {
                                            this.setState({
                                                selectedCategory: {
                                                    description: (event.target as HTMLInputElement).value
                                                }
                                            });
                                        }}
                                        value={this.state.selectedCategoryDescription}
                                    />
                                </FormGroup>
                            </Modal.Body>

                            <Modal.Footer>
                                <Button
                                    onClick={() => {
                                        this.setState({ showModalEdit: false });
                                    }}
                                >
                                    Close
                                </Button>
                                <Button
                                    bsStyle="primary"
                                    onClick={() => {
                                        if (this.state.selectedCategoryId !== undefined) {
                                            if (
                                                Validation.notEmpty(
                                                    this.state.selectedCategoryName !== undefined
                                                        ? this.state.selectedCategoryName
                                                        : ""
                                                ).isValid()
                                            ) {
                                                this.editCategory(
                                                    this.state.selectedCategoryId,
                                                    this.state.selectedCategoryName as string,
                                                    this.state.selectedCategoryDescription !== undefined
                                                        ? this.state.selectedCategoryDescription
                                                        : ""
                                                );
                                            }
                                        }
                                        this.setState({ showModalEdit: false });
                                    }}
                                >
                                    Edit category
                                </Button>
                            </Modal.Footer>
                        </Modal.Dialog>
                    </div>
                )}
            </div>
        );
    }

    private renderAddCategory(): JSX.Element {
        return (
            <div>
                {this.renderDefault()}
                <div className="static-modal">
                    <Modal.Dialog>
                        <Modal.Header>
                            <Modal.Title>Add category</Modal.Title>
                        </Modal.Header>

                        <Modal.Body>
                            <FormGroup
                                controlId="formControlName"
                                validationState={Validation.notEmpty(this.state.addCategoryName).toString()}
                            >
                                <ControlLabel>Name</ControlLabel>
                                <FormControl
                                    type="text"
                                    placeholder="Name"
                                    onChange={event => {
                                        this.setState({
                                            addCategoryName: (event.target as HTMLInputElement).value
                                        });
                                    }}
                                    value={this.state.addCategoryName}
                                />
                            </FormGroup>
                            <FormGroup controlId="formControlDescription">
                                <ControlLabel>Description</ControlLabel>
                                <FormControl
                                    type="text"
                                    placeholder="Name"
                                    onChange={event => {
                                        this.setState({
                                            addCategoryDescription: (event.target as HTMLInputElement).value
                                        });
                                    }}
                                    value={this.state.addCategoryDescription}
                                />
                            </FormGroup>
                        </Modal.Body>

                        <Modal.Footer>
                            <Button
                                onClick={() => {
                                    this.setState({ addCategoryName: "", addCategoryDescription: "" });
                                    State.mainNavigation.redirectLink = RouteName.category;
                                }}
                            >
                                Close
                            </Button>
                            <Button
                                bsStyle="primary"
                                onClick={() => {
                                    if (Validation.notEmpty(this.state.addCategoryName).isValid()) {
                                        this.addCategory(
                                            this.state.addCategoryName,
                                            this.state.addCategoryDescription !== undefined
                                                ? this.state.addCategoryDescription
                                                : ""
                                        );
                                        this.setState({ addCategoryName: "", addCategoryDescription: "" });
                                        State.mainNavigation.redirectLink = RouteName.category;
                                    }
                                }}
                            >
                                Add category
                            </Button>
                        </Modal.Footer>
                    </Modal.Dialog>
                </div>
            </div>
        );
    }

    private loadCategories() {
        GetCategoriesListAction((error, result) => {
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                this.setState({
                    errorMessage: error,
                    successMessage: undefined,
                    categoriesList: undefined
                });
            } else {
                this.setState({
                    errorMessage: undefined,
                    categoriesList: result
                });
            }
        });
    }

    private removeCategory(id: number) {
        RemoveCategoryAction(id, error => {
            if (error !== undefined) {
                return this.handleError(error);
            }
            this.setState({ errorMessage: undefined, successMessage: "Category was deleted!" });
            this.loadCategories();
        });
    }

    private addCategory(name: string, description: string) {
        AddCategoryAction(name, description, error => {
            if (error !== undefined) {
                return this.handleError(error);
            }
            this.setState({ errorMessage: undefined, successMessage: "Category was added!" });
            this.loadCategories();
        });
    }

    private editCategory(id: number, name: string, description: string) {
        EditCategoryAction(id, name, description, error => {
            if (error !== undefined) {
                return this.handleError(error);
            }
            this.setState({ errorMessage: undefined, successMessage: "Category was successfully edited!" });
            this.loadCategories();
        });
    }

    private handleError(error: string | object) {
        this.setState({ errorMessage: error, successMessage: undefined });
    }
}
