<script lang="ts" setup>
const route = useRoute()
const router = useRouter()

const { params, query } = route
const { path } = params

router.replace({ path: '/' + path, query })
</script>

<template>
  <div></div>
</template>

<style lang="scss" scoped></style>
