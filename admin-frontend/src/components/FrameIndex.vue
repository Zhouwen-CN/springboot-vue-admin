<script lang="ts" setup>
defineProps({
  url: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <iframe :src="url" class="iframe" frameborder="0"></iframe>
</template>

<style lang="scss" scoped>
.iframe {
  width: 100%;
  height: calc(100vh - $base_header_height - 40px - 4px);
}
</style>
