<script setup>
import 'swagger-ui-dist/swagger-ui.css'
import {SwaggerUIBundle, SwaggerUIStandalonePreset} from "swagger-ui-dist"
import request from '@/api/request'

onMounted(async () => {

  const data = await request.get('/v3/api-docs')
  SwaggerUIBundle({
    spec: data,
    // url: "http://127.0.0.1:8080/v3/api-docs",
    // configUrl: "http://127.0.0.1:8080/v3/api-docs/swagger-config",
    dom_id: '#swagger-ui',
    deepLinking: true,
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