import { observable } from 'mobx';
import { LoggedUser } from './api/SignControllerApi';

export interface MainNavigation {
    currentLink?: string;
    redirectLink?: string;
    redirectAfterSignIn?: string;
}

export class BlogAdminStore {
    @observable 
    mainNavigation: MainNavigation = {
        currentLink: undefined,
        redirectLink: undefined,
        redirectAfterSignIn: undefined
    };

    @observable
    isLoading: boolean = false;

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