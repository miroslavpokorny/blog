import { State } from "../BlogAdminStore";
import { DtoBase } from "./DtoBase";
import axios, { AxiosRequestConfig } from "axios";

interface ErrorMessageDto extends DtoBase {
    message: string;
    code: string;
}

export function callRestApiWithResult<T extends DtoBase | ErrorMessageDto>(
    endpointPath: string,
    callback: (error?: string, result?: T) => void,
    data?: object
) {
    const endpoint = State.endpoint + endpointPath;
    State.isLoading = true;
    axios
        .post<T>(endpoint, data, {
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            params: {
                tokenId: getTokenId()
            }
        })
        .then(response => {
            State.isLoading = false;
            if (response.data.type !== undefined) {
                return response.data.type === "ErrorMessageDto" && (response.data as ErrorMessageDto).message !== null
                    ? callback((response.data as ErrorMessageDto).message)
                    : callback(undefined, response.data);
            }
            return callback("Unknown error!");
        })
        .catch(error => {
            State.isLoading = false;
            if (
                error.response !== undefined &&
                error.response.data !== undefined &&
                error.response.data.type !== undefined &&
                error.response.data.type === "ErrorMessageDto" &&
                error.response.data.message !== null
            ) {
                return callback(error.response.data.message);
            } else if (error.message !== undefined) {
                return callback(error.message);
            }
            return callback(JSON.stringify(error));
        });
}

export function callRestApiWithoutResult(endpointPath: string, callback: (error?: string) => void, data?: object) {
    const endpoint = State.endpoint + endpointPath;
    const params: AxiosRequestConfig = {
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json"
        },
        params: {
            tokenId: getTokenId()
        }
    };
    State.isLoading = true;
    if (data !== undefined) {
        axios
            .post(endpoint, data, params)
            .then(response => {
                State.isLoading = false;
                return callback();
            })
            .catch(error => {
                State.isLoading = false;
                if (
                    error.response !== undefined &&
                    error.response.data !== undefined &&
                    error.response.data.type !== undefined &&
                    error.response.data.type === "ErrorMessageDto" &&
                    error.response.data.message !== null
                ) {
                    return callback(error.response.data.message);
                } else if (error.message !== undefined) {
                    return callback(error.message);
                }
                return callback(JSON.stringify(error));
            });
    } else {
        axios
            .get(endpoint, params)
            .then(response => {
                State.isLoading = false;
                return callback();
            })
            .catch(error => {
                State.isLoading = false;
                if (
                    error.response !== undefined &&
                    error.response.data !== undefined &&
                    error.response.data.type !== undefined &&
                    error.response.data.type === "ErrorMessageDto" &&
                    error.response.data.message !== null
                ) {
                    return callback(error.response.data.message);
                } else if (error.message !== undefined) {
                    return callback(error.message);
                }
                return callback(JSON.stringify(error));
            });
    }
}

function getTokenId() {
    return State.loggedUser.tokenId !== undefined && State.loggedUser.tokenId.trim().length > 0
        ? State.loggedUser.tokenId
        : null;
}
