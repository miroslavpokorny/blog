import { DtoBase } from "./DtoBase";
import { callRestApiWithResult, callRestApiWithoutResult, callRestApiUploadFile } from "./RestApiCalls";
import { Endpoint } from "./Endpoint";
import { RequestByIdDto } from "./RequestId";
import { UserInfoDto } from "./UsersControllerApi";

export interface GalleryDto extends DtoBase {
    id: number;
    name: string;
    description: string;
    author: UserInfoDto;
}

export interface AddGalleryDto extends DtoBase {
    name: string;
    description: string;
    authorId: number;
}

export interface GalleryItemDto extends DtoBase {
    id: number;
    imageName: string;
    title: string;
    galleryId: number;
}

export interface GalleryListDto extends DtoBase {
    galleries: GalleryDto[];
}

export interface GalleryItemsListDto extends DtoBase {
    items: GalleryItemDto[];
}

export function GetGalleryListAction(callback: (error?: string | object, result?: GalleryListDto) => void) {
    callRestApiWithResult<GalleryListDto>(Endpoint.GalleryList, (error, result) => {
        if (error !== undefined) {
            return callback(error, undefined);
        }
        return callback(undefined, result);
    });
}

export function GetGalleryItemsListAction(
    id: number,
    callback: (error?: string | object, result?: GalleryItemsListDto) => void
) {
    const data: RequestByIdDto = {
        id: id
    };
    callRestApiWithResult<GalleryItemsListDto>(
        Endpoint.GalleryItemList,
        (error, result) => {
            if (error !== undefined) {
                return callback(error, undefined);
            }
            return callback(undefined, result);
        },
        data
    );
}

export function AddGalleryAction(
    name: string,
    description: string,
    authorId: number,
    callback: (error?: string | object) => void
) {
    const data: AddGalleryDto = {
        authorId: authorId,
        name: name,
        description: description
    };
    callRestApiWithoutResult(
        Endpoint.GalleryAdd,
        error => {
            callback(error);
        },
        data
    );
}

export function AddGalleryItemAction(galleryId: number, file: File, callback: (error?: string | object) => void) {
    callRestApiUploadFile(
        Endpoint.GalleryItemAdd,
        file,
        error => {
            callback(error);
        },
        galleryId
    );
}

export function EditGalleryAction(
    id: number,
    name: string,
    description: string,
    author: UserInfoDto,
    callback: (error?: string | object) => void
) {
    const data: GalleryDto = {
        id: id,
        name: name,
        description: description,
        author: author
    };
    callRestApiWithoutResult(
        Endpoint.GalleryEdit,
        error => {
            callback(error);
        },
        data
    );
}

export function EditGalleryItemAction(id: number, title: string, callback: (error?: string | object) => void) {
    const data: GalleryItemDto = {
        id: id,
        imageName: "",
        title: title,
        galleryId: 0
    };
    callRestApiWithoutResult(
        Endpoint.GalleryItemEdit,
        error => {
            callback(error);
        },
        data
    );
}

export function RemoveGalleryAction(galleryId: number, callback: (error?: string | object) => void) {
    const data: RequestByIdDto = {
        id: galleryId
    };
    callRestApiWithoutResult(
        Endpoint.GalleryRemove,
        error => {
            callback(error);
        },
        data
    );
}

export function RemoveGalleryItemAction(galleryItemId: number, callback: (error?: string | object) => void) {
    const data: RequestByIdDto = {
        id: galleryItemId
    };
    callRestApiWithoutResult(
        Endpoint.GalleryItemRemove,
        error => {
            callback(error);
        },
        data
    );
}
