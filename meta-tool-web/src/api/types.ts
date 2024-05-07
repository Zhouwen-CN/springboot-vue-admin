export interface ResultData<T = unknown> {
    success: boolean
    code: number
    data: T
    message: string
}
