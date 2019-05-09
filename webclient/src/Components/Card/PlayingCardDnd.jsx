import { ItemTypes } from "../Constants/Constants";

export const cardSource = {
  beginDrag(props) {
    return {
      sourceType: ItemTypes.CARD,
      playable: props.playableCards.includes(props.id),
      studyable: props.studyableCards.includes(props.id)
    };
  },

  canDrag(props) {
    return (
      props.playableCards.includes(props.id) ||
      props.studyableCards.includes(props.id)
    );
  },

  endDrag(props, monitor) {
    if (!monitor.didDrop()) {
      return;
    }

    const targetProps = monitor.getDropResult();
    if (
      props.playableCards.includes(props.id) &&
      (targetProps.targetType === ItemTypes.FIELD)
    ) {
      props.gameHandler.PlayCard(props.id);
    } else if (targetProps.targetType === ItemTypes.ALTAR) {
      props.gameHandler.StudyCard(props.id);
    }
  }
};

export const cardTarget = {
  drop(props, monitor) {
    if (monitor.didDrop()) {
      return;
    }

    return { targetType: ItemTypes.CARD, id: props.id };
  }
};
