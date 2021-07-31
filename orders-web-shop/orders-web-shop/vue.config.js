
const VueLoaderPlugin = require('vue-loader/lib/plugin');

const path = require('path')

function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath: './',

  lintOnSave: true,
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@', resolve('src'))
  },
  assetsDir: 'static',
  configureWebpack: (config) => {
    // process.env为环境变量，分别对应.env.development文件和.env.production文件 此处表示加快开发环境打包速度
    if (process.env.NODE_ENV !== 'production') return;
    config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true;	//生产环境去掉console.log
    return {  // 此处配置webpack.config.js的相关配置
      plugins: [new VueLoaderPlugin()],
      performance: {}
    };
  }
}