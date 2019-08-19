import React from "react";
import { connect } from "react-redux";

import PlayingCard from '../Card/PlayingCard';
import '../../css/Stack.css';
import '../../css/Utils.css';
import VerticalStack from '../Primitives/VerticalStack';

const mapStateToProps = state => {
  return { stack: state.extendedGameState.game.stack };
};

class Stack extends React.Component {
  render() {
    const { stack } = this.props;
    return (
      <VerticalStack stepSize={20}>
          {stack.map((child, i) => {
            return (
              <PlayingCard
                key={i}
                {...child}
              />
            );
          })}
      </VerticalStack>
    );
  }
}

export default connect(mapStateToProps)(Stack);