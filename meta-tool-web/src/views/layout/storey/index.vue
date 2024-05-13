<script lang="ts" setup>
function a() {
  return new Promise((resoleve, reject) => {
    resoleve(111)
  })

}

function b() {
  return new Promise((resoleve, reject) => {
    resoleve(222)
  })
}

a().then(res => {
  console.log(res);
})
b().then(res => {
  console.log(res);
})
</script>

<template>
  <h1>数仓层级管理</h1>
</template>

<style lang="scss" scoped></style>
