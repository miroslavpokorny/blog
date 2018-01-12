import { DtoBase } from "./DtoBase";
// import { callRestApiWithResult, callRestApiWithoutResult } from "./RestApiCalls";
// import { Endpoint } from "./Endpoint";
// import { RequestByIdDto } from "./RequestId";
import { UserInfoDto } from "./UsersControllerApi";

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
    previewImage: string;
    content: string;
    authorId: number;
    categoryId: number;
    galleryId?: number;
}

export interface ArticleListDto extends DtoBase {
    articles: ArticleDto[];
}
