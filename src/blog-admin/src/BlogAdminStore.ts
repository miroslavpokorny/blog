import { observable } from 'mobx';

export interface MainNavigation {
    currentLink?: string;
    redirectLink?: string;
}

export class BlogAdminStore {
    @observable mainNavigation: MainNavigation = {
        currentLink: undefined,
        redirectLink: undefined
    };
}

export const State = new BlogAdminStore();