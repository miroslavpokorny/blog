import { observable } from 'mobx';
import { LoggedUser } from './api/SignControllerApi';

export interface MainNavigation {
    currentLink?: string;
    redirectLink?: string;
}

export interface ProfileInfo {
    name?: string;
    surname?: string;
    nickname: string;
}

export interface SignInForm {
    email: string;
    password: string;
}

export class BlogAdminStore {
    @observable 
    mainNavigation: MainNavigation = {
        currentLink: undefined,
        redirectLink: undefined
    };

    @observable
    profile: ProfileInfo = {
        nickname: '',
        surname: undefined,
        name: undefined
    };

    @observable
    isLoading: boolean = false;

    @observable
    signInForm: SignInForm = {
        email: '',
        password: '',
    };

    endpoint: string = '';

    @observable
    loggedUser: LoggedUser = {
        id: undefined,
        lastSignInDate: undefined,
        role: undefined,
        nickname: undefined,
        tokenId: undefined
    };
}

export const State = new BlogAdminStore();