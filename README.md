# Bloom AD React-Native SDK 接入文档

## 概述

本文档描述了 react-native 开发者如何集成 Bloom AD SDK（后面简称为 AdSdk），通过集成 AdSdk 展示广告创造收益。 AdSdk 提供了以下几种广告形式：SplashAd（开屏广告）、RewardVideoAd（激励视频广告）、BannerAd（横幅广告）、NativeExpressAd（原生广告）、InterstitialAd（插屏广告）、DrawVideoAd（Draw 视频广告）等。**暂时只支持 Android 客户端，ios 开发中。**

#### 术语介绍

AppId：应用程序 id，以 ba 开头的 18 位 hex 字符串，如 ba0063bfbc1a5ad878；

## SDK 集成

### 1、添加 NPM

`$ npm install react-native-bloom-ad --save`

或

`$ yarn add react-native-bloom-ad --save`

react native 版本号为 0.60 以下的需要手动 link

`$ react-native link react-native-bloom-ad`

### 2、添加 Maven 仓库地址

```
allprojects {
    repositories {
        maven {
            credentials {
                username 'iqLuKm'
                password 'pomH01oYcR'
            }
            url 'https://repo.rdc.aliyun.com/repository/117933-release-sPkE7F/'
        }
    }
}
```

## SDK 使用

```javascript
import BloomAd, {
  BannerView,
  NativeExpress,
  DrawVideo,
  VideoStreaming,
} from "react-native-bloom-ad";
```

## SDK 初始化

开发者需要初始化 AppId 后才能使用相关功能。例如：

```javascript
BloomAd.init("ba0063bfbc1a5ad878")
  .then((appId) => {
    // 初始化成功
    BloomAd.showSplash(); // 加载开屏
  })
  .catch((error) => {
    // 初始化失败
    console.log(error);
  });
```

登录时请设置 userId：

```
BloomAd.setUserId("uid");
```

退出登录请重置 userId：

```
BloomAd.setUserId(null);
```

## SDK 返回参数说明

params 参数说明：

| 参数    | 说明           | 类型   | 说明               |
| ------- | -------------- | ------ | ------------------ |
| type    | 广告状态       | string | -                  |
| id      | 广告 Id        | int    | -                  |
| code    | 返回的错误代码 | int    | type 为 onError 时 |
| message | 返回的错误消息 | string | type 为 onError 时 |

## 开屏广告接入

开屏广告以 App 启动作为曝光时机，提供 5s 的可感知广告展示。用户可以点击广告跳转到目标页面；或者点击右上角的“跳过”按钮，跳转到 app 主页。

使用接口调用，调用示例如下：

```javascript
const interval = 1000 * 60 * 3; // 设置时间间隔，单位是毫秒，切到后台后超过间隔返回时重新加载开屏
BloomAd.showSplash({
  unitId: "s1", // 广告位 id
  time: interval,
  onAdDismiss(params) {
    // 广告被关闭
    console.log(params);
  },
  onError(params) {
    // 广告出错
    console.log(params);
  },
});
```

**强烈建议**：App 切到后台超过三分钟返回时加载开屏，以提升开屏广告的曝光量.

## 激励视频广告接入

激励视频广告是指将短视频融入到 app 场景当中，用户观看完激励视频广告后可以得到一些应用内奖励。使用场景包括但不限于： 1、游戏等应用内观看视频广告获得游戏内金币等：用户必须观看完整视频才能获取奖励； 2、积分类应用接入。

使用接口调用，调用示例如下：

```javascript
BloomAd.rewardVideo({
  unitId: "rv1", // 广告位 id
  // showWhenCached: false, // 是否完全加载后才开始播放
  onAdLoad(params) {
    // 广告加载成功
    console.log(params);
  },
  onVideoCached(params) {
    // 视频素材缓存成功
    console.log(params);
  },
  onAdShow(params) {
    // 广告页面展示
    console.log(params);
  },
  onReward(params) {
    // 广告激励发放
    console.log(params);
  },
  onAdClick(params) {
    // 广告被点击
    console.log(params);
  },
  onVideoComplete(params) {
    // 广告播放完毕
    console.log(params);
  },
  onAdClose(params) {
    // 广告被关闭
    console.log(params);
  },
  onError(params) {
    // 广告出错
    console.log(params);
  },
});
```

## 横幅广告接入

Banner 广告（横幅广告）位于 app 顶部、中部、底部任意一处，横向贯穿整个 app 页面；当用户与 app 互动时，Banner 广告会停留在屏幕上，并可在一段时间后自动刷新。需要设置组件的 width 和 height。

使用组件方式调试，调用示例如下：

```javascript
const unitId = "b1"; // 广告位 id
<BannerView
  style={{
    width: 332,
    height: 52,
  }}
  unitId={unitId}
  onChange={(params) => {
    console.log(params);
  }}
/>;
```

params.type 说明：

| type      | 说明         |
| --------- | ------------ |
| onAdLoad  | 广告加载成功 |
| onAdShow  | 广告页面展示 |
| onAdClose | 广告被关闭   |
| onAdClick | 广告被点击   |
| onError   | 广告出错     |

## 原生广告接入

原生模板广告，支持图文和视频样式，开发者不用自行对广告样式进行编辑和渲染，可直接调用相关接口获取广告 view。需要设置组件的 width 和 height。由于原生广告高度不确定，可适当设置大一点。

使用组件方式调试，调用示例如下：

```javascript
const unitId = "n1"; // 广告位 id
<NativeExpress
  style={{
    width: 332,
    height: 200,
  }}
  unitId={unitId}
  count={1} // 请求广告数量
  onChange={(params) => {
    console.log(params);
  }}
/>;
```

params.type 说明：

| type      | 说明         |
| --------- | ------------ |
| onAdLoad  | 广告加载成功 |
| onAdShow  | 广告页面展示 |
| onAdClose | 广告被关闭   |
| onAdClick | 广告被点击   |
| onError   | 广告出错     |

## 插屏广告接入

插屏广告是移动广告的一种常见形式，在应用开流程中弹出，当应用展示插页式广告时，用户可以选择点击广告，访问其目标网址，也可以将其关闭，返回应用。

使用接口调用，调用示例如下：

```javascript
BloomAd.interstitial({
  unitId: "i1", // 广告位 id
  width: 300, // 插屏广告广告宽度
  onAdLoad(params) {
    // 广告加载成功
    console.log(params);
  },
  onAdShow(params) {
    // 广告页面展示
    console.log(params);
  },
  onAdClick(params) {
    // 广告被点击
    console.log(params);
  },
  onAdClose(params) {
    // 广告被关闭
    console.log(params);
  },
  onError(params) {
    // 广告出错
    console.log(params);
  },
});
```

## Draw 视频广告接入

竖版视频信息流广告，该广告适合在竖版全屏视频流中使用，接入方可以控制视频暂停或继续播放，默认视频播放不可干预。需要设置组件的 width 和 height。

使用组件方式调试，调用示例如下：

```javascript
const unitId = "dv1"; // 广告位 id
<DrawVideo
  style={{
    width: "100%",
    height: "100%",
    position: "absolute",
  }}
  unitId={unitId}
  count={1} // 请求广告数量
  onChange={(params) => {
    console.log(params);
  }}
/>;
```

params.type 说明：

| type            | 说明         |
| --------------- | ------------ |
| onAdLoad        | 广告加载成功 |
| onAdShow        | 广告页面展示 |
| onAdClick       | 广告被点击   |
| onVideoStart    | 广告播放开始 |
| onVideoPause    | 广告播放暂停 |
| onVideoResume   | 广告播放恢复 |
| onVideoComplete | 广告播放完毕 |
| onError         | 广告出错     |

## 视频流接入

开发者需要初始化 AppId 后才能使用相关功能。例如：

```javascript
<VideoStreaming
  appId="ba0063bfbc1a5ad878"
  style={{
    width: width,
    height: height,
    backgroundColor: "blue",
  }}
  play={true}
  onChange={(params) => {
    console.log("params", params);
  }}
/>
```

### params 参数说明：

| 参数      | 说明           | 类型   | 说明               |
| --------- | -------------- | ------ | ------------------ |
| type      | 广告状态       | string | -                  |
| id        | 视频 Id        | string | -                  |
| videoType | 视频类型       | string | 1:视频，2:广告     |
| code      | 返回的错误代码 | int    | type 为 onError 时 |
| message   | 返回的错误消息 | string | type 为 onError 时 |

params.type 说明：

| type            | 说明           |
| --------------- | -------------- |
| onVideoShow     | 视频切换展示   |
| onVideoStart    | 播放开始       |
| onVideoPause    | 播放暂停       |
| onVideoResume   | 播放恢复       |
| onVideoComplete | 播放完成       |
| onVideoError    | 播放出错       |
| onLikeClick     | 点赞或取消点赞 |
