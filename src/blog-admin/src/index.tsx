import * as React from 'react';
import * as ReactDOM from 'react-dom';
import Router from './Router';
import registerServiceWorker from './registerServiceWorker';
import { State } from './BlogAdminStore';

// Styles
import 'bootstrap/dist/css/bootstrap.css';

// tslint:disable-next-line:no-console
console.log(State);

ReactDOM.render(
    <Router />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();