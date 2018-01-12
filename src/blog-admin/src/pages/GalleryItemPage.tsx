import * as React from "react";
import MainNavigation from "../components/MainNavigation";
import { RouteComponentProps } from "react-router";
import PageHelper from "../helpers/PageHelper";
import { UserRole } from "../api/UserRole";
import {
    GalleryItemsListDto,
    GetGalleryItemsListAction,
    RemoveGalleryItemAction,
    AddGalleryItemAction
} from "../api/GalleryControllerApi";
import { observer } from "mobx-react";
import { State } from "../BlogAdminStore";
import { RouteName } from "../Router";
import FileUpload from "../components/FileUpload";
import { ButtonToolbar, Button, Alert, Table, Modal } from "react-bootstrap";
import LoadingOverlay from "../components/LoadingOverlay";

interface GalleryItemPageParams {
    id: string;
}

interface PageProps extends RouteComponentProps<GalleryItemPageParams> {}

@observer
export default class GalleryItemPage extends React.Component<PageProps> {
    state: {
        errorMessage?: string;
        successMessage?: string;
        galleryItemsList?: GalleryItemsListDto;
        galleryId: number;
        showModalUpload: boolean;
        selectedGalleryItemId?: number;
        selectedGalleryItemTitle?: string;
        showModalEdit: boolean;
    };

    private _isMounted: boolean = false;

    constructor(props: PageProps) {
        super(props);
        this.state = {
            errorMessage: undefined,
            successMessage: undefined,
            galleryItemsList: undefined,
            galleryId: parseInt(props.match.params.id ? props.match.params.id : "", 10),
            showModalUpload: false,
            selectedGalleryItemId: undefined,
            selectedGalleryItemTitle: undefined,
            showModalEdit: false
        };
        if (this.state.galleryId === NaN) {
            State.mainNavigation.redirectLink = RouteName.about;
            return;
        }
        setTimeout(() => this.loadGalleryItems(), 0);
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
                    <div className="container">{this.renderDefault()}</div>
                </div>
            );
        });
    }

    private renderDefault() {
        return (
            <div>
                <h2>Gallery items (ID: {this.state.galleryId})</h2>
                <ButtonToolbar>
                    <Button
                        bsStyle="primary"
                        bsSize="large"
                        onClick={() => {
                            this.setState({ showModalUpload: true });
                        }}
                    >
                        Upload new gallery images
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
                {(this.state.galleryItemsList === undefined ||
                    this.state.galleryItemsList.items === undefined ||
                    this.state.galleryItemsList.items.length === 0) && <p>No data to display</p>}
                {this.state.galleryItemsList !== undefined &&
                    this.state.galleryItemsList.items !== undefined &&
                    this.state.galleryItemsList.items.length > 0 && (
                        <Table striped={true} bordered={true} condensed={true} hover={true}>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Preview</th>
                                    <th>Title</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.galleryItemsList.items.map((item, i) => {
                                    return (
                                        <tr key={i}>
                                            <td>{item.id}</td>
                                            <td>{item.imageName}</td>
                                            <td>{item.title}</td>
                                            <td>
                                                <Button
                                                    bsStyle="danger"
                                                    onClick={() => this.removeGalleryItem(item.id)}
                                                >
                                                    Remove
                                                </Button>
                                                <Button
                                                    onClick={() => {
                                                        this.setState({
                                                            selectedGalleryItemId: item.id,
                                                            selectedGalleryItemTitle: item.title
                                                        });
                                                        this.setState({ showModalEdit: true });
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
                <LoadingOverlay display={State.uploadingCount > 0} text="Uploading" />
                {this.state.showModalUpload && this.renderUploadFilesModal()}
                {this.state.showModalEdit && this.renderEditItemModal()}
            </div>
        );
    }

    private renderUploadFilesModal() {
        return (
            <div className="static-modal">
                <Modal.Dialog>
                    <Modal.Header>
                        <Modal.Title>Upload images</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <FileUpload
                            mimeType="image/jpeg,image/png"
                            multiple={true}
                            onDropAccepted={files => this.uploadFiles(files)}
                            text="Drop your images here!"
                        />
                    </Modal.Body>

                    <Modal.Footer>
                        <Button
                            onClick={() => {
                                this.setState({ showModalUpload: false });
                            }}
                        >
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal.Dialog>
            </div>
        );
    }

    private renderEditItemModal() {
        return <div>Edit modal</div>;
    }

    private loadGalleryItems() {
        GetGalleryItemsListAction(this.state.galleryId, (error, result) => {
            if (!this._isMounted) {
                return;
            }
            if (error !== undefined) {
                this.handleError(error);
            } else {
                this.state.galleryItemsList = result;
            }
        });
    }

    private removeGalleryItem(id: number) {
        RemoveGalleryItemAction(id, error => {
            if (error !== undefined) {
                this.handleError(error);
            } else {
                this.handleSuccess("Gallery item was deleted!");
            }
        });
    }

    private uploadFiles(files: File[]) {
        const errors: Array<string | object> = [];
        console.log("Before upload");
        files.forEach(file => {
            console.log("Upload start");
            AddGalleryItemAction(this.state.galleryId, file, error => {
                console.log("Upload callback");
                if (error !== undefined) {
                    errors.push(error);
                }
            });
        });
        console.log("Upload should done");

        console.log(errors);
    }

    private handleError(error: string | object) {
        this.setState({ successMessage: undefined, errorMessage: error });
    }

    private handleSuccess(success: string) {
        this.setState({ successMessage: success, errorMessage: undefined });
    }
}
