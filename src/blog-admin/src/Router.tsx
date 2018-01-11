import * as React from "react";
import { HashRouter, Route, Switch } from "react-router-dom";
import HomePage from "./pages/HomePage";
import AboutPage from "./pages/AboutPage";
import ArticlePage from "./pages/ArticlePage";
import CategoryPage from "./pages/CategoryPage";
import GalleryPage from "./pages/GalleryPage";
import ProfilePage from "./pages/ProfilePage";
import UserPage from "./pages/UserPage";
import SignPage from "./pages/SignPage";

export default class Router extends React.Component {
    render() {
        return (
            <HashRouter>
                <Switch>
                    <Route exact={true} path="/" component={HomePage} />
                    <Route path="/about" component={AboutPage} />
                    <Route path="/article/:action?/:id?" component={ArticlePage} />
                    <Route path="/category/:action?" component={CategoryPage} />
                    <Route path="/gallery/:action?/:id?" component={GalleryPage} />
                    <Route path="/profile/:action?/:id?" component={ProfilePage} />
                    <Route path="/sign/:action" component={SignPage} />
                    <Route path="/user" component={UserPage} />
                </Switch>
            </HashRouter>
        );
    }
}

export enum RouteName {
    home = "/",
    about = "/about",
    article = "/article",
    articleAdd = "/article/add",
    category = "/category",
    categoryAdd = "/category/add",
    gallery = "/gallery",
    galleryAdd = "/gallery/add",
    galleryEdit = "/gallery/edit",
    profile = "/profile",
    profileEdit = "/profile/edit",
    profileUploadAvatar = "/profile/uploadAvatar",
    profileChangePassword = "/profile/changePassword",
    user = "/user",
    signIn = "/sign/in",
    signOut = "/sign/out",
    signUp = "/sign/up",
    signPassword = "/sign/password"
}
