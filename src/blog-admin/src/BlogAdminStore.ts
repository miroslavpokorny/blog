import { observable } from 'mobx';
import { UserRole } from './api/UserRole';

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
    userRole: UserRole | undefined = undefined;

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
}

export const State = new BlogAdminStore();