<script lang="ts" setup>
import { type CodegenPreviewVo, reqGetCodegenPreview } from '@/api/tool/codegen'
import 'highlight.js/styles/github.css' // 导入代码高亮样式
import hljs from 'highlight.js' // 导入代码高亮文件
import java from 'highlight.js/lib/languages/java'
import xml from 'highlight.js/lib/languages/xml'
import html from 'highlight.js/lib/languages/vbscript-html'
import typescript from 'highlight.js/lib/languages/typescript'

// 树形结构对象
interface CodegenPreviewTree {
  id: string
  label: string
  children: CodegenPreviewTree[]
}

// 切换对话框
const toggleDialog = ref(false)
// 代码预览列表
const previewList = ref<CodegenPreviewVo[]>([])

/**
 * 代码高亮
 */
const highlightedCode = (item: CodegenPreviewVo) => {
  const language = item.filePath.substring(item.filePath.lastIndexOf('.') + 1)
  const result = hljs.highlight(item.content || '', {
    language,
    ignoreIllegals: true
  })
  return result.value
}

// 打开对话框
async function openDialog(id: number) {
  toggleDialog.value = true
  const result = await reqGetCodegenPreview(id)
  previewList.value = result.data
}

defineExpose({
  openDialog
})

onMounted(() => {
  hljs.registerLanguage('java', java)
  hljs.registerLanguage('xml', xml)
  hljs.registerLanguage('html', html)
  hljs.registerLanguage('vue', html)
  hljs.registerLanguage('ts', typescript)
})
</script>

<template>
  <el-dialog
    v-model="toggleDialog"
    title="代码生成预览"
    width="80%"
    style="height: 80vh"
    top="10vh"
  >
    <el-row>
      <el-col :span="8">
        <el-card body-style="height: calc(80vh - 40px - 32px);">
          <el-scrollbar>
            <div style="height: 1000px">左边</div>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card body-style="height: calc(80vh - 40px - 32px);">
          <el-tabs>
            <el-tab-pane
              v-for="item in previewList"
              :key="item.filePath"
              :label="item.filePath.substring(item.filePath.lastIndexOf('/') + 1)"
            >
              <el-scrollbar class="scrollbar">
                <pre><code v-html="highlightedCode(item)" class="hljs"></code></pre>
              </el-scrollbar>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<style lang="scss" scoped>
/**
  40px: 对话框头
  32px: 对话框padding
  40px: card padding
  60px: tabs 头
*/
.scrollbar {
  height: calc(80vh - 40px - 32px - 40px - 60px);
}
</style>
