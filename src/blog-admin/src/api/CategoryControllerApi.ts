import { JsonBase } from "./JsonBase";
import {
    callRestApiWithResult,
    callRestApiWithoutResult
} from "./RestApiCalls";
import { Endpoint } from "./Endpoint";
import { RequestId } from "./RequestId";

export interface CategoryDto extends JsonBase {
    name: string;
    description: string;
    id: number;
}

export interface AddCategoryDto extends JsonBase {
    name: string;
    description: string;
}

export interface CategoryListDto extends JsonBase {
    categories: CategoryDto[];
}

export function GetCategoriesListAction(
    callback: (error?: string | object, result?: CategoryListDto) => void
) {
    callRestApiWithResult<CategoryListDto>(Endpoint.CategoryList, (error, result) => {
        if (error !== undefined) {
            return callback(error, undefined);
        }
        return callback(undefined, result);
    });
}

export function AddCategoryAction(
    name: string,
    description: string,
    callback: (error?: string | object) => void
) {
    const data: AddCategoryDto = {
        name: name,
        description: description
    };
    callRestApiWithoutResult(
        Endpoint.CategoryAdd,
        error => {
            callback(error);
        },
        data
    );
}

export function RemoveCategoryAction(
    id: number,
    callback: (error?: string | object) => void
) {
    const data: RequestId = {
        id: id
    };
    callRestApiWithoutResult(
        Endpoint.CategoryRemove,
        error => {
            callback(error);
        },
        data
    );
}

export function EditCategoryAction(id: number, name: string, description: string, callback: (error?: string | object) => void) {
    const data: CategoryDto = {
        id: id,
        name: name,
        description: description
    };
    callRestApiWithoutResult(
        Endpoint.CategoryEdit, 
        error => {
            callback(error);
        }, 
        data
    );
}
