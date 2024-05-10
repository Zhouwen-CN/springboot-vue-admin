<script lang="ts" setup>
import {ref} from 'vue'

interface Option {
  key: number
  label: string
  initial: string
}

const generateData = () => {
  const data: Option[] = []
  const states = [
    'California',
    'Illinois',
    'Maryland',
    'Texas',
    'Florida',
    'Colorado',
    'Connecticut ',
  ]
  const initials = ['CA', 'IL', 'MD', 'TX', 'FL', 'CO', 'CT']
  states.forEach((city, index) => {
    data.push({
      label: city,
      key: index,
      initial: initials[index],
    })
  })
  return data
}

const data = ref<Option[]>(generateData())
const value = ref([])

const filterMethod = (query: string, item: Option) => {
  return item.label.toLowerCase().startsWith(query.toLowerCase())
  // return item.initial.toLowerCase().includes(query.toLowerCase())
}
</script>

<template>
  <el-transfer v-model="value" :data="data" :filter-method="filterMethod" filterable/>
</template>

<style lang="scss" scoped></style>
