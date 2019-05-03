import React, { Component } from "react";
import { DragDropContext } from "react-dnd";
import MultiBackend from "react-dnd-multi-backend";
import HTML5toTouch from "react-dnd-multi-backend/lib/HTML5toTouch";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import Center from "react-center";
import { connect } from "react-redux";

import PlayArea from "./Components/GameAreas/PlayArea";
import DeckBuilder from "./DeckBuilder";
import OpenPacks from "./OpenPacks";
import { toKnowledgeCost } from "./Constants/Constants";
import { initialize } from "./Redux/Actions";

function mapDispatchToProps(dispatch) {
  return {
    addCollection: collection => dispatch(initialize(collection))
  };
}

class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = { value: 0 };
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
        this.props.addCollection({
          collection: result.map(c => ({ count: 3, card: c })),
          fullCollection: result.map(c => ({ count: 3, card: c }))
        });
      });
  }

  render() {
    const { value } = this.state;
    return (
      <div>
        {value === 0 && (
          <Center>
            <Paper>
              <Typography component="h1" variant="h1">
                Profile Page
              </Typography>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 1 })}
              >
                Play
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 2 })}
              >
                Deck Builder
              </Button>
              <Button
                variant="contained"
                onClick={event => this.setState({ value: 3 })}
              >
                Open Packs
              </Button>
            </Paper>
          </Center>
        )}
        {value === 1 && <PlayArea />}
        {value === 2 && (
          <DeckBuilder style={{ maxHeight: "100vh", maxWidth: "100vw" }} />
        )}
        {value === 3 && <OpenPacks value={999}/>}
      </div>
    );
  }
}

export default connect(
  null,
  mapDispatchToProps
)(DragDropContext(MultiBackend(HTML5toTouch))(Profile));
