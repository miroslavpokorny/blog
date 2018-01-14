import * as React from "react";
import { DragEvent } from "react";
import Dropzone, { ImageFile } from "react-dropzone";
import "./FileUpload.css";

interface FileUploadProps {
    mimeType: string;
    onDropAccepted: (files: ImageFile[]) => void;
    multiple: boolean;
    text: string;
}

class FileUpload extends React.Component<FileUploadProps> {
    dz: Dropzone;
    state: {
        previews: string[];
    };

    private _previews: string[] = [];

    constructor(props: FileUploadProps) {
        super(props);
        this.state = {
            previews: []
        };
    }

    open() {
        this.dz.open();
    }

    render() {
        return (
            <div>
                <Dropzone
                    className="file-upload"
                    ref={node => {
                        if (node !== null) {
                            this.dz = node;
                        }
                    }}
                    onDropAccepted={(files, event) => this.handleDropAcceptedFile(files, event)}
                    onDropRejected={(files, event) => this.handleDropRejectedFile(files, event)}
                    multiple={this.props.multiple}
                    accept={this.props.mimeType}
                    name="dropzone"
                    inputProps={{ id: "dropzone" }}
                >
                    <div className="file-upload-inner">
                        <div className="file-upload-text">{this.props.text}</div>
                    </div>
                </Dropzone>
                <div>
                    {this.state.previews.length > 0 && <h3>Preview</h3>}
                    {this.props.multiple &&
                        this.state.previews.map((item, i) => {
                            return (
                                <div className="file-upload-previews" key={i}>
                                    <img alt="preview" src={item} />
                                </div>
                            );
                        })}
                    {!this.props.multiple &&
                        this.state.previews.length > 0 && (
                            <div className="file-upload-previews">
                                <img alt="preview" src={this.state.previews[this.state.previews.length - 1]} />
                            </div>
                        )}
                </div>
            </div>
        );
    }

    componentWillUnmount() {
        this._previews.forEach(item => {
            window.URL.revokeObjectURL(item);
        });
        this._previews = [];
    }

    private handleDropAcceptedFile(files: ImageFile[], event: DragEvent<HTMLDivElement>) {
        files.forEach(item => {
            if (item.preview !== undefined) {
                this._previews.push(item.preview);
            }
        });
        this.setState({ previews: this._previews });
        this.props.onDropAccepted(files);
    }

    private handleDropRejectedFile(files: ImageFile[], event: DragEvent<HTMLDivElement>) {
        files.forEach(file => {
            if (file.preview !== undefined) {
                window.URL.revokeObjectURL(file.preview);
            }
        });
    }
}

export default FileUpload;
