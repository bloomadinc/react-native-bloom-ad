import { 
  requireNativeComponent, 
  NativeModules, 
  UIManager, 
  findNodeHandle,
  View 
} from "react-native";
import React from "react";

const { BloomAd } = NativeModules;
const VIDEO_STREAMING = "VideoStreaming";
const BaseVideoStreaming = requireNativeComponent(VIDEO_STREAMING);

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
    }
    onChange = (event) => {
      // console.log(event.nativeEvent);
      if (!this.props.onChange) {
        return;
      }
      this.props.onChange(event.nativeEvent, event);
    };

    componentWillUnmount = () => {
      BloomAd.destroyView(this.androidViewId.toString());
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
      let play = true
      if(typeof this.props.play === 'boolean'){
        play = this.props.play
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
            play={{
              reactNativeId: this.androidViewId || 0,
              play,
            }}
            onChange={this.onChange}
            ref={(nativeRef) => this.nativeComponentRef = nativeRef}
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
