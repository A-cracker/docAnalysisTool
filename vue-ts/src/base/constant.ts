/* eslint-disable no-unused-vars */
import _ from 'lodash';
const AID: string = '21419389378723787447';

const getActToolUrl = (): string => {
  const actToolUrl: string = (window as any).g_actToolUrl;
  if (process.env.NODE_ENV === 'development') {
    // 如果是在开发环境，由于actToolUrl是后端的IP和端口，需要替换为前端的IP和端口，这里只需要把这个url的http://localhost:8080/替换为空，前端会自动加上一个BASE_URL。    
    const newUrl = actToolUrl.replace(/http(s)?:\/\/[^:]+:\d+/, '');
    return newUrl;
  } else {
    return actToolUrl;
  }
};
/**
 * 是否是同一个部署
 */
export const isSameDeploy = (gid1: string, gid2: string) => {
  if (!gid1 || !gid2) {
    return false;
  }
  return (
    Math.floor(parseInt(gid1) / Math.pow(10, 8)) === Math.floor(parseInt(gid2) / Math.pow(10, 8))
  );
};

const getRunToolUrl = (): string => {
  return (window as any).g_runToolUrl;
};

const getForwardUrl = (): string => {
  return (window as any).g_forwardUrl;
};

const getRtParam = (): string => {
  return (window as any).g_rtParam;
};

const RESOURCE_URL: string = (window as any).g_resourceUrl;

const USER_ID: string = (window as any).g_userId;

const getAccessToken = (): string => {
  return (window as any).g_accessToken;
};

const getBandId = (): string => {
  return (window as any).g_bandId;
};

const PARAM: string = (window as any).g_param;

const CLIENTYPE: string = (window as any).g_clientType;

const TOOLID: string = (window as any).g_toolId;

const CORE_URL: string = (window as any).g_coreUrl;

/**
 * 获取吾托帮门户地址
 */
const GATEWAY_URL = (): string => {
  const domainUrl = _.truncate(getRunToolUrl(), {
    separator: 'tre',
    omission: ''
  });
  return domainUrl;
};

const TRE_URL: string = GATEWAY_URL + '/tre/';

const RUN_SYSTEMTOOL_URL: string = GATEWAY_URL + '/tre/runSystemTool?';

/**
 * 判断当前设备类型
 */
const isCurrentClient = (type: string) => {
  const ua = navigator.userAgent;
  const isWindowsPhone = /(?:Windows Phone)/.test(ua);
  const isSymbian = /(?:SymbianOS)/.test(ua) || isWindowsPhone;
  const isAndroid = /(?:Android)/.test(ua);
  const isFireFox = /(?:Firefox)/.test(ua);
  const isChrome = /(?:Chrome|CriOS)/.test(ua);
  // 平板
  const isTablet =
    /(?:iPad|PlayBook)/.test(ua) ||
    (isAndroid && !/(?:Mobile)/.test(ua)) ||
    (isFireFox && /(?:Tablet)/.test(ua));
  const isPhone = /(?:iPhone)/.test(ua) && !isTablet;
  const isPc = !isPhone && !isAndroid && !isSymbian;
  const isWx = /(MicroMessenger)/.test(ua);
  const isHtml5Plus = /(Html5Plus)/.test(ua);
  // 判断是否为微信浏览器
  if (isWx && type === 'wx') {
    return true;
  }

  if (isPhone && type === 'ios') {
    return true;
  }

  // 判断是否为安卓系统
  if (type === 'android' && isAndroid) {
    return true;
  }

  // 判断是否为APP客户端
  if (type === 'app' && (isAndroid || isPhone) && isHtml5Plus) {
    return true;
  }

  // 判断是否是移动端，移动端泛指APP客户端、移动端网页和微信浏览器
  if (type === 'mobile' && (isAndroid || isPhone || isTablet)) {
    return true;
  }

  if (type === 'desktop' && isPc) {
    return true;
  }
  return false;
};

export {
  isCurrentClient,
  GATEWAY_URL,
  RUN_SYSTEMTOOL_URL,
  getActToolUrl,
  getRunToolUrl,
  getForwardUrl,
  CLIENTYPE,
  CORE_URL,
  TRE_URL,
  AID,
  getRtParam,
  getBandId,
  getAccessToken
};
