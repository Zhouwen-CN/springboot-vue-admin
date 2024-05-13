<script lang="ts" setup>
import {Delete, Edit, Plus} from '@element-plus/icons-vue';
import {reactive} from 'vue';
import useUserStore from '@/stores/user';

const toggleDialog = reactive({
  show: false,
  title: ''
})

// TODO: 待处理
const menuItemForm = reactive({} as any)


// 添加一级菜单
function addFirstLevelMenu() {
  toggleDialog.show = true
  toggleDialog.title = '添加一级菜单'
}

// 删除菜单
function confirmDeleteEvent(level: number, id: number) {
  if (level === 1) {
    console.log("删除一级菜单: id = %s", id);
  } else if (level === 2) {
    console.log("删除二级菜单: id = %s", id);
  }
}

// 打开对话框前的清理操作
function clean() {
  toggleDialog.title = ''
  menuItemForm.id = undefined
  menuItemForm.title = ''
  menuItemForm.accessPath = ''
  menuItemForm.filePath = ''
  menuItemForm.icon = ''
  menuItemForm.pid = undefined
}

</script>
<template>
  <div>
    <el-table :border="true" :data="useUserStore().userMenuInfo.menus" row-key="id">
      <el-table-column label="菜单名称" prop="title"/>
      <el-table-column label="访问路径" prop="accessPath"/>
      <el-table-column label="文件路径" prop="filePath"/>
      <el-table-column label="更新时间" prop="updateTime"/>
      <el-table-column align="center" label="图标" width="60">
        <template #default="{ row }">
          <el-icon :size="24">
            <component :is="row.icon"></component>
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column>
        <template #header>
          <el-button :icon="Plus" type="primary" @click="addFirstLevelMenu">添加一级菜单</el-button>
        </template>
        <template #default="{ $index, row }">
          <el-button-group>
            <el-button :disabled="row.level > 1" :icon="Plus" type="primary"/>
            <el-button :icon="Edit" type="primary"/>
            <el-popconfirm title="是否删除" @confirm="confirmDeleteEvent(row.level, row.id)">
              <template #reference>
                <el-button :disabled="row.level < 1 || row.children?.length > 0" :icon="Delete" type="danger"/>
              </template>
            </el-popconfirm>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog ref="dialog" v-model="toggleDialog.show" :title="toggleDialog.title" align-center width="500"
               @close="clean">
      <template #footer>
        <el-form :inline="false" :model="menuItemForm" label-width="80px">
          <el-form-item label="菜单名称">
            <el-input v-model="menuItemForm.title"></el-input>
          </el-form-item>
          <el-form-item label="访问路径">
            <el-input v-model="menuItemForm.accessPath"></el-input>
          </el-form-item>
          <el-form-item label="文件路径">
            <el-input v-model="menuItemForm.filePath"></el-input>
          </el-form-item>
          <el-form-item label="菜单图标">
            <el-input v-model="menuItemForm.icon"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button @click="toggleDialog.show = false">取消</el-button>
            <el-button type="primary" @click="toggleDialog.show = false">
              确认
            </el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped></style>
