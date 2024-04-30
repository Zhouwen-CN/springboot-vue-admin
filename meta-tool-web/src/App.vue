<script setup lang="ts">
import {RouterView} from 'vue-router'
</script>

<template>
  <RouterLink to="/layout/home">go home</RouterLink>
  <br/><br/>
  <RouterLink to="/login">go login</RouterLink>
  <br/><br/>
  <RouterView></RouterView>
</template>

<style lang="scss"></style>