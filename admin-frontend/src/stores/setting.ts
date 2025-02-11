const useSettingStore = defineStore('setting', () => {
  // 侧边栏折叠
  const collapse = ref(false)

  const {
    VITE_APP_NAME: appName,
    VITE_APP_SHORT_NAME: appShortName,
    VITE_APP_GITEE_LINK: giteeLink,
    VITE_APP_AVATAR_URL: avatarUrl
  } = import.meta.env

  return {collapse, appName, appShortName, giteeLink, avatarUrl}
})

export default useSettingStore
