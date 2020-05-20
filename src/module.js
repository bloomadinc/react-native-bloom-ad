import { NativeModules, NativeEventEmitter } from "react-native";
import { setInit, checkInit, getInit, getEventName } from "./utils";

const { BloomAd } = NativeModules;
const eventEmitter = new NativeEventEmitter(BloomAd);
const SplashTime = 1000 * 60 * 3;

const REWAED_VIDEO = "rewardVideo";
const SHOW_SPLASH = "showSplash";
const INTERSTITIAL = "interstitial";

export default {
  init(appId) {
    return new Promise((resolve, reject) => {
      let isInit = getInit();
      if (!isInit && typeof appId === "string" && appId.length > 0) {
        BloomAd.init(appId)
          .then(() => {
            setInit(true);
            resolve(appId);
          })
          .catch(reject);
      } else if (isInit) {
        resolve(appId);
      } else {
        console.error("appid not null");
        reject(new Error("appid not null"));
      }
    });
  },
  setUserId(userId) {
    if (checkInit()) {
      BloomAd.setUserId(userId);
    }
  },
  baseModule(actionName, params, ...args) {
    if (checkInit()) {
      let eventName = getEventName(actionName);
      let emiter = eventEmitter.addListener(eventName, (data) => {
        console.log(eventName, data);
        if (params[data.type]) {
          params[data.type](data);
        }
      });
      console.log("args:", ...args);
      BloomAd[actionName](eventName, ...args);
      return emiter;
    }
  },
  rewardVideo(params = {}, unitId = "rv1") {
    return this.baseModule(REWAED_VIDEO, params, unitId);
  },
  showSplash(time = SplashTime, params = {}, unitId = "s1") {
    return this.baseModule(SHOW_SPLASH, params, time, unitId);
  },
  interstitial(width = 300, params = {}, unitId = "i1") {
    return this.baseModule(INTERSTITIAL, params, width, unitId);
  },
};
