import { callRestApiWithResult, callRestApiWithoutResult } from "./RestApiCalls";
import { Endpoint } from "./Endpoint";
import { DtoBase } from "./DtoBase";
import { RequestByIdDto } from "./RequestId";

export interface ProfileInfoDto extends DtoBase {
    name?: string;
    surname?: string;
    nickname: string;
    id: number;
}

export interface ChangePasswordDto extends DtoBase {
    userId: number;
    oldPassword: string;
    newPassword: string;
}

export function LoadProfileAction(id: number, callback: (error?: string | object, result?: ProfileInfoDto) => void) {
    const data: RequestByIdDto = {
        id: id
    };
    callRestApiWithResult<ProfileInfoDto>(
        Endpoint.ProfileLoad,
        (error, result) => {
            if (error !== undefined) {
                return callback(error);
            }
            if (result === undefined) {
                return callback("Server return corrupted data!");
            }
            callback(undefined, result);
        },
        data
    );
}

export function EditProfileAction(
    id: number,
    nickname: string,
    name: string,
    surname: string,
    callback: (error?: string | object) => void
) {
    const data: ProfileInfoDto = {
        id: id,
        nickname: nickname,
        name: name,
        surname: surname
    };
    callRestApiWithoutResult(
        Endpoint.ProfileEdit,
        error => {
            if (error !== undefined) {
                return callback(error);
            }
            return callback();
        },
        data
    );
}

export function ChangePasswordAction(
    userId: number,
    oldPassword: string,
    newPassword: string,
    callback: (error?: string | object) => void
) {
    const data: ChangePasswordDto = {
        userId: userId,
        oldPassword: oldPassword,
        newPassword: newPassword
    };
    callRestApiWithoutResult(
        Endpoint.ProfileChangePassword,
        error => {
            if (error !== undefined) {
                return callback(error);
            }
            return callback();
        },
        data
    );
}
