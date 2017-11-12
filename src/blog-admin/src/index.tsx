import * as React from 'react';
import * as ReactDOM from 'react-dom';
import Router from './Router';
import registerServiceWorker from './registerServiceWorker';

// Styles
import 'bootstrap/dist/css/bootstrap.css';

ReactDOM.render(
    <Router />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();