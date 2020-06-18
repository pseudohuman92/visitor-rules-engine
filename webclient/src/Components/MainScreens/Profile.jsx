import React, {Component} from "react";
import ProfileBar from "../Other Components/ProfileBar";
import LinkedButton from "../Primitives/LinkedButton";


export default class Profile extends Component {
    render() {
        return (
            <div>
                <ProfileBar style={{maxHeight:"10%"}}/>
                <div style={{display: "flex"}}>
                    <LinkedButton to={"/profile/playgame"} text="Play"/>
                    <LinkedButton to={"/profile/decks"} text="Decks"/>
                    <LinkedButton to={"/profile/packs"} text="Packs"/>
                    <LinkedButton to={"/profile/collection"} text="Collection"/>
                    <LinkedButton to={"/profile/store"} text="Store"/>
                </div>
            </div>
        );
    }
}
