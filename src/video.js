import { requireNativeComponent, View, Text } from "react-native";
import React from "react";
import { getEventName } from "./utils";

const VIDEO_STREAMING = "VideoStreaming";
const BaseVideoStreaming = requireNativeComponent(VIDEO_STREAMING);

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
        appId: props.appId,
      };
    }
    onChange = (event) => {
      // console.log(event.nativeEvent);
      if (!this.props.onChange) {
        return;
      }
      this.props.onChange(event.nativeEvent, event);
    };

    componentWillUnmount = () => {};
    render() {
      // console.log("state", this.state, this.props);
      let play = true;
      if (this.props.hasOwnProperty("play")) {
        play = this.props.play;
      }
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
            play={play}
            size={{
              play: play,
              unique: this.state.unique,
              appId: this.state.appId,
            }}
            onChange={this.onChange}
          />
        </View>
      );
    }
  };
}

export const VideoStreaming = withComponent(BaseVideoStreaming, {
  ratio: 1,
  name: VIDEO_STREAMING,
});
