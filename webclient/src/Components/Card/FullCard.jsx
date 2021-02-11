import React from "react";
import {PureComponent} from "react";

import {replaceSpecialCharacters, toKnowledgeString} from "../Helpers/Helpers";
import "./css/Card.css";
import "../../fonts/Fonts.css";
import TextOnImage from "../Primitives/TextOnImage";
import FittedText from "../Primitives/FittedText";
import TextFit from "react-textfit";
import Fonts from "../Primitives/Fonts";
import { Text } from '@visx/text';
import Center from "react-center";
import {colorPalette} from "../Helpers/Constants";
import {knowledgeIconURLs} from "../Helpers/URLS";

function stripAbilityFromTitle(name){
    return name.replaceAll("'s Ability", "");
}

function toMultilineSVG(text, color, width){
    return (<Text style={{
            textAlign: "left",
            whiteSpace: "pre-wrap",
        }} verticalAnchor="start" fill={color} width={width}>{text}</Text>);

    return text.split("\n").map((s, i) =>
            <Text key={i} style={{
                textAlign: "left",
                whiteSpace: "pre-wrap",
            }} verticalAnchor="start" fill={color} width={width}>{s}</Text>
    );
}

class FullCard extends PureComponent {
    state = {showDialog: false};

    render() {
        const {
            brightness,
            cardData,
            borderColor,
            square,
            windowDimensions,
            scale,
            preview,
        } = this.props;

        const {
            set,
            name,
            description,
            cost,
            types,
            subtypes,
            knowledgeCost,
            delay,
            loyalty,
            combat,
        } = cardData;

        const scale_ = scale ? 10 / scale : 10;
        const {width} = windowDimensions;
        const wtohRatio = square ? 63 / 54 : 63 / 88;
        const cardWidth = width / scale_;
        const cardHeight = cardWidth / wtohRatio;

        const backColor = borderColor ? borderColor : undefined // "gainsboro";
        const defaultImageSrc = process.env.PUBLIC_URL + "/img/default.png";
        const imageSrc = process.env.PUBLIC_URL + "/img/sets/" + set + "/" + stripAbilityFromTitle(name) + ".jpg";
        //const imageSrc = process.env.PUBLIC_URL + "/img/card-frames/card-frame-3.png";
        return (
            <div
                className = "full-card"
                style={{
                    position: "relative",
                    width: cardWidth,
                    height: cardHeight,
                    filter: "brightness(" + brightness + "%)",
                    backgroundColor: backColor,
                    borderRadius: cardWidth / (2 * scale_) + "px",
                }}
            >
                <div
                    className="card-inner fill"
                    style={{
                        top: square ? "3%" : "2%",
                        height: square ? "94%" : "96%",
                        position: "relative",
                        fontSize: cardWidth / (2 * scale_) + "px",
                        borderRadius: cardWidth / ((square ? 3 : 2.5) * scale_) + "px",
                        backgroundColor: "darkgray",
                        backgroundImage: "url(" + imageSrc +")",
                        border: "1px " + colorPalette.red + " solid",
                    }}
                >
                    {!preview && combat && combat.deploying &&
                    <img
                        src={
                            process.env.PUBLIC_URL +
                            "/img/deploying.png"
                        }
                        style={
                            square
                                ? {
                                    maxWidth: "50%",
                                    position: "absolute",
                                    top: "25%",
                                    left: "25%",
                                    zIndex: 10
                                }
                                : {
                                    maxHeight: "50%",
                                    position: "absolute",
                                    top: "25%",
                                    left: "25%",
                                    zIndex: 10
                                }
                        }
                        alt=""
                    />
                    }
                    {cost !== "" && (
                        <div className="card-cost fill"
                             style={{width: "90%",
                                 textAlign: "center",
                                 fontSize: "initial",
                                 backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/card-components/energy-black.png)"}}>
                                {cost}
                        </div>
                    )}
                    {/*<div className="knowledge-bar fill"
                         style={{
                             position: "absolute",
                             top: "10%",
                             left: "0%",
                             width: "15%",
                             height: "" + ((toKnowledgeString(knowledgeCost).length > 0 ? 10 : 0)+(toKnowledgeString(knowledgeCost).length *  3)) + "%",
                             zIndex: "0",
                        backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/rectangle-vertical-black.png)"}}/>*/}
                    {toKnowledgeString(knowledgeCost)
                        .split("")
                        .map((c, i) => (
                            <div
                                className="card-knowledge"
                                style={{top: (square ? 17 : 13) + i * (square ? 4 : 3.5) + "%",
                                left: "2.5%"}}
                                key={i}
                            >
                                <img
                                    src={knowledgeIconURLs[c]}
                                    alt=""
                                />
                            </div>
                        ))}
                    <div className="card-name fill"
                         style={{
                             width: "95%",
                             height: square ? "9%" : "6%",
                             backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/buttons/grunge-highlight-red.png)",
                        }}>
                            {name}
                    </div>

                    {!square && <div className="card-type fill" style={{
                        backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/buttons/grunge-highlight-red.png)",
                    }}>{types}{subtypes && subtypes.length > 0 && subtypes[0] !== "" ? " - " + subtypes.join(' ') : ""} </div>}

                    {!square && (
                        <div
                            className="card-description fill hidden-scrollable"
                            style={{
                                textAlign: "left",
                                whiteSpace: "pre-wrap",
                                fontSize: "initial",
                                backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/grunge-background-black.png)",
                                fontFamily: "Roboto, serif"
                            }}
                        >
                            <TextFit style={{
                                position: "relative",
                                top: "2%",
                                left: "3%",
                                maxWidth: "94%",
                                maxHeight: "96%",
                                fontFamily: "Roboto, serif"
                            }}>{(combat && combat.combatAbilities && combat.combatAbilities.length > 0 ? combat.combatAbilities + "\n" : "")
                            + replaceSpecialCharacters(description, name)}</TextFit>
                        </div>
                    )}

                    <div
                        style={{
                            position: "absolute",
                            bottom: "0",
                            left: "3%",
                            width : cardWidth * 0.93 * 0.94,
                            height: cardHeight * 0.1,
                            zIndex: 2
                        }}
                    >
                        <div style={{
                            position: "relative", display: "flex",
                            width : cardWidth * 0.93 * 0.94 * 0.5,
                            height: cardHeight * 0.1,
                            justifyContent:"flex-begin"
                        }}>
                            {combat && combat.attack !== "" && combat.attack > -1 && (
                                <TextOnImage
                                    src={
                                        process.env.PUBLIC_URL + "/img/card-components/attack-red.png"
                                    }
                                    text={combat.attack}
                                    min={15}
                                    scale={5 * scale_}
                                    font={{fontFamily: "Frijole, cursive", color: colorPalette.white}}
                                    windowDimensions={windowDimensions}
                                />
                            )}
                            {combat && combat.health !== "" && combat.health > -1 && (
                                <TextOnImage
                                    src={process.env.PUBLIC_URL + "/img/card-components/health-red.png"}
                                    text={combat.health}
                                    min={15}
                                    scale={5 * scale_}
                                    font={{fontFamily: "Frijole, cursive"}}
                                    textStyle={{color:"white"}}
                                    windowDimensions={windowDimensions}
                                />
                            )}
                            {combat && combat.shield > 0 && (
                                <TextOnImage
                                    style={{flexGrow: 1}}
                                    src={process.env.PUBLIC_URL + "/img/card-components/shield.png"}
                                    text={combat.shield}
                                    min={15}
                                    scale={5 * scale_}
                                    font={{fontFamily: "Special Elite, cursive"}}
                                    windowDimensions={windowDimensions}
                                />
                            )}
                            {loyalty > -1 && (
                                <TextOnImage
                                    style={{flexGrow: 1}}
                                    src={process.env.PUBLIC_URL + "/img/card-components/loyalty.png"}
                                    text={loyalty}
                                    min={15}
                                    scale={5 * scale_}
                                    font={{fontFamily: "Special Elite, cursive"}}
                                    windowDimensions={windowDimensions}
                                />
                            )}
                            {delay > 0 && (
                                <TextOnImage
                                    style={{flexGrow: 1}}
                                    src={process.env.PUBLIC_URL + "/img/card-components/delay.png"}
                                    text={delay}
                                    min={15}
                                    scale={5 * scale_}
                                    font={{fontFamily: "Special Elite, cursive"}}
                                    windowDimensions={windowDimensions}
                                />
                            )}
                        </div>
                    </div>

                    { !square && <div className="illustrator fill" style={{textAlign : "right", fontSize: "10px", backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/buttons/grunge-highlight-black.png)",}}> Illustrator Name </div> }
                </div>
            </div>
        );
    }
}

export default FullCard;
