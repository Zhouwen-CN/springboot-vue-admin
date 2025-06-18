import { ElIcon, ElTooltip } from 'element-plus'
import type { Directive } from 'vue'
import { QuestionFilled } from '@element-plus/icons-vue'

/**
 * 自定义指令，给表单项添加提示
 */
const vTip: Directive<HTMLElement, string> = {
  updated: (el, binding) => {
    // 是否绑定了值
    const value = binding.value
    if (!value || value.trim() === '') {
      return
    }

    // 是否已经创建提示元素
    const exists = el.querySelector('.el-tooltip__trigger')
    if (exists) {
      return
    }

    // 获取label元素
    const target = el.querySelector('.el-form-item__label')
    if (!target) {
      return
    }

    // 渲染
    render(target, value)
  }
}

function render(target: Element, tip: string) {
  const app = createApp({
    render: () =>
      h(
        ElTooltip,
        {
          'append-to': target,
          content: tip,
          placement: 'top'
        },
        {
          default: () => h(ElIcon, {}, () => [h(QuestionFilled)])
        }
      )
  })

  const div = document.createElement('div')
  div.style.marginLeft = '1px'
  app.mount(div)
  target.appendChild(div)
}

export default vTip
