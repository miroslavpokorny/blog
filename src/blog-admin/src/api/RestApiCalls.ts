import { State } from '../BlogAdminStore';
import { JsonBase } from './JsonBase';
import axios from 'axios';

interface ErrorMessageJson extends JsonBase {
    message: string;
    code: string;
}

export function callRestApiWithResult<T extends JsonBase | ErrorMessageJson>(
    endpointPath: string, 
    callback: (error?: string, result?: T) => void,
    data?: object) {
        const endpoint = State.endpoint + endpointPath;
        State.isLoading = true;
        axios.post<T>(endpoint, data, {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            params: {
                tokenId: getTokenId()
            }
        }).then((response) => {
            State.isLoading = false;
            if (response.data.type !== undefined) {
                return response.data.type === 'ErrorMessageJson' 
                    ? callback((response.data as ErrorMessageJson).message)
                    : callback(undefined, response.data);
            }
            return callback('Unknown error!');
        }).catch((error) => {
            State.isLoading = false;
            if (error.response !== undefined && 
                error.response.data !== undefined && 
                error.response.data.type !== undefined &&
                error.response.data.type === 'ErrorMessageJson') {
                return callback(error.response.data.message);
            } else if (error.message !== undefined) {
                return callback(error.message);
            }
            return callback(JSON.stringify(error));
        });
    }    

export function callRestApiWithoutResult (
    endpointPath: string,
    callback: (error?: string) => void) {
        const endpoint = State.endpoint + endpointPath;
        State.isLoading = true;
        axios.get(endpoint, {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            params: {
                tokenId: getTokenId()
            }
        }).then((response) => {
            State.isLoading = false;
            return callback();
        }).catch((error) => {
            State.isLoading = false;
            if (error.response !== undefined && 
                error.response.data !== undefined && 
                error.response.data.type !== undefined &&
                error.response.data.type === 'ErrorMessageJson') {
                return callback(error.response.data.message);
            } else if (error.message !== undefined) {
                return callback(error.message);
            }
            return callback(JSON.stringify(error));
        });
    }

function getTokenId() {
    return State.loggedUser.tokenId !== undefined && State.loggedUser.tokenId.trim().length > 0 
        ? State.loggedUser.tokenId 
        : null;
}