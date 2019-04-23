import React, { Component } from "react";

import "../css/DiscardPile.css";

class DiscardPile extends Component {
  constructor() {
    super();
    this.state = {};
  }

  render() {
    return (
      <div>
        <section id="discardPile" className="discard-pile">
          <i className="discard-pile-icon" />
        </section>
      </div>
    );
  }
}

export default DiscardPile;
