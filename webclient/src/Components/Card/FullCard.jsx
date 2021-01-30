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
            opacity,
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
        const imageSrc = process.env.PUBLIC_URL + "/img/sets/" + set + "/" + stripAbilityFromTitle(name) + ".jpg";
        return (
            <div
                style={{
                    width: cardWidth,
                    height: cardHeight,
                    position: "relative",
                    filter: "brightness("+opacity+"%)",
                    backgroundColor: backColor,
                    borderRadius: cardWidth / (2 * scale_) + "px",
                }}
            >
                {<Fonts />}
                <div
                    className="card-inner"
                    style={{
                        top: square ? "3%" : "2%",
                        height: square ? "94%" : "96%",
                        position: "relative",
                        fontSize: cardWidth / (2 * scale_) + "px",
                        borderRadius: cardWidth / ((square ? 3 : 2.5) * scale_) + "px",
                        backgroundColor: "gainsboro",

                        backgroundImage: "url(" + imageSrc +")",
                        backgroundPosition: 'center',
                        backgroundSize: 'cover',
                        backgroundRepeat: 'no-repeat',

                        border: "1px red solid",
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
                                    objectFit: "scale-down",
                                    position: "absolute",
                                    top: "25%",
                                    left: "25%",
                                    zIndex: 10
                                }
                                : {
                                    maxHeight: "50%",
                                    objectFit: "scale-down",
                                    position: "absolute",
                                    top: "25%",
                                    left: "25%",
                                    zIndex: 10
                                }
                        }
                        alt=""
                    />
                    }
                    {/*
                    <div
                        style={{
                            position: "absolute",
                            top: 0,
                            left: 0,
                            width: "100%",
                            height: "100%",
                            zIndex: -1,
                            backgroundColor: "gainsboro",
                            display: "block",
                            marginLeft: "auto",
                            marginRight: "auto",
                        }}
                    >
                        <img
                            src={
                                process.env.PUBLIC_URL +
                                "/img/sets/" + set + "/"
                                + stripAbilityFromTitle(name) + ".jpg"
                            }
                            style={{
                                position: "absolute",
                                top: "50%",
                                left: "50%",
                                transform: "translate(-50%, -50%)",
                                ...(square
                                    ? {
                                        maxWidth: "100%",
                                        objectFit: "scale-down",
                                    }
                                    : {
                                        maxHeight: "100%",
                                        objectFit: "scale-down",
                                    })
                            }}
                            alt=""
                        />
                    </div> */}
                    {cost !== "" && (
                        <div className="card-cost">
                            <img
                                src={process.env.PUBLIC_URL + "/img/card-components/energy-black.png"}
                                style={{
                                    maxWidth: "100%",
                                }}
                                alt=""
                            />
                            <Center
                                className="card-cost-text"
                                style={{fontSize: cardWidth / 15 + "px"}}
                            >
                                {cost}
                            </Center>
                        </div>
                    )}

                    {toKnowledgeString(knowledgeCost)
                        .split("")
                        .map((c, i) => (
                            <div
                                className="card-knowledge"
                                style={{top: (square ? 17 : 11) + i * (square ? 4 : 3.5) + "%"}}
                                key={i}
                            >
                                <img
                                    src={
                                        process.env.PUBLIC_URL +
                                        "/img/card-components/knowledge-" +
                                        c +
                                        ".png"
                                    }
                                    style={{
                                        maxWidth: "100%",
                                    }}
                                    alt=""
                                />
                            </div>
                        ))}
                    <div className="card-name fill"
                         style={{
                             height: square ? "9%" : "6%",
                             backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/buttons/grunge-highlight-red.png)",
                        }}>
                        {<div style={{
                            position: "static",
                            margin: 0,
                        }}>
                            <TextFit forceSingleModeWidth={true}>
                            {name}
                            </TextFit>
                        </div>}
                    </div>

                    {!square && <div className="card-type fill" style={{
                        backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/buttons/grunge-highlight-red.png)",
                    }}>{types}{subtypes && subtypes.length > 0 && subtypes[0] !== "" ? " - " + subtypes.join(' ') : ""} </div>}

                    {!square && (
                        <div
                            className="card-description fill"
                            style={{

                                textAlign: "left",
                                whiteSpace: "pre-wrap",
                                backgroundImage: "url(" + process.env.PUBLIC_URL + "/img/grunge-background-black.png)",

                            }}
                        >
                            <TextFit style={{
                                position: "relative",
                                top: "2%",
                                left: "3%",
                                maxWidth: "94%",
                                maxHeight: "96%",
                            }}>{(combat && combat.combatAbilities && combat.combatAbilities.length > 0 ? combat.combatAbilities + "\n" : "")
                            + replaceSpecialCharacters(description, name)}</TextFit>

                            {/*toMultilineSVG((combat && combat.combatAbilities && combat.combatAbilities.length > 0 ? combat.combatAbilities.join(" ") + "\n" : "")
                            + replaceSpecialCharacters(description, name), "white", 200 * scale)*/}
                            {/*<FittedText
                                text={(combat ? (combat.combatAbilities && combat.combatAbilities.length > 0 ? combat.combatAbilities + "\n" : "") : "")
                                + replaceSpecialCharacters(description, name)}
                                max={12}
                                padding={0}
                                font = {{fontFamily: "Archivo"}}
                                windowDimensions={windowDimensions}
                            />*/}
                            {/*(combat ? (combat.combatAbilities ? combat.combatAbilities + "\n" : "") : "") + replaceSpecialCharacters(description, name)*/}
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
                                        process.env.PUBLIC_URL + "/img/card-components/attack2.png"
                                    }
                                    text={combat.attack}
                                    min={15}
                                    scale={5 * scale_}
                                    font={{fontFamily: "Special Elite, cursive"}}
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
