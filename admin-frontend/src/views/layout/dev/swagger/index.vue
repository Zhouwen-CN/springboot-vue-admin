<script lang="ts" setup>

const iframeSrc = ref('')
onMounted(() => {
  iframeSrc.value = import.meta.env.VITE_APP_SERVER + '/swagger-ui/index.html'
})

</script>

<template>
  <iframe id="iframe" :src="iframeSrc">
  </iframe>
</template>

<style lang="scss" scoped>
#iframe {
  width: 100%;
  height: calc(100vh - $base_header_height - 40px - 4px);
}
</style>
