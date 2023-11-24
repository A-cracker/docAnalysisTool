const IS_PRO = ['production', 'test'].includes(process.env.NODE_ENV);
const { defineConfig } = require('@vue/cli-service');

const AutoImport = require("unplugin-auto-import/webpack");
const Components = require("unplugin-vue-components/webpack");
const { ElementPlusResolver } = require("unplugin-vue-components/resolvers");

module.exports = defineConfig({
  configureWebpack: {
    plugins: [
      AutoImport({
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      }),
    ],
  },
  transpileDependencies: true,
  // 开启或者关闭eslint
  lintOnSave: false,
  productionSourceMap: false, // 不生成map
  publicPath: IS_PRO ? '${resourceUrl}' : '/',
  css: {
    extract: IS_PRO, // 是否使用css分离插件 ExtractTextPlugin
    sourceMap: false // 打包时不生成.map文件
  },
  chainWebpack: (config) => {
    if (IS_PRO) {
      config.plugin('html').tap((args) => {
        return [{ filename: 'pages/main.jsp', template: './public/main.html', title: 'XXX工具' }];
      });
    }
  },
  devServer: {
    port: 8000,
    proxy: {
      '/actRunTool': {
        target: 'http://127.0.0.1:8080', // 设置你调用的接口域名和端口号 别忘了加http
        changeOrigin: true, // 这里设置是否跨域
        pathRewrite: {
          '^/actRunTool': '/actRunTool' // 路径重写规则，将请求路径中的/actRunTool替换为/actRunTool，使代理的请求路径和目标路径相同
        },
        ws: false  
      }
    }
  }
});
