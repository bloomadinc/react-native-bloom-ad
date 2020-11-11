import {
  NativeModules,
  requireNativeComponent,
  View,
  findNodeHandle,
  UIManager,
  Text,
} from "react-native";
import React from "react";
import { getEventName } from "./utils";

const { BloomAd } = NativeModules;
const NEWS_PORTAL = "NewsPortal";
const BaseNewsPortal = requireNativeComponent(NEWS_PORTAL);

function withComponent(WrappedComponent, selectData = {}) {
  return class extends React.Component {
    nativeComponentRef;
    
    constructor(props) {
      super(props);
      let ratio = selectData.ratio || 1;
      let width = 332;
      let height = width / ratio;
      styleMap = props.style;
      if (styleMap) {
        width = styleMap.width || width;
        height = styleMap.height || width / ratio;
      }

      this.state = {
        width,
        height,
        appId: props.appId || "",
      };

      this.newsFun = {
        showNews: (param) => {
          BloomAd.showNews(unique, {
            countdownSeconds: param.countdownSeconds || 10,
            scrollEffectSeconds: param.scrollEffectSeconds || 3,
            rewardData: param.rewardData || 1,
          });
        },
        rewardNews: (param) => {
          BloomAd.rewardNews(unique, {
            reward: param.reward || false,
            rewardData: param.rewardData || 1,
          });
        },
      };
    }

    onChange = (event) => {
      // console.log(event.nativeEvent);
      if (!this.props.onChange) {
        return;
      }
      this.props.onChange(
        event.nativeEvent,
        Object.assign(event, this.newsFun)
      );
    };

    componentWillUnmount = () => {
      // BloomAd.destroyView(this.androidViewId.toString());
    };

    componentDidMount(){
      this.findId()
    }

    findId = () => {
      const androidViewId = findNodeHandle(this.nativeComponentRef);
      this.androidViewId = androidViewId
      console.log("androidViewId", androidViewId)
      UIManager.dispatchViewManagerCommand(
        androidViewId,
        UIManager.VideoStreaming.Commands.create.toString(),
        [androidViewId, this.state.appId]
      )
    }


    render() {
      // console.log("state", this.state, this.props);
      return (
        <View
          style={{
            ...this.props.style,
            alignItems: "center",
          }}
        >
          <WrappedComponent
            style={{
              width: this.state.width,
              height: this.state.height,
            }}
            onChange={this.onChange}
            ref={(nativeRef) => this.nativeComponentRef = nativeRef}
          />
        </View>
      );
    }
  };
}

export const NewsPortal = withComponent(BaseNewsPortal, {
  ratio: 1,
  name: NEWS_PORTAL,
});
