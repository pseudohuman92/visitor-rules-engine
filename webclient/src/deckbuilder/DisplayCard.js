import React from "react";
import Grid from "@material-ui/core/Grid";
import Fittext from "@kennethormandy/react-fittext";
import Rectangle from "react-rectangle";
import Image from "react-image";

export class DisplayCard extends React.Component {
    
  getCardColor(knowlString){
      if(knowlString.startsWith("B")){
          return "#666666";
      } else if (knowlString.startsWith("U")) {
          return "#0066ff";
      } else if (knowlString.startsWith("R")) {
          return "#ff1a1a";
      } else if (knowlString.startsWith("Y")) {
          return "#ffff00";
      } else {
          return "#e6e6e6";
      }
  };
  
  render() {
    const {
      small,
      opacity,
      name,
      description,
      cost,
      type,
      knowledge
    } = this.props;
    
    const marginSize = "2%";
    const border = "5px black solid";
    const borderRadius = "10px";
    const smallBorder = "3px black solid";
    
    
    if (small) {
      return (
        <div>
          <Rectangle aspectRatio={[22, 4]} 
          style={{border: smallBorder, borderRadius: borderRadius, 
              backgroundColor: this.getCardColor(knowledge), overflow: "hidden"}}>
            <Fittext>
                <div style={{margin : marginSize}}>
                  {cost !== "-" ? cost : ""}{" "}
                  {knowledge !== "-" ? "[" + knowledge + "]" : ""} {" | "}
                  {name}
                </div>
              </Fittext>
          </Rectangle>
        </div>
      );
    }

    return (
      <div>
        <Rectangle aspectRatio={[22, 32]} style={{ opacity: opacity, border: border, 
            borderRadius: borderRadius, backgroundColor: this.getCardColor(knowledge),
            overflow: "hidden" }}>
          <Grid
            container
            justify="flex-start"
            alignItems="flex-start"
            overflow="hidden"
          >
            <Grid item xs={12} style={{ margin: marginSize }}>
              <Fittext>
                <div>
                  {((cost !== "-" || knowledge !== "-")? 
                  ((cost !== "-" ? cost : "") + " " +
                  (knowledge !== "-" ? "[" + knowledge + "]" : "") + " | ") : "") + name}
                </div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{ margin: marginSize }}>
                <div>
                    <Image src={[process.env.PUBLIC_URL + "/img/" + name + ".jpg", 
                                 process.env.PUBLIC_URL + "/img/" + type + ".jpg"]} 
                           style={{maxWidth: "70%"}} decode={false}/>
                </div>
            </Grid>
            <Grid item xs={12} style={{ margin: marginSize }}>
              <Fittext>
                <div>{description}</div>
              </Fittext>
            </Grid>
            <Grid item xs={12} style={{ margin: marginSize }}>
              <Fittext>
                <div>{type}</div>
              </Fittext>
            </Grid>
          </Grid>
        </Rectangle>
      </div>
    );
  }
}

export default DisplayCard;

