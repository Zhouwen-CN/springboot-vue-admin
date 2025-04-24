import {type LabelValueVo, reqGetDictDataListByTypeId} from '@/api/tool/dict'
import {type Ref} from 'vue'

export interface UseDictResult {
    dictData: Ref<LabelValueVo[]>
    dictMap: Ref<Map<number, string>>
    run: Function
}

/**
 * 有下拉菜单表单提交，才会使用字典
 * 像日志管理，有下拉列表，没有表单提交，这种最好不使用字典
 * @param type 字典类型
 * @returns 字典
 */
function useDict(type: string): UseDictResult {
    const dictData = ref<LabelValueVo[]>([])
    const dictMap = ref<Map<number, string>>(new Map<number, string>())

    function run() {
        reqGetDictDataListByTypeId(type)
            .then((res) => {
                dictData.value = res.data

                res.data.forEach((item) => {
                    dictMap.value.set(item.value, item.label)
                })
            })
            .catch((error) => {
                console.warn(error)
            })
    }

    return {dictData, dictMap, run}
}

export default useDict
