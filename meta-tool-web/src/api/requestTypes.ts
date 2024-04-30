interface BaseResult {
  success: boolean
  code: number
  message: string
}

export interface SingleResult<T = unknown> extends BaseResult {
  data: T
}

export interface ArrayResult<T = unknown> extends BaseResult {
  data: T[]
}
