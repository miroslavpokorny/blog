import * as React from "react";

// tslint:disable-next-line:no-any
declare var tinymce: any;

interface WysiwygProps {
    text: string;
    onChange: (value: string) => void;
}

interface Editor {
    on: (name: string, callback: (event: Event) => void, first?: boolean) => Object;
    getContent(args?: {}): string;
}

class WysiwygEditor extends React.Component<WysiwygProps> {
    editor: Editor;

    constructor(props: WysiwygProps) {
        super(props);
    }

    render() {
        return <textarea onChange={value => this.props.onChange(value.target.value)} value={this.props.text} />;
    }

    componentDidMount() {
        tinymce.init({
            selector: "textarea",
            height: 400,
            menubar: false,
            toolbar:
                "undo redo | insert | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
            content_css: "//www.tinymce.com/css/codepen.min.css",
            setup: (editor: Editor) => {
                editor.on("init", event => {
                    setTimeout(() => {
                        const elements = document.getElementsByClassName("mce-notification-warning");
                        while (elements.length > 0) {
                            const parent = elements[0].parentNode;
                            if (parent !== null) {
                                parent.removeChild(elements[0]);
                            }
                        }
                    }, 0);
                });
            },
            init_instance_callback: (editor: Editor) => {
                editor.on("KeyUp", event => {
                    this.props.onChange(editor.getContent());
                });
            }
        });
    }
}

export default WysiwygEditor;
