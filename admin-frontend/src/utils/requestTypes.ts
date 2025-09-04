/**
 * 请求api
 *    1、如果需要状态存储，使用 request
 *    2、如果是分页，使用 usePagination
 *    3、如果请求有顺序，则使用request + async await（比如更新数据后重新分页）
 * 页面
 *    动态路由视图组件命名规则：/views/${ComponentName}/index.vue
 */

export interface ResultData<T = unknown> {
  success: boolean
  code: number
  data: T
  message: string
}

export interface BaseVo {
  createUser?: string
  createTime?: string
  updateUser?: string
  updateTime?: string
}

export interface Page<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}
