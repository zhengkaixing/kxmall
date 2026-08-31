// ESlint 检查配置
module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2021: true
  },
  extends: ['plugin:vue/vue3-recommended', 'eslint:recommended'],
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module'
  },
  globals: {
    defineProps: 'readonly',
    defineEmits: 'readonly',
    defineExpose: 'readonly',
    withDefaults: 'readonly'
  },
  rules: {
    'vue/max-attributes-per-line': [2, {
      singleline: { max: 10 },
      multiline: { max: 1 }
    }],
    'vue/singleline-html-element-content-newline': 'off',
    'vue/multiline-html-element-content-newline': 'off',
    'vue/multi-word-component-names': 'off',
    'vue/no-v-html': 'off',
    'vue/no-mutating-props': 'off',
    'vue/require-default-prop': 'off',
    'vue/require-explicit-emits': 'off',
    'no-console': 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 2 : 0,
    'no-unused-vars': [2, {
      vars: 'all',
      args: 'none'
    }],
    quotes: [2, 'single', {
      avoidEscape: true,
      allowTemplateLiterals: true
    }],
    semi: [2, 'never'],
    eqeqeq: ['error', 'always', { null: 'ignore' }]
  }
}
