import React from "react";
import Grid from "@material-ui/core/Grid";
import Fittext from "@kennethormandy/react-fittext";
import Textfit from "react-textfit";
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
    const border = "1px black solid";
    const borderRadius = "3px";
    const smallBorder = "3px black solid";

    const cardWidth = 63;
    const cardHeight = 88;
    const outerMargin = 2;
    const innerWidth = cardWidth - 2 * outerMargin;
    const innerHeight = cardHeight - 2 * outerMargin;
    
    
    if (small) {
      return (
        <div>
          <Rectangle aspectRatio={[22, 4]} 
          style={{border: smallBorder, borderRadius: borderRadius, 
              backgroundColor: this.getCardColor(knowledge), overflow: "hidden"}}>
            <Textfit
              		mode="single"
              		forceSingleModeWidth={false}
              		style={{maxHeight: '100%',
              		margin : marginSize}}>
                  {cost !== "-" ? cost : ""}{" "}
                  {knowledge !== "-" ? "[" + knowledge + "]" : ""} {" | "}
                  {name}
                </Textfit>
          </Rectangle>
        </div>
      );
    }

    return (
      <div>
      	<Rectangle aspectRatio={[63, 88]} style={{ opacity: opacity,
            borderRadius: borderRadius, backgroundColor: "black",
            overflow: "hidden" }}>
	        <div style={{ opacity: opacity,
	        	margin: "3.1746% 3.9683% 3.1746% 3.9683%", 
	        	backgroundColor: this.getCardColor(knowledge),
	            overflow: "hidden",
	            height: "93.6508%" }}>

		        <div style={{ 
		        	margin: "3.4483% 3.4483% 0 3.4483%",
		        	border: border,
		        	borderRadius: "3px",  
		        	backgroundColor: "beige",
		            overflow: "hidden",
		            height: "5.9524%" }}>
		            <Textfit
              		mode="single"
              		forceSingleModeWidth={false}
              		style={{ maxWidth: '96%', maxHeight: '100%'}}>
				            {" " + ((cost !== "-" || knowledge !== "-")? 
			                  ((cost !== "-" ? cost : "") + " " +
			                  (knowledge !== "-" ? "[" + knowledge + "]" : "") + " | ") : "") + 
				            name}
		            </Textfit>
	            </div>

	            <div style={{ 
		        	margin: "0.8621% 3.4483% 0 3.4483%",
		            overflow: "hidden",
		            height: "47.6190%" }}>
		            <Image src={[process.env.PUBLIC_URL + "/img/" + name + ".jpg", 
	                                 process.env.PUBLIC_URL + "/img/" + type + ".jpg"]} 
	                           style={{maxWidth: "100%"}} decode={false}/>
	            </div>

	            <div style={{ 
		        	margin: "0.8621% 3.4483% 0 3.4483%",
		        	border: border,
		        	borderRadius: "3px",  
		        	backgroundColor: "beige",
		            overflow: "hidden",
		            height: "5.9524%" }}>
		            <Textfit
              		mode="single"
              		forceSingleModeWidth={false}
              		style={{maxHeight: '100%'}}>
			            {" " + type}
		            </Textfit>
	            </div>

	            <div style={{ 
		        	margin: "0.8621% 3.4483% 3.4483% 3.4483%",
		        	border: border,
		        	borderRadius: "3px",  
		        	backgroundColor: "beige",
		            overflow: "hidden",
		            height: "33.9286%" }}>
	            	<Textfit style={{maxHeight: '100%'}}>
			            {description}
		            </Textfit>

	            </div>
	        </div>
        </Rectangle>
      </div>
    );
  }
}

export default DisplayCard;

