<script lang="ts" setup>
import {ref} from 'vue';
import IconSelect from '@/components/IconSelect.vue';

const value = ref('')


</script>
<template>
  <div>
    <h1>数据域管理</h1>
    <IconSelect v-model="value"/>
  </div>
</template>

<style lang="scss" scoped></style>
