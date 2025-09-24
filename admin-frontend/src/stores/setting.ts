const useSettingStore = defineStore(
  'PINIA:SETTING-STORE',
  () => {
    // 侧边栏折叠
    const collapse = ref(false)

    const {
      VITE_APP_NAME: appName,
      VITE_APP_SHORT_NAME: appShortName,
      VITE_APP_GITEE_LINK: giteeLink,
      VITE_APP_AVATAR_URL: avatarUrl
    } = import.meta.env

    // 代码生成默认配置
    const codegenConfig = {
      author: 'chen',
      ignoreTablePrefix: 't_',
      ignoreColumnPrefix: undefined,
      jsTypeList: ['string', 'number', 'boolean'],
      javaTypeList: [
        'Long',
        'String',
        'Integer',
        'Double',
        'BigDecimal',
        'LocalDateTime',
        'Boolean'
      ],
      htmlTypeList: ['input', 'textarea', 'select', 'radio', 'datetime'],
      selectConditionList: ['=', '>', '>=', '<', '<=', 'like']
    }

    function $reset() {
      collapse.value = false
    }

    return { codegenConfig, collapse, appName, appShortName, giteeLink, avatarUrl, $reset }
  },
  {
    persist: {
      paths: ['collapse']
    }
  }
)

export default useSettingStore
