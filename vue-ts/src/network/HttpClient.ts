import axios, { AxiosError, AxiosResponse } from 'axios';
import qs from 'qs';
import { getActToolUrl } from '@/base/constant';
// 定义参数的格式：{param1:value1, param2:value2}
interface params {
  [index: string]: unknown;
}
export class HttpClient {
  // 熟悉请求后端之后，可按照自己的编码风格进行修改
  static post(action: string, params: params = {}) {
    // console.log(getActToolUrl())
    // console.log(qs.stringify(params))
    params.action = action;
    return axios
      .post(getActToolUrl(), qs.stringify(params))
      .then((res: AxiosResponse) => res.data)
      .catch((err: AxiosError) => err.response);
  }
  static get(action: string, params: params = {}) {
    console.log(getActToolUrl())
    console.log(qs.stringify(params))
    params.action = action;
    return axios
        .get(getActToolUrl(), params)
        .then((res: AxiosResponse) => res.data)
        .catch((err: AxiosError) => err.response);
  }
  static put(action: string, params: params = {}) {
    console.log(getActToolUrl())
    console.log(qs.stringify(params))
    params.action = action;
    return axios
        .put(getActToolUrl(), qs.stringify(params))
        .then((res: AxiosResponse) => res.data)
        .catch((err: AxiosError) => err.response);
  }
}
