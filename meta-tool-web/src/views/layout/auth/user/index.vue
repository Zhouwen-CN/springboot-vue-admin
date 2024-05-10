<script lang="ts" setup>
import {ref} from 'vue'
import {ElTreeV2} from 'element-plus'
import type {TreeNodeData} from 'element-plus/es/components/tree-v2/src/types.mjs';

interface Tree {
  id: string
  label: string
  children?: Tree[]
}

const getKey = (prefix: string, id: number) => {
  return `${prefix}-${id}`
}

const createData = (
    maxDeep: number,
    maxChildren: number,
    minNodesNumber: number,
    deep = 1,
    key = 'node'
): Tree[] => {
  let id = 0
  return Array.from({length: minNodesNumber})
      .fill(deep)
      .map(() => {
        const childrenNumber =
            deep === maxDeep ? 0 : Math.round(Math.random() * maxChildren)
        const nodeKey = getKey(key, ++id)
        return {
          id: nodeKey,
          label: nodeKey,
          children: childrenNumber
              ? createData(maxDeep, maxChildren, childrenNumber, deep + 1, nodeKey)
              : undefined,
        }
      })
}

const query = ref('')
const treeRef = ref<InstanceType<typeof ElTreeV2>>()
const data = createData(2, 3, 3)
const props = {
  value: 'id',
  label: 'label',
  children: 'children',
}

const onQueryChanged = (query: string) => {
  treeRef.value!.filter(query)
}
const filterMethod = (query: string, node: TreeNodeData) => {
  return node.label!.includes(query)
}

function submit() {
  console.log(data);

}
</script>

<template>
  <div>
    <el-input v-model="query" placeholder="Please enter keyword" style="width: 240px" @input="onQueryChanged"/>
    <el-tree-v2 ref="treeRef" :data="data" :filter-method="filterMethod" :height="208" :props="props" show-checkbox
                style="max-width: 600px">
      <template #default="{ node }">
        <span :class="{ 'is-leaf': node.isLeaf }" class="prefix">[ElementPlus]</span>
        <span>{{ node.label }}</span>
      </template>
    </el-tree-v2>
    <el-button size="default" type="primary" @click="submit">点我</el-button>
  </div>
</template>

<style lang="scss" scoped></style>