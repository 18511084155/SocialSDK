/*
 * Copyright (C) 2015 Socialbili <jungly.ik@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.woodys.socialsdk.share.core.error;

/**
 * @author woodys
 *
 * @since 2015/9/31
 */
public class SocialShareStatusCode {

    public static final int ST_CODE_SUCCESSED = 200;
    public static final int ST_CODE_ERROR_CANCEL = 201;
    public static final int ST_CODE_ERROR = 202;

    public static final int ST_CODE_SHARE_ERROR_NOT_CONFIG = -233;//没有配置appkey, appId
    public static final int ST_CODE_SHARE_ERROR_NOT_INSTALL = -234;//第三方软件未安装
    public static final int ST_CODE_SHARE_ERROR_PARAM_INVALID = -235;//ShareParam参数不正确
    public static final int ST_CODE_SHARE_ERROR_EXCEPTION = -236;//异常
    public static final int ST_CODE_SHARE_ERROR_UNEXPLAINED = -237;
    public static final int ST_CODE_SHARE_ERROR_SHARE_FAILED = -238;
    public static final int ST_CODE_SHARE_ERROR_AUTH_FAILED = -239;//认证失败
    public static final int ST_CODE_SHARE_ERROR_CONTEXT_TYPE = -240;//上下文类型和需求不负
    public static final int ST_CODE_SHARE_ERROR_PARAM_UNSUPPORTED = -241;//上下文类型和需求不负
    public static final int ST_CODE_SHARE_ERROR_IMAGE = -242;//图片处理失败

}