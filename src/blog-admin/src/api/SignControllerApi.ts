import { State } from "../BlogAdminStore";
import { Endpoint } from "./Endpoint";
import { callRestApiWithResult, callRestApiWithoutResult } from "./RestApiCalls";
import { DtoBase } from "./DtoBase";
import "datejs";

export interface LoggedUser extends DtoBase {
    id?: number;
    lastSignInDate?: Date;
    role?: number;
    nickname?: string;
    tokenId?: string;
}

export interface SignInCredentials {
    email: string;
    password: string;
}

export interface SignUpData {
    email: string;
    nickname: string;
    password: string;
    name: string;
    surname: string;
}

export function SignInAction(email: string, password: string, callback: (error?: string | object) => void) {
    const data: SignInCredentials = {
        email: email,
        password: password
    };
    callRestApiWithResult<LoggedUser>(
        Endpoint.SignIn,
        (error, result) => {
            if (error !== undefined) {
                return callback(error);
            }
            if (result === undefined) {
                return callback("Server return corrupted data!");
            }
            setLoggedUserState(result);
            return callback();
        },
        data
    );
}

export function SignOutAction(callback: (error?: string | object) => void) {
    callRestApiWithoutResult(Endpoint.SignOut, error => {
        if (error !== undefined) {
            return callback(error);
        }
        resetLoggedUserState();
        return callback();
    });
}

export function GetLoggedUserAction(callback: (error?: string | object) => void) {
    callRestApiWithResult<LoggedUser>(Endpoint.GetLoggedUser, (error, result) => {
        if (error !== undefined) {
            return callback(error);
        }
        if (result !== undefined) {
            setLoggedUserState(result);
        }
        return callback();
    });
}

export function SignUpAction(
    email: string,
    nickname: string,
    password: string,
    name: string,
    surname: string,
    callback: (error?: string | object) => void
) {
    const data: SignUpData = {
        email: email,
        nickname: nickname,
        password: password,
        name: name,
        surname: surname
    };
    callRestApiWithResult<LoggedUser>(
        Endpoint.SignUp,
        (error, result) => {
            if (error !== undefined) {
                return callback(error);
            }
            if (result !== undefined) {
                setLoggedUserState(result);
            }
            return callback();
        },
        data
    );
}

function setLoggedUserState(loggedUser: LoggedUser) {
    State.loggedUser.id = loggedUser.id;
    State.loggedUser.lastSignInDate = loggedUser.lastSignInDate;
    State.loggedUser.role = loggedUser.role;
    State.loggedUser.nickname = loggedUser.nickname;
    State.loggedUser.tokenId = loggedUser.tokenId;
    document.cookie = `tokenId=${loggedUser.tokenId}; expires=${new Date().addDays(10)}`;
}

function resetLoggedUserState() {
    State.loggedUser.id = undefined;
    State.loggedUser.lastSignInDate = undefined;
    State.loggedUser.role = undefined;
    State.loggedUser.nickname = undefined;
    State.loggedUser.tokenId = undefined;
    document.cookie = `tokenId= ; expires=${new Date(1970, 0, 0)}`;
}
