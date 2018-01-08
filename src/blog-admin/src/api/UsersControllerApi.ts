import { JsonBase } from "./JsonBase";
import { callRestApiWithResult } from "./RestApiCalls";
import { Endpoint } from "./Endpoint";

interface UserInfoDto {
    name: string;
    surname: string;
    nickname: string;
    email: string;
    role: number;
    isEnabled: boolean;
}

interface UsersListDto extends JsonBase {
    users: UserInfoDto[];
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
