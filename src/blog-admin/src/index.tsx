import * as React from 'react';
import * as ReactDOM from 'react-dom';
// import App from './App';
import registerServiceWorker from './registerServiceWorker';
// import './index.css';
// import Hello from './components/Hello';
import 'bootstrap/dist/css/bootstrap.css';
// import 'bootstrap/dist/css/bootstrap-theme.css';
import * as Navbar from 'react-bootstrap/lib/Navbar';
import * as Nav from 'react-bootstrap/lib/Nav';
import * as NavItem from 'react-bootstrap/lib/NavItem';
import * as NavDropdown from 'react-bootstrap/lib/NavDropdown';
import * as MenuItem from 'react-bootstrap/lib/MenuItem';

const navbarInstance = (
  <Navbar>
    <Navbar.Header>
      <Navbar.Brand>
        <a href="#">React-Bootstrap</a>
      </Navbar.Brand>
    </Navbar.Header>
    <Nav>
      <NavItem eventKey={1} href="#">Link</NavItem>
      <NavItem eventKey={2} href="#">Link</NavItem>
      <NavDropdown eventKey={3} title="Dropdown" id="basic-nav-dropdown">
        <MenuItem eventKey={3.1}>Action</MenuItem>
        <MenuItem eventKey={3.2}>Another action</MenuItem>
        <MenuItem eventKey={3.3}>Something else here</MenuItem>
        <MenuItem divider={true} />
        <MenuItem eventKey={3.4}>Separated link</MenuItem>
      </NavDropdown>
    </Nav>
  </Navbar>
);

ReactDOM.render(
  // <App />,
  navbarInstance,
  // <Hello name="TypeScript" enthusiasmLevel={10} />,
  document.getElementById('root') as HTMLElement
);
registerServiceWorker();
