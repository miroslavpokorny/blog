import * as React from 'react';
import { 
    HashRouter, 
    Route,
    Switch
} from 'react-router-dom';
import HomePage from './pages/HomePage';
import AboutPage from './pages/AboutPage';
import ArticlePage from './pages/ArticlePage';
import CategoryPage from './pages/CategoryPage';
import GalleryPage from './pages/GalleryPage';
import ProfilePage from './pages/ProfilePage';
import UserPage from './pages/UserPage';

export default class Router extends React.Component {
    render() {
        return (
            <HashRouter>
                <Switch>
                    <Route exact={true} path="/" component={HomePage} />
                    <Route path="/about" component={AboutPage} />
                    <Route path="/article" component={ArticlePage} />
                    <Route path="/category" component={CategoryPage} />
                    <Route path="/gallery" component={GalleryPage} />
                    <Route path="/profile/:action?/:id?" component={ProfilePage} />
                    {/* <Route path="/profile" component={ProfilePage} /> */}
                    <Route path="/user" component={UserPage} />
                </Switch>
            </HashRouter>
        );
    }
}

export enum RouteName {
    home = '/',
    about = '/about',
    article = '/article',
    category = '/category',
    gallery = '/gallery',
    profile = '/profile',
    profileEdit = '/profile/edit',
    profileUploadAvatar = '/profile/uploadAvatar',
    profileChangePassword = '/profile/changePassword',
    user = '/user'
}
