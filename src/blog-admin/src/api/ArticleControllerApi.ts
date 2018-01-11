import { JsonBase } from "./JsonBase";
import {
    callRestApiWithResult,
    callRestApiWithoutResult
} from "./RestApiCalls";
import { Endpoint } from "./Endpoint";
import { RequestId } from "./RequestId";
import { UserInfoDto } from "./UsersControllerApi";

export interface ArticleDto extends JsonBase {
    id: number;
    name: string;
    previewImage: string;
    content: string;
    author: UserInfoDto;
    categoryId: number;
    galleryId?: number;
}

export interface AddArticleDto extends JsonBase {
    name: string;
    previewImage: string;
    content: string;
    authorId: number;
    categoryId: number;
    galleryId?: number;
}

export interface ArticleListDto extends JsonBase {
    articles: ArticleDto[];
}


