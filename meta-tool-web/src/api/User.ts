import { request } from '@/api/request'

export function getUserInfo() {
  return request('/api/rand.qinghua?format=json')
}
