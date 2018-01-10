import { JsonBase } from "./JsonBase";
import {
    callRestApiWithResult,
    callRestApiWithoutResult
} from "./RestApiCalls";
import { Endpoint } from "./Endpoint";
import { RequestId } from "./RequestId";

export interface UserInfoDto {
    name: string;
    surname: string;
    nickname: string;
    email: string;
    role: number;
    enabled: boolean;
    id: number;
}

export interface UsersListDto extends JsonBase {
    users: UserInfoDto[];
}

export interface ChangeUserRoleDto extends JsonBase {
    userId: number;
    role: number;
}

export function GetUsersListAction(
    callback: (error?: string | object, result?: UsersListDto) => void
) {
    callRestApiWithResult(Endpoint.UsersList, (error, result) => {
        if (error !== undefined) {
            return callback(error, undefined);
        }
        return callback(undefined, result as UsersListDto);
    });
}

export function SwitchUserEnabledStateAction(
    id: number,
    callback: (error?: string | object) => void
) {
    let data: RequestId = {
        id: id
    };
    callRestApiWithoutResult(
        Endpoint.UsersSwitchEnabledState,
        error => {
            callback(error);
        },
        data
    );
}

export function ChangeUserRoleAction(
    id: number,
    role: number,
    callback: (error?: string | object) => void
) {
    let data: ChangeUserRoleDto = {
        userId: id,
        role: role
    };
    callRestApiWithoutResult(
        Endpoint.UsersChangeRole,
        error => {
            callback(error);
        },
        data
    );
}
