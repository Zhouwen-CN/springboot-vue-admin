interface BaseResult {
    success: boolean
    code: number
    message: string
}

export interface ResultData<T = unknown> extends BaseResult {
    data: T
}
