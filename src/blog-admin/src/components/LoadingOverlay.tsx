import * as React from 'react';
import { observer } from 'mobx-react';

import './LoadingOverlay.css';

interface LoadingOverlayProps {
    display: boolean;
    text?: string;
}

@observer
class LoadingOverlay extends React.Component<LoadingOverlayProps> {
    render() {
        if (this.props.display === false) {
            return false;
        }
        return (
        <div className="loading-overlay">
            <div>
                <div className="lds-css ng-scope">
                    <div className="lds-spinner">
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                        <div/>
                    </div>
                </div><br/>
                <div className="text-center">{this.props.text}</div>
            </div>
        </div>);
    }
}

export default LoadingOverlay;