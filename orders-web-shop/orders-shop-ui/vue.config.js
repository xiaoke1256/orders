const { defineConfig } = require("@vue/cli-service");
/*
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/store_intra': {
        target: 'http://localhost:8763/store_intra/', // 目标服务器地址
        ws: true,
        changeOrigin: true, // 改变源
        pathRewrite: {
          '^/store_intra/': '' // 重写路径
        }
      }
    }
  }
});
*/

module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: process.env.VUE_APP_BASE_HOST,//'http://localhost:8763/store_intra',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    client: {
      overlay: false
    }
  }
}
