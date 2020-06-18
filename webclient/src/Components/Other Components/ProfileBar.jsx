import React, {Component} from "react";
import {connect} from "react-redux";

const mapStateToProps = state => {
    return {
        username: state.profile.username,
        dust: state.profile.dust,
        coins: state.profile.coins,
        dailyWins: state.profile.dailyWins
    };
};

class ProfileBarElement extends Component {
    render() {
        const {imageSrc, text} = this.props;
        return <div style={{display: "flex", alignItems: "center", border: "3px black solid", borderRadius: "10px"}}>
            <img style={{
                maxWidth: "20%",
                maxHeight: "100%",
                width: "auto",
                height: "auto",
                objectFit: "scale-down",
                padding: "5px"
            }} src={imageSrc}/>
            <div>{text}</div>
        </div>
    }

}

class ProfileBar extends Component {
    render() {

        const {username, dust, coins, dailyWins} = this.props;
        return (
            <div style={{width: "100%", display: "flex", justifyContent: "space-around", alignContent: "center"}}>
                <ProfileBarElement imageSrc={process.env.PUBLIC_URL + "/img/profile-bar/username.png"} text={username}/>
                <ProfileBarElement imageSrc={process.env.PUBLIC_URL + "/img/profile-bar/coins.png"} text={coins}/>
                <ProfileBarElement imageSrc={process.env.PUBLIC_URL + "/img/profile-bar/dust.png"} text={dust}/>
                <ProfileBarElement imageSrc={process.env.PUBLIC_URL + "/img/profile-bar/daily-wins.png"} text={dailyWins}/>
            </div>);
    }
}

export default connect(
    mapStateToProps
)(ProfileBar);