interface BaseResult {
    success: boolean;
    code: number;
    message: string;
}

export type SingleResult<T = any> = T & BaseResult
export type ArrayResult<T = any> = T[] & BaseResult