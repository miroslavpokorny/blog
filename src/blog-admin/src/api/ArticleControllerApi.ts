import { DtoBase } from "./DtoBase";
import { Endpoint } from "./Endpoint";
import { UserInfoDto } from "./UsersControllerApi";
import { callRestApiWithoutResult, callRestApiWithResult, callRestApiUploadDataAndFile } from "./RestApiCalls";
import { RequestByIdDto } from "./RequestId";

export interface ArticleDto extends DtoBase {
    id: number;
    name: string;
    previewImage: string;
    content: string;
    author: UserInfoDto;
    categoryId: number;
    galleryId?: number;
}

export interface AddArticleDto extends DtoBase {
    name: string;
    content: string;
    authorId: number;
    categoryId: number;
    galleryId?: number;
}

export interface EditArticleDto extends DtoBase {
    id: number;
    name: string;
    content: string;
    categoryId: number;
    galleryId: number;
}

export interface ArticleListDto extends DtoBase {
    articles: ArticleDto[];
}

export function GetArticlesListAction(callback: (error?: string | object, result?: ArticleListDto) => void) {
    callRestApiWithResult<ArticleListDto>(Endpoint.ArticleList, (error, result) => {
        if (error !== undefined) {
            return callback(error, undefined);
        }
        return callback(undefined, result);
    });
}

export function AddArticleAction(
    name: string,
    content: string,
    authorId: number,
    categoryId: number,
    galleryId: number,
    previewImage: File,
    callback: (error?: string | object) => void
) {
    const json: AddArticleDto = {
        name: name,
        content: content,
        authorId: authorId,
        categoryId: categoryId,
        galleryId: galleryId
    };
    callRestApiUploadDataAndFile(
        Endpoint.ArticleAdd,
        error => {
            callback(error);
        },
        previewImage,
        json
    );
}

export function RemoveArticleAction(id: number, callback: (error?: string | object) => void) {
    const data: RequestByIdDto = {
        id: id
    };
    callRestApiWithoutResult(
        Endpoint.ArticleRemove,
        error => {
            callback(error);
        },
        data
    );
}

export function EditArticleAction(
    articleId: number,
    name: string,
    content: string,
    categoryId: number,
    galleryId: number,
    previewImage: File | undefined,
    callback: (error?: string | object) => void
) {
    const json: EditArticleDto = {
        galleryId: galleryId,
        categoryId: categoryId,
        content: content,
        name: name,
        id: articleId
    };
    if (previewImage !== undefined) {
        callRestApiUploadDataAndFile(
            Endpoint.ArticleEdit,
            error => {
                callback(error);
            },
            previewImage,
            json
        );
        return;
    }
    callRestApiWithoutResult(
        Endpoint.ArticleEdit,
        error => {
            callback(error);
        },
        json
    );
}
