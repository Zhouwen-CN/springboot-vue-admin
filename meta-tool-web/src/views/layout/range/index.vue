<script lang="ts" setup>
interface RoleCheck {
  id: number
  roleName: string
  checked: boolean
}

const roleChecks: RoleCheck[] = [
  {id: 1, roleName: 'admin', checked: false},
  {id: 2, roleName: 'user', checked: false},
  {id: 3, roleName: 'guest', checked: false}
]

const handleCheckChange = (roleId: number, checked: boolean) => {
  const index = roleChecks.findIndex(role => role.id === roleId)
  roleChecks[index].checked = checked
}

const a = '3chen'
console.log(a.indexOf('\b'));

</script>

<template>
  <div>
    <el-checkbox-group v-model="roleChecks">
      <el-checkbox v-for="role in roleChecks" :key="role.id" :label="role.roleName" :value="role.id"/>
    </el-checkbox-group>
  </div>
</template>

<style lang="scss" scoped></style>
