import * as React from 'react';
import * as ReactDOM from 'react-dom';
import Router from './Router';
import registerServiceWorker from './registerServiceWorker';
import { State } from './BlogAdminStore';

// Styles
import 'bootstrap/dist/css/bootstrap.css';
import { GetLoggedUserAction } from './api/SignControllerApi';

// TODO change to production server
State.endpoint = 'http://localhost:8080';

ReactDOM.render(
    <Router />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();

const tokenId = document.cookie.replace(/(?:(?:^|.*;\s*)tokenId\s*\=\s*([^;]*).*$)|^.*$/, '$1');
if (tokenId !== undefined && tokenId.trim().length !== 0) {
    State.loggedUser.tokenId = tokenId;
    GetLoggedUserAction((error) => {
        console.log(error);
    });
}
