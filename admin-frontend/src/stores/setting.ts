const useSettingStore = defineStore(
  'PINIA:SETTING-STORE',
  () => {
    // 侧边栏折叠
    const sidebarCollapse = ref(false)
    // 侧边栏隐藏
    const sidebarHidden = ref(false)
    // 会话列表是否可见
    const conversationVisible = ref(false)

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
      sidebarCollapse.value = false
      sidebarHidden.value = false
    }

    return {
      codegenConfig,
      sidebarCollapse,
      sidebarHidden,
      conversationVisible,
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
      pick: ['sidebarCollapse', 'sidebarHidden']
    }
  }
)

export default useSettingStore
