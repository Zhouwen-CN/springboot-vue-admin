/**
 * 请求api
 *    1、如果需要状态存储，使用 request
 *    2、如果是分页，使用 usePagination
 *    3、如果需要按钮 loading，使用 useRequest 包装 request
 * 页面
 *    动态路由视图组件命名规则：/views/layout/${ComponentName}/index.vue
 *    方便插件定位，只要输入index就可以定位到当前页面组件
 */

export interface ResultData<T = unknown> {
  success: boolean
  code: number
  data: T
  message: string
}

export interface CreateAndUpdateTime {
  createTime?: string
  updateTime?: string
}

export interface Page<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}
