import {
  NativeModules,
  requireNativeComponent,
  View,
  Text,
} from "react-native";
import React from "react";
import { getEventName, checkInit } from "./utils";

const { BloomAd } = NativeModules;
const BANNER_VIEW = "BannerView";
const NATIVE_EXPRESS = "NativeExpress";
const DRAW_VIDEO = "DrawVideo";
const BaseBanner = requireNativeComponent(BANNER_VIEW);
const BaseNativeExpress = requireNativeComponent(NATIVE_EXPRESS);
const BaseDrawVideo = requireNativeComponent(DRAW_VIDEO);

function withComponent(WrappedComponent, selectData = {}) {
  return class extends React.Component {
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

      let unique = getEventName(selectData.name);
      console.log("count:", typeof props.count);

      this.state = {
        width,
        height,
        unique,
        unitId: props.unitId || selectData.unitId,
        count: props.count || 1,
      };
    }
    onChange = (event) => {
      // console.log(event.nativeEvent);
      if (!this.props.onChange) {
        return;
      }
      this.props.onChange(event.nativeEvent, event);
    };

    componentWillUnmount = () => {
      BloomAd.destroyView(this.state.unique);
    };
    render() {
      return checkInit() ? (
        <View
          {...this.props}
          style={{
            alignItems: "center",
          }}
        >
          <WrappedComponent
            style={{
              width: this.state.width,
              height: this.state.height,
            }}
            size={this.state}
            onChange={this.onChange}
          />
        </View>
      ) : (
        <View>
          <Text>未初始化广告ID</Text>
        </View>
      );
    }
  };
}

export const BannerView = withComponent(BaseBanner, {
  ratio: 6.4,
  name: BANNER_VIEW,
  unitId: "b1",
});

export const NativeExpress = withComponent(BaseNativeExpress, {
  ratio: 1.2,
  name: NATIVE_EXPRESS,
  unitId: "n1",
});

export const DrawVideo = withComponent(BaseDrawVideo, {
  ratio: 1,
  name: DRAW_VIDEO,
  unitId: "df1",
});
