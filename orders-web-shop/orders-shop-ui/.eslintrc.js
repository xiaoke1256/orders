module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    "plugin:vue/vue3-essential",
    "eslint:recommended",
    "@vue/typescript/recommended",
    "plugin:prettier/recommended",
  ],
  parserOptions: {
    /*
    "ecmaVersion": "2020",
    "sourceType": "module",
    "vueFeatures": {
      "filter": true,
      "interpolationAsNonHTML": false,
    },*/
    "ecmaFeatures": {
      "jsx": false // 关闭JSX语法支持
    }
  },
  //plugins: ['vue', '@typescript-eslint', 'prettier'],
  rules: {
    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
    "prettier/prettier": "off",
    '@typescript-eslint/no-empty-function': 'off',
  },
};
