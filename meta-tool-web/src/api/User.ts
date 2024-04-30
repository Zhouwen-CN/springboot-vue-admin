import request from '@/api/Request'

export function getUserInfo() {
    return request('/api/rand.qinghua?format=json')
}
