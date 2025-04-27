<script setup>
import 'swagger-ui/dist/swagger-ui.css'
import SwaggerUI from 'swagger-ui'
import request from '@/utils/request'

// 不用iframe是因为不好做鉴权
onMounted(async () => {
  const data = await request.get('/v3/api-docs')
  SwaggerUI({
    spec: data,
    dom_id: '#swagger-ui',
    docExpansion: 'none'
  })
})
</script>

<template>
  <div id="swagger-ui"></div>
</template>

<style scoped></style>