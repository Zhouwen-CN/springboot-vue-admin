<script setup>
import 'swagger-ui-dist/swagger-ui.css'
import {SwaggerUIBundle, SwaggerUIStandalonePreset} from "swagger-ui-dist"
import request from '@/utils/request'

// 不用iframe是因为不好做鉴权
onMounted(async () => {
  const data = await request.get('/v3/api-docs')
  SwaggerUIBundle({
    spec: data,
    dom_id: '#swagger-ui',
    docExpansion: 'none',
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  })
})
</script>

<template>
  <div id="swagger-ui"></div>
</template>

<style scoped></style>