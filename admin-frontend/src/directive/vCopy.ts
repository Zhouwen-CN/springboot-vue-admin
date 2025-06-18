import type { Directive } from 'vue'

const copyText = (function () {
  if (navigator.clipboard) {
    return (text: string) => {
      navigator.clipboard.writeText(text)
    }
  } else {
    return (text: string) => {
      const textarea = document.createElement('textarea')
      textarea.value = text
      document.body.appendChild(textarea)
      textarea.select()
      document.execCommand('copy')
      document.body.removeChild(textarea)
    }
  }
})()

const vCopy: Directive<HTMLElement, string> = {
  // 指令绑定到元素时触发（类似 Vue2 的 mounted）
  mounted(el, binding) {
    // 定义点击事件处理函数（需保存引用以便卸载时移除）
    const value = binding.value
    if (!value || value.trim() === '') {
      return
    }

    function handleClick() {
      copyText(value)
      ElMessage.success('复制成功')
    }

    // 绑定点击事件
    el.addEventListener('click', handleClick)
    // 保存处理函数到元素，以便卸载时移除
    // @ts-ignore
    el._handleClick = handleClick
  },

  // 指令从元素卸载时触发（类似 Vue2 的 unmounted）
  unmounted(el) {
    // 移除事件监听，避免内存泄漏
    // @ts-ignore
    const handleClick = el._handleClick
    if (handleClick) {
      el.removeEventListener('click', handleClick)
      // @ts-ignore
      delete el._handleClick // 清理引用
    }
  }
}

export default vCopy
