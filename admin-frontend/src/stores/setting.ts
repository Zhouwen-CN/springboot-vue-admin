const useSettingStore = defineStore(
  'PINIA:SETTING-STORE',
  () => {
    // 侧边栏折叠
    const collapse = ref(false)
    // 侧边栏隐藏
    const hidden = ref(false)

    const {
      VITE_APP_NAME: appName,
      VITE_APP_SHORT_NAME: appShortName,
      VITE_APP_PROJECT_LINK: projectLink,
      VITE_APP_AVATAR_URL: avatarUrl
    } = import.meta.env

    // 代码生成默认配置
    const codegenConfig = {
      javaBasePackage: 'com.yeeiee',
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
      htmlTypeList: ['input', 'textarea', 'select', 'switch', 'datetime'],
      selectConditionList: ['=', '>', '>=', '<', '<=', 'like']
    }

    function $reset() {
      collapse.value = false
      hidden.value = false
    }

    return {
      codegenConfig,
      collapse,
      hidden,
      appName,
      appShortName,
      projectLink,
      avatarUrl,
      $reset
    }
  },
  {
    persist: {
      storage: localStorage,
      pick: ['collapse', 'hidden']
    }
  }
)

export default useSettingStore
