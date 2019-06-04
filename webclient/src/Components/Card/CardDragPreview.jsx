import React from "react";
import { PureComponent } from "react";
import { DragLayer } from "react-dnd";
import { ItemTypes } from "../Helpers/Constants";

import FullCard from "./FullCard";
import SmallCard from "./SmallCard";

class CardDragPreview extends PureComponent {
  getItemStyles = props => {
    const { initialOffset, currentOffset } = props;
    if (!initialOffset || !currentOffset) {
      return {
        display: "none"
      };
    }
    let { x, y } = currentOffset;
    const transform = `translate(${x}px, ${y}px)`;
    return {
      transform,
      WebkitTransform: transform
    };
  };

  renderItem = (itemType, props) => {
    const { small, size, ...rest } = props;
    switch (itemType) {
      case ItemTypes.CARD:
        return (
          <div style={{ opacity: "0.5" }}>
            {small ? <SmallCard {...rest} /> : <FullCard {...rest} />}
          </div>
        );
      default:
        return null;
    }
  };

  render() {
    const { item, itemType, isDragging } = this.props;
    if (!isDragging || !item.props) {
      return null;
    }

    return (
      <div
        style={{
          position: "fixed",
          pointerEvents: "none",
          zIndex: 100,
          left: 0,
          top: 0,
          width: item.props.size.width,
          height: item.props.size.height
        }}
      >
        <div style={this.getItemStyles(this.props)}>
          {this.renderItem(itemType, item.props)}
        </div>
      </div>
    );
  }
}

export default DragLayer(monitor => ({
  item: monitor.getItem(),
  itemType: monitor.getItemType(),
  initialOffset: monitor.getInitialSourceClientOffset(),
  currentOffset: monitor.getSourceClientOffset(),
  isDragging: monitor.isDragging()
}))(CardDragPreview);
