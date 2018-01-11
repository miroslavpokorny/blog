import { callRestApiWithResult, callRestApiWithoutResult } from "./RestApiCalls";
import { Endpoint } from "./Endpoint";
import { DtoBase } from "./DtoBase";
import { RequestByIdDto } from "./RequestId";

export interface ProfileInfo extends DtoBase {
    name?: string;
    surname?: string;
    nickname: string;
    id: number;
}

export function LoadProfileAction(id: number, callback: (error?: string | object, result?: ProfileInfo) => void) {
    const data: RequestByIdDto = {
        id: id
    };
    callRestApiWithResult<ProfileInfo>(
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
    const data: ProfileInfo = {
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
