import * as React from 'react';
import * as ReactDOM from 'react-dom';
// import App from './App';
import registerServiceWorker from './registerServiceWorker';
// import './index.css';
// import Hello from './components/Hello';
import 'bootstrap/dist/css/bootstrap.css';
// import 'bootstrap/dist/css/bootstrap-theme.css';
import MainNavigation from './components/MainNavigation';

ReactDOM.render(
  // <App />,
  <MainNavigation />,
  // <Hello name="TypeScript" enthusiasmLevel={10} />,
  document.getElementById('root') as HTMLElement
);
registerServiceWorker();
