import { observable, action } from "mobx";
import { LoggedUser } from "./api/SignControllerApi";

export interface MainNavigation {
    currentLink?: string;
    redirectLink?: string;
    redirectAfterSignIn?: string;
    redirectId?: number;
}

export class BlogAdminStore {
    @observable
    mainNavigation: MainNavigation = {
        currentLink: undefined,
        redirectLink: undefined,
        redirectAfterSignIn: undefined,
        redirectId: undefined
    };

    @observable isLoading: boolean = false;
    @observable uploadingCount: number = 0;

    endpoint: string = "";

    @observable
    loggedUser: LoggedUser = {
        id: undefined,
        lastSignInDate: undefined,
        role: undefined,
        nickname: undefined,
        tokenId: undefined
    };

    @action
    public setLoggedUserState(loggedUser: LoggedUser) {
        State.loggedUser.id = loggedUser.id;
        State.loggedUser.lastSignInDate = loggedUser.lastSignInDate;
        State.loggedUser.role = loggedUser.role;
        State.loggedUser.nickname = loggedUser.nickname;
        State.loggedUser.tokenId = loggedUser.tokenId;
        document.cookie = `tokenId=${loggedUser.tokenId}; expires=${new Date().addDays(10)}`;
    }

    @action
    public resetLoggedUserState() {
        State.loggedUser.id = undefined;
        State.loggedUser.lastSignInDate = undefined;
        State.loggedUser.role = undefined;
        State.loggedUser.nickname = undefined;
        State.loggedUser.tokenId = undefined;
        document.cookie = `tokenId= ; expires=${new Date(1970, 0, 0)}`;
    }
}

export const State = new BlogAdminStore();
