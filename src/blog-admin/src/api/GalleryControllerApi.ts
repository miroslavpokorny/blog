import { JsonBase } from "./JsonBase";
import { callRestApiWithResult, callRestApiWithoutResult } from "./RestApiCalls";
import { Endpoint } from "./Endpoint";
import { RequestId } from "./RequestId";
import { UserInfoDto } from "./UsersControllerApi";

export interface GalleryDto extends JsonBase {
    id: number;
    name: string;
    description: string;
    author: UserInfoDto;
}

export interface AddGalleryDto extends JsonBase {
    name: string;
    description: string;
    authorId: number;
}

export interface GalleryItemDto extends JsonBase {
    id: number;
    imageName: string;
    title: string;
    galleryId: number;
}

export interface GalleryListDto extends JsonBase {
    galleries: GalleryDto[];
}

export interface GalleryItemsListDto extends JsonBase {
    items: GalleryItemDto[];
}

// TODO upload of file
// export interface AddGalleryItemDto extends JsonBase {
//     galleryId: number;
//     imageName: string;
// }

export function GetGalleryListAction(callback: (error?: string | object, result?: GalleryListDto) => void) {
    callRestApiWithResult<GalleryListDto>(Endpoint.GalleryList, (error, result) => {
        if (error !== undefined) {
            return callback(error, undefined);
        }
        return callback(undefined, result);
    });
}

export function GetGalleryItemsListAction(callback: (error?: string | object, result?: GalleryItemsListDto) => void) {
    callRestApiWithResult<GalleryItemsListDto>(Endpoint.GalleryItemList, (error, result) => {
        if (error !== undefined) {
            return callback(error, undefined);
        }
        return callback(undefined, result);
    });
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

// TODO upload of file
// export function AddGalleryItemAction()

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

export function EditGalleryItemAction(
    id: number,
    imageName: string,
    title: string,
    galleryId: number,
    callback: (error?: string | object) => void
) {
    const data: GalleryItemDto = {
        id: id,
        imageName: imageName,
        title: title,
        galleryId: galleryId
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
    const data: RequestId = {
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
    const data: RequestId = {
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
