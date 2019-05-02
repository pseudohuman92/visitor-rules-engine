import React, { Component } from "react";
import { DragDropContext } from "react-dnd";
import MultiBackend from "react-dnd-multi-backend";
import HTML5toTouch from "react-dnd-multi-backend/lib/HTML5toTouch";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Centered from "./Centered";

import PlayArea from "./Components/GameAreas/PlayArea";
import DeckBuilder from "./DeckBuilder";
import OpenPacks from "./OpenPacks";
import { toKnowledgeCost } from "./Constants/Constants";

class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = { value: 0, collection: null };
  }

  componentDidMount() {
    var result = [];
    fetch("/Cards.tsv")
      .then(r => r.text())
      .then(file => {
        file.split("\n").forEach(line => {
          let fields = line.split("\t");
          if (
            fields[0] !== "" &&
            !fields[0].startsWith("Code") &&
            !fields[0].startsWith("A")
          ) {
            result.push({
              name: fields[0],
              type: fields[1],
              description: fields[3],
              health: fields[4],
              cost: fields[5],
              knowledgeCost: toKnowledgeCost(fields[6])
            });
          }
        });
      })
      .then(() => {
        this.setState({ collection: result.map(c => ({ count: 3, card: c })) });
      });
  }

  Play = event => {
    this.setState({ value: 1 });
  };

  Signup = event => {
    this.setState({ value: 2 });
  };

  render() {
    const { value, collection } = this.state;
    return (
      <div>
        {value === 0 && (
          <Centered>
            <Paper>
              <Typography component="h1" variant="h1">
                Profile Page
              </Typography>
              <Button variant="contained" onClick={event => this.setState({ value: 1 })}>
                Play
              </Button>
              <Button variant="contained" onClick={event => this.setState({ value: 2 })}>
                Deck Builder
              </Button>
              <Button variant="contained" onClick={event => this.setState({ value: 3 })}>
                Open Packs
              </Button>
            </Paper>
          </Centered>
        )}
        {value === 1 && <PlayArea />}
        {value === 2 &&
          (collection ? (
            <DeckBuilder
              collection={collection}
              style={{ maxHeight: "100vh", maxWidth: "100vw" }}
            />
          ) : (
            <div>LOADING DATABASE</div>
        ))}
        {value === 3 &&
          (collection ? (
            <OpenPacks collection={collection} value={999} />
          ) : (
            <div>LOADING DATABASE</div>
          ))}
      </div>
    );
  }
}

export default DragDropContext(MultiBackend(HTML5toTouch))(Profile);
