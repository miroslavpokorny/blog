import { observable } from 'mobx';
import { LoggedUser } from './api/SignControllerApi';
import { ProfileInfo } from './api/ProfileControllerApi';

export interface MainNavigation {
    currentLink?: string;
    redirectLink?: string;
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
        name: undefined,
        id: 0
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