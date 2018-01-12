import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { UserRole } from "../api/UserRole";
import { ButtonToolbar, Button, Alert, Table, Modal, FormGroup, ControlLabel, FormControl } from "react-bootstrap";
import { State } from "../BlogAdminStore";
import { RouteName } from "../Router";
import {
    GalleryListDto,
    RemoveGalleryAction,
    GetGalleryListAction,
    AddGalleryAction,
    EditGalleryAction
} from "../api/GalleryControllerApi";
import LoadingOverlay from "../components/LoadingOverlay";
import { Validation } from "../helpers/ValidationHelper";
import { observer } from "mobx-react";
import { UserInfoDto } from "../api/UsersControllerApi";

interface GalleryPageParams {
    action?: string;
    id?: string;
}

interface PageProps extends RouteComponentProps<GalleryPageParams> {}

@observer
export default class GalleryPage extends React.Component<PageProps> {
    state: {
        errorMessage?: string;
        successMessage?: string;
        galleriesList?: GalleryListDto;
        addGalleryName: string;
        addGalleryDescription: string;
        selectedGalleryId?: number;
        selectedGalleryName: string;
        selectedGalleryDescription: string;
        selectedGalleryAuthor?: UserInfoDto;
    };

    private _isMounted: boolean = false;

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            galleriesList: undefined,
            successMessage: undefined,
            addGalleryName: "",
            addGalleryDescription: "",
            selectedGalleryId: parseInt(props.match.params.id ? props.match.params.id : "", 10),
            selectedGalleryAuthor: undefined,
            selectedGalleryDescription: "",
            selectedGalleryName: ""
        };
        setTimeout(() => this.loadGalleries(), 0);
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
                        {!this.props.match.params.action && this.renderDefault()}
                        {this.props.match.params.action === "add" && this.renderAddGallery()}
                        {this.props.match.params.action === "edit" && this.renderEditGallery()}
                    </div>
                </div>
            );
        });
    }

    renderDefault() {
        return (
            <div className="container">
                <h2>Galleries</h2>
                <ButtonToolbar>
                    <Button
                        bsStyle="primary"
                        bsSize="large"
                        onClick={() => {
                            State.mainNavigation.redirectLink = RouteName.galleryAdd;
                        }}
                    >
                        Add gallery
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
                {(this.state.galleriesList === undefined ||
                    this.state.galleriesList.galleries === undefined ||
                    this.state.galleriesList.galleries.length === 0) && <p>No data to display</p>}
                {this.state.galleriesList !== undefined &&
                    this.state.galleriesList.galleries !== undefined &&
                    this.state.galleriesList.galleries.length > 0 && (
                        <Table striped={true} bordered={true} condensed={true} hover={true}>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.galleriesList.galleries.map((item, i) => {
                                    return (
                                        <tr key={i}>
                                            <td>{item.id}</td>
                                            <td>{item.name}</td>
                                            <td>{item.description}</td>
                                            <td>
                                                <Button bsStyle="danger" onClick={() => this.removeGallery(item.id)}>
                                                    Remove
                                                </Button>
                                                <Button
                                                    onClick={() => {
                                                        this.setState({
                                                            selectedGalleryId: item.id,
                                                            selectedGalleryAuthor: item.author,
                                                            selectedGalleryDescription: item.description,
                                                            selectedGalleryName: item.name
                                                        });
                                                        State.mainNavigation.redirectId = item.id;
                                                        State.mainNavigation.redirectLink = RouteName.galleryEdit;
                                                    }}
                                                >
                                                    Edit
                                                </Button>
                                                <Button
                                                    onClick={() => {
                                                        State.mainNavigation.redirectId = item.id;
                                                        State.mainNavigation.redirectLink = RouteName.galleryItem;
                                                    }}
                                                >
                                                    Edit items
                                                </Button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </Table>
                    )}
                <LoadingOverlay display={State.isLoading} />
            </div>
        );
    }

    renderAddGallery() {
        return (
            <div>
                {this.renderDefault()}
                <div className="static-modal">
                    <Modal.Dialog>
                        <Modal.Header>
                            <Modal.Title>Add gallery</Modal.Title>
                        </Modal.Header>

                        <Modal.Body>
                            <FormGroup
                                controlId="formControlName"
                                validationState={Validation.notEmpty(this.state.addGalleryName).toString()}
                            >
                                <ControlLabel>Name</ControlLabel>
                                <FormControl
                                    type="text"
                                    placeholder="Name"
                                    onChange={event => {
                                        this.setState({
                                            addGalleryName: (event.target as HTMLInputElement).value
                                        });
                                    }}
                                    value={this.state.addGalleryName}
                                />
                            </FormGroup>
                            <FormGroup controlId="formControlDescription">
                                <ControlLabel>Description</ControlLabel>
                                <FormControl
                                    type="text"
                                    placeholder="Name"
                                    onChange={event => {
                                        this.setState({
                                            addGalleryDescription: (event.target as HTMLInputElement).value
                                        });
                                    }}
                                    value={this.state.addGalleryDescription}
                                />
                            </FormGroup>
                        </Modal.Body>

                        <Modal.Footer>
                            <Button
                                onClick={() => {
                                    this.setState({ addGalleryName: "", addGalleryDescription: "" });
                                    State.mainNavigation.redirectLink = RouteName.gallery;
                                }}
                            >
                                Close
                            </Button>
                            <Button
                                bsStyle="primary"
                                onClick={() => {
                                    if (Validation.notEmpty(this.state.addGalleryName).isValid()) {
                                        this.addGallery(
                                            this.state.addGalleryName,
                                            this.state.addGalleryDescription !== undefined
                                                ? this.state.addGalleryDescription
                                                : "",
                                            State.loggedUser.id as number
                                        );
                                        this.setState({ addGalleryName: "", addGalleryDescription: "" });
                                        setTimeout(() => (State.mainNavigation.redirectLink = RouteName.gallery), 0);
                                    }
                                }}
                            >
                                Add gallery
                            </Button>
                        </Modal.Footer>
                    </Modal.Dialog>
                </div>
            </div>
        );
    }

    private renderEditGallery() {
        return (
            <div>
                {this.renderDefault()}
                <div className="static-modal">
                    <Modal.Dialog>
                        <Modal.Header>
                            <Modal.Title>Edit gallery</Modal.Title>
                        </Modal.Header>

                        <Modal.Body>
                            <FormGroup
                                controlId="formControlName"
                                validationState={Validation.notEmpty(this.state.selectedGalleryName).toString()}
                            >
                                <ControlLabel>Name</ControlLabel>
                                <FormControl
                                    type="text"
                                    placeholder="Name"
                                    onChange={event => {
                                        this.setState({
                                            selectedGalleryName: (event.target as HTMLInputElement).value
                                        });
                                    }}
                                    value={this.state.selectedGalleryName}
                                />
                            </FormGroup>
                            <FormGroup controlId="formControlDescription">
                                <ControlLabel>Description</ControlLabel>
                                <FormControl
                                    type="text"
                                    placeholder="Name"
                                    onChange={event => {
                                        this.setState({
                                            selectedGalleryDescription: (event.target as HTMLInputElement).value
                                        });
                                    }}
                                    value={this.state.selectedGalleryDescription}
                                />
                            </FormGroup>
                        </Modal.Body>

                        <Modal.Footer>
                            <Button
                                onClick={() => {
                                    // this.setState({ editGalleryName: "", editGalleryDescription: "" });
                                    State.mainNavigation.redirectLink = RouteName.gallery;
                                }}
                            >
                                Close
                            </Button>
                            <Button
                                bsStyle="primary"
                                onClick={() => {
                                    if (Validation.notEmpty(this.state.selectedGalleryName).isValid()) {
                                        if (
                                            this.state.selectedGalleryId !== undefined &&
                                            this.state.selectedGalleryAuthor !== undefined
                                        ) {
                                            this.editGallery(
                                                this.state.selectedGalleryId,
                                                this.state.selectedGalleryName,
                                                this.state.selectedGalleryDescription,
                                                this.state.selectedGalleryAuthor
                                            );
                                        }
                                        // this.setState({ addGalleryName: "", addGalleryDescription: "" });
                                        setTimeout(() => (State.mainNavigation.redirectLink = RouteName.gallery), 0);
                                    }
                                }}
                            >
                                Edit gallery
                            </Button>
                        </Modal.Footer>
                    </Modal.Dialog>
                </div>
            </div>
        );
    }

    private loadGalleries() {
        GetGalleryListAction((error, result) => {
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                this.handleError(error);
                return;
            }
            this.setState({ galleriesList: result });
            if (this.state.selectedGalleryId !== undefined && result !== undefined) {
                const selectedItem = result.galleries.find(item => {
                    return item.id === this.state.selectedGalleryId;
                });
                if (selectedItem === undefined) {
                    return;
                }
                this.setState({
                    selectedGalleryAuthor: selectedItem.author,
                    selectedGalleryName: selectedItem.name,
                    selectedGalleryDescription: selectedItem.description
                });
            }
        });
    }

    private removeGallery(id: number) {
        RemoveGalleryAction(id, error => {
            if (error !== undefined) {
                this.setState({
                    errorMessage: error,
                    successMessage: undefined,
                    galleriesList: undefined
                });
            } else {
                this.handleSuccess("Gallery was removed!");
                this.loadGalleries();
            }
        });
    }

    private addGallery(name: string, description: string, authorId: number) {
        AddGalleryAction(name, description, authorId, error => {
            if (error !== undefined) {
                this.handleError(error);
                return;
            }
            this.handleSuccess("Gallery was added!");
            this.loadGalleries();
        });
    }

    private editGallery(id: number, name: string, description: string, author: UserInfoDto) {
        EditGalleryAction(id, name, description, author, error => {
            if (error !== undefined) {
                this.handleError(error);
                return;
            }
            this.handleSuccess("Gallery was edited!");
            this.loadGalleries();
        });
    }

    private handleError(error: string | object) {
        this.setState({ successMessage: undefined, errorMessage: error });
    }

    private handleSuccess(message: string) {
        this.setState({ successMessage: message, errorMessage: undefined });
    }
}
