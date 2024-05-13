import type {CreateAndUpdateTime} from '@/api/types'
import useRequest from '@/hooks/useRequest'

export interface Role extends CreateAndUpdateTime {
    id: number
    roleName: string
}

export function reqGetRoles() {
    return useRequest<Role[]>({url: '/role'})
}
