const fs = require('fs');
require('path');
// 修改app.js中的${resourceUrl}
const _assetsPath = './dist/js/';
const _assetsJSPPath = './dist/pages/main.jsp';
let appJsName;

function modifyJs() {
  const files = fs.readdirSync(_assetsPath);
  files.forEach(function (val) {
    if (val.indexOf('app.') !== -1) {
      appJsName = val;
    }
  });

  const jsName = _assetsPath + appJsName;

  fs.readFile(jsName, (err, data) => {
    if (err) throw err;

    let newContent = data.toString();
    // eslint-disable-next-line no-template-curly-in-string
    newContent = newContent.replace(/"\${resourceUrl}\/"/g, 'g_resourceUrl');

    fs.writeFile(jsName, newContent, () => {
      console.log(`'修改 ${jsName} 完成'`);
    });
  });
}
function modifyMainJSP() {
  fs.readFile(_assetsJSPPath, (err, data) => {
    if (err) throw err;
    let newContent = data.toString();
    // eslint-disable-next-line no-template-curly-in-string
    const header = '<%@ page import="java.util.*" contentType="text/html; charset=utf-8" %>';
    newContent = header + newContent;
    fs.writeFile(_assetsJSPPath, newContent, () => {
      console.log(`'修改 ${_assetsJSPPath} 完成'`);
    });
  });
}
modifyJs();
modifyMainJSP();
