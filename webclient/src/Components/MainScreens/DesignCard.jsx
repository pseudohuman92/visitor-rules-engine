import React from "react";
import { Component } from "react";
import Rectangle from "react-rectangle";
import Button from "@material-ui/core/Button";
import Input from "@material-ui/core/Input";
import Center from "react-center";

import {
  getCardColor,
  toKnowledgeCost,
} from "../Helpers/Helpers";
import Fonts from "../Fonts/Fonts";
import "../../css/Card.css";
import "../../css/Utils.css";
import { TextField, Grid } from "@material-ui/core";
import { withFirebase } from "../Firebase";

class DesignCard extends Component {
  constructor(props) {
    super(props);

    this.state = {
      creator: "",
      cost: "",
      knowledge: "",
      name: "",
      type: "",
      subtype: "",
      description: "",
      health: ""
    };
  }

  onChange = name => event => {
    this.setState({ [name]: event.target.value });
  };

  submit = event => {
    this.props.firebase.createNewCustomCard(this.state);
    this.setState({
        creator: "",
        cost: "",
        knowledge: "",
        name: "",
        type: "",
        subtype: "",
        description: "",
        health: ""
      });
  };

  render() {
    const {
      knowledge,
      type,
    } = this.state;
    return (
      <Center>
        <div>
          <Fonts />
          <Grid container spacing={24} justify="space-around">
            <Grid item xs={10}>
              <TextField
                id="creator"
                label="Creator"
                margin="dense"
                value={this.state.creator}
                fullWidth
                onChange={this.onChange("creator")}
              />
            </Grid>
            <Grid item xs={2}>
              <Button type="submit" variant="contained" onClick={this.submit}>
                Submit
              </Button>
            </Grid>
          </Grid>
          <Rectangle
            aspectRatio={[63, 88]}
            style={{
              backgroundColor: "black",
              overflow: "hidden"
            }}
          >
            <div
              className="card-inner"
              style={{
                backgroundColor: getCardColor(toKnowledgeCost(knowledge))
              }}
            >
              <div className="card-name">
                <Grid container spacing={24} justify="space-around">
                  <Grid item xs={2}>
                    <Input
                      id="cost"
                      label="Cost"
                      placeholder="0"
                      margin="dense"
                      value={this.state.cost}
                      onChange={this.onChange("cost")}
                      InputLabelProps={{
                        shrink: true
                      }}
                    />
                  </Grid>
                  <Grid item xs={4}>
                    <Input
                      id="knowledge"
                      label="Knowledge"
                      placeholder="BGRU"
                      margin="dense"
                      value={this.state.knowledge}
                      onChange={this.onChange("knowledge")}
                      InputLabelProps={{
                        shrink: true
                      }}
                    />
                  </Grid>
                  <Grid item xs={6}>
                    <Input
                      id="name"
                      label="Name"
                      placeholder="Card Name"
                      margin="dense"
                      value={this.state.name}
                      fullWidth
                      onChange={this.onChange("name")}
                      InputLabelProps={{
                        shrink: true
                      }}
                    />
                  </Grid>
                </Grid>
              </div>

              <div className="card-image">
                <img
                  src={process.env.PUBLIC_URL + "/img/" + type + ".png"}
                  style={{ maxWidth: "100%" }}
                  alt=""
                />
                {/*
              <Image
                src={[
                  process.env.PUBLIC_URL + "/img/" + name + ".jpg",
                  process.env.PUBLIC_URL + "/img/" + type + ".png"
                ]}
                style={{ maxWidth: "100%" }}
                decode={false}
              />
              */}
              </div>

              <div className="card-type">
                <Input
                  id="type"
                  label="Type"
                  placeholder="Card Type"
                  onChange={this.onChange("type")}
                  margin="dense"
                  value={this.state.type}
                  fullWidth
                  InputLabelProps={{
                    shrink: true
                  }}
                />
              </div>

              <div className="card-description">
                <Grid
                  container
                  spacing={24}
                  justify="flex-end"
                  style={{ maxHeight: "100%" }}
                >
                  <Grid item xs={12}>
                    <Input
                      id="description"
                      label="Ability Text"
                      placeholder="Ability Text"
                      margin="dense"
                      value={this.state.description}
                      fullWidth
                      multiline
                      onChange={this.onChange("description")}
                      InputLabelProps={{
                        shrink: true
                      }}
                    />
                  </Grid>
                  {this.state.type === "Item" ? (
                    <Grid item xs={4}>
                      <Input
                        id="health"
                        label="Health"
                        placeholder="Health"
                        margin="dense"
                        value={this.state.health}
                        onChange={this.onChange("health")}
                        InputLabelProps={{
                          shrink: true
                        }}
                      />
                    </Grid>
                  ) : (
                    <div />
                  )}
                </Grid>
              </div>
            </div>
          </Rectangle>
        </div>
      </Center>
    );
  }
}

export default withFirebase(DesignCard);
