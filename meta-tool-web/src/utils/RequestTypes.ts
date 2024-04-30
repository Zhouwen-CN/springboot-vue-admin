interface BaseResult {
    success: boolean
    code: number
    message: string
}

export interface SingleResult<T = any> extends BaseResult {
    data: T
}

export interface ArrayResult<T = any> extends BaseResult {
    data: T[]
}

// type Method = 'get' | 'post' | 'put' | 'delete'
// type Headers = Record<string, string>
// export type Params = Record<string, any>

// export interface Config {
//   url: string
//   method?: Method
//   params?: Params
//   immediate?: boolean
//   headers?: Headers
// }
