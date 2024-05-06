import request from '@/api/request'

export function getUserInfo() {
    return request.get('/api/rand.qinghua?format=json')
}
