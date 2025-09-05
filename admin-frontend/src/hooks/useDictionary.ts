import { type DictDataSelectorVo, reqGetDictDataListByTypeId } from '@/api/tool/dict'
import { type Ref } from 'vue'

export interface UseDictResult {
  dictData: Ref<DictDataSelectorVo[]>
  dictMap: Ref<Map<number, string>>
  run: Function
}

/**
 * 有下拉菜单表单提交，才会使用字典
 * 像日志管理，有下拉列表，没有表单提交，这种最好不使用字典
 * @param typeId 字典类型Id
 * @param useMap 是否使用字典映射
 * @returns 字典
 */
function useDict(typeId: number, useMap = true): UseDictResult {
  const dictData = ref<DictDataSelectorVo[]>([])
  const dictMap = ref<Map<number, string>>(new Map<number, string>())

  function run() {
    reqGetDictDataListByTypeId(typeId)
      .then((res) => {
        dictData.value = res.data
        if (useMap) {
          res.data.forEach((item) => {
            dictMap.value.set(item.data, item.label)
          })
        }
      })
      .catch((error) => {
        console.warn(error)
      })
  }

  return { dictData, dictMap, run }
}

export default useDict
