import React, { Component } from "react";
import { knowledgeMap } from "./Constants.js";

import "./css/ResourceArea.css";

class ResourceArea extends Component {
  

  render() {
    const {
      energy,
      maxEnergy,
      knowledgePool
    } = this.props.player;
    return (
      <div>
        <section id="resourceArea" className="resource-area">
          <div className="mana-area">
            <span>
              {energy} / {maxEnergy}
            </span>
          </div>
          <div className="knowledge-area">
            {knowledgePool.map(k => (
              <div className="knowledge">
                <span className={"knowledge-stone " + knowledgeMap[k.knowledge] + "-knowledge"} />
                {k.count}
              </div>))
            }
          </div>
        </section>
      </div>
    );
  }
}

export default ResourceArea;
