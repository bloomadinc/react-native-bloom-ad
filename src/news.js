import { requireNativeComponent, View, Text } from "react-native";
import React from "react";
import { getEventName } from "./utils";

const NEWS_PORTAL = "NewsPortal";
const BaseNewsPortal = requireNativeComponent(NEWS_PORTAL);

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

      this.state = {
        width,
        height,
        unique,
        appId: props.appId || "",
      };
    }

    componentWillUnmount = () => {};
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
            size={{
              unique: this.state.unique,
              appId: this.state.appId,
            }}
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
