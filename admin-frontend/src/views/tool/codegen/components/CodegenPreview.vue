<script lang="ts" setup>
import { type CodegenPreviewVo, reqGetCodegenPreview } from '@/api/tool/codegen'
import 'highlight.js/styles/github.css' // 导入代码高亮样式
import hljs from 'highlight.js' // 导入代码高亮文件
import java from 'highlight.js/lib/languages/java'
import xml from 'highlight.js/lib/languages/xml'
import html from 'highlight.js/lib/languages/vbscript-html'
import typescript from 'highlight.js/lib/languages/typescript'
import type { TabsPaneContext, TreeInstance } from 'element-plus'
import useAppStore from '@/stores/app'

// 目录树形结构
interface CongenDirTreeVo {
  id: string
  label: string
  parentId: string
  children: CongenDirTreeVo[]
}

// 默认左边 splitterPanel 大小
const defaultLeftPanelSize = 350
const leftPanelSize = ref(defaultLeftPanelSize)
// 切换对话框
const toggleDialog = ref(false)
// 代码预览响应列表
const codegenPreviewVoList = ref<CodegenPreviewVo[]>([])
// 代码预览对象
const preview = reactive<{
  dirTree: CongenDirTreeVo[]
  activeName: string
}>({
  dirTree: [],
  activeName: ''
})

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
  const data = result.data
  codegenPreviewVoList.value = data
  // 默认选中 tab、tree
  const activeName = data[0]?.filePath
  preview.activeName = activeName!
  treeRef.value?.setCurrentKey(activeName)

  const treeMap = getCongenDirTreeMap(data)
  preview.dirTree = getCongenDirTreeVoList(treeMap)
}

/**
 * 将 map 结构转换成目录树
 * @param treeMap map
 */
function getCongenDirTreeVoList(treeMap: Map<string, CongenDirTreeVo>) {
  const result: CongenDirTreeVo[] = []
  const voList = treeMap.values()

  for (const vo of voList) {
    const parentId = vo.parentId
    if (parentId === '/') {
      result.push(vo)
    }

    const parent = treeMap.get(parentId)
    if (parent) {
      parent.children.push(vo)
    }
  }

  return result
}

/**
 * 将文件路径切割，并存入 map 中
 * @param voList voList
 */
function getCongenDirTreeMap(voList: CodegenPreviewVo[]) {
  const result: Map<string, CongenDirTreeVo> = new Map()

  for (const item of voList) {
    const filePath = item.filePath
    const paths = filePath.split('/')
    for (let i = 0; i < paths.length; i++) {
      const path = paths[i]!
      let id = ''
      if (i < paths.length - 1) {
        id = paths.slice(0, i + 1).join('/')
      } else {
        id = filePath
      }

      if (result.has(id)) {
        continue
      }

      const label = path
      const parentId = i === 0 ? '/' : paths.slice(0, i).join('/')
      result.set(id, { id, label, parentId, children: [] })
    }
  }
  return result
}

// tree点击
function nodeClickHandle(data: CongenDirTreeVo, node: any) {
  if (node && !node.isLeaf) {
    return
  }
  preview.activeName = data.id
}

// tab 点击
const treeRef = ref<TreeInstance>()
function tabClickHandler(context: TabsPaneContext) {
  treeRef.value?.setCurrentKey(context.paneName)
}

// 恢复leftPanelSize默认值
function colse() {
  leftPanelSize.value = defaultLeftPanelSize
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
    style="height: 80vh"
    title="代码生成预览"
    top="10vh"
    width="80%"
    @close="colse"
  >
    <el-splitter style="height: calc(80vh - 40px - 32px)">
      <el-splitter-panel
        v-model:size="leftPanelSize"
        :min="300"
        v-if="useAppStore().device === 'desktop'"
      >
        <el-scrollbar>
          <el-tree
            ref="treeRef"
            :data="preview.dirTree"
            :expand-on-click-node="false"
            default-expand-all
            highlight-current
            node-key="id"
            @node-click="nodeClickHandle"
          />
        </el-scrollbar>
      </el-splitter-panel>
      <el-splitter-panel :min="600">
        <el-tabs v-model="preview.activeName" @tab-click="tabClickHandler">
          <el-tab-pane
            v-for="item in codegenPreviewVoList"
            :key="item.filePath"
            :label="item.filePath.substring(item.filePath.lastIndexOf('/') + 1)"
            :name="item.filePath"
          >
            <el-button v-copy="item.content" bg class="copy-btn" text type="primary">
              复制
            </el-button>
            <el-scrollbar height="calc(80vh - 40px - 32px - 60px)">
              <pre><code v-html="highlightedCode(item)" class="hljs"></code></pre>
            </el-scrollbar>
          </el-tab-pane>
        </el-tabs>
      </el-splitter-panel>
    </el-splitter>
  </el-dialog>
</template>

<style lang="scss" scoped>
.copy-btn {
  position: absolute;
  top: 0;
  left: 90%;
  z-index: 1;
}
</style>
