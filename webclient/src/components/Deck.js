import React, { Component } from "react";

import "../css/Deck.css";

class Deck extends Component {
  constructor() {
    super();
    this.state = {
      numberOfCardsInDeck: 23
    };
  }

  render() {
    let { numberOfCardsInDeck } = this.state;
    return (
      <div>
        <section id="deck" className="deck">
          <span className="number-of-cards">{numberOfCardsInDeck}</span>
        </section>
      </div>
    );
  }
}

export default Deck;
