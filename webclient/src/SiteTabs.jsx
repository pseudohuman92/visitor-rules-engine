import React from 'react';
import { DragDropContext } from 'react-dnd';
import MultiBackend from 'react-dnd-multi-backend';
import HTML5toTouch from 'react-dnd-multi-backend/lib/HTML5toTouch';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';

import PlayArea from './PlayArea';
import DeckBuilder from './DeckBuilder';
import OpenPacks from './OpenPacks';
import { toKnowledgeCost} from "./constants/Constants";

class SiteTabs extends React.Component {
        constructor(props) {
            super(props);
            this.state = {
                value: 0,
                collection: null,
            };
        }


        componentDidMount() {
            var result = [];
            fetch('/Cards.tsv').then(r => r.text()).then(file => {
                file.split("\n").forEach(line => {
                    let fields = line.split("\t");
                    if (fields[0] !== "" && !fields[0].startsWith("Code") && !fields[0].startsWith("A")) {
                        result.push({
                            name: fields[1],
                            type: fields[2],
                            description: fields[4],
                            cost: fields[6],
                            knowledgeCost: toKnowledgeCost(fields[7])
                        });
                    }
                });
            }).then(() => {
                this.setState({ collection: result.map(c => ({ count: 3, card: c })) });
            });
        }

        handleChange = (event, value) => {
            this.setState({ value: value });
        }

        render() {
            const { value, collection } = this.state;

            return ( 
            <div style = {{ maxHeight: "100vh", maxWidth: "100vw" }} >
                <AppBar position = "static" >
                    <Tabs value = { value }
                    onChange = { this.handleChange } >
                        <Tab label = "Home" / >
                        <Tab label = "Deck Builder" / >
                        <Tab label = "Game" / >
                        <Tab label = "Open Packs" / >
                    </Tabs> 
                </AppBar > 
                { value === 0 && 
                <div> FRONTPAGE </div > } 
                { value === 1 && 
                (collection ? 
                <DeckBuilder collection = { collection }
                    style = {{ maxHeight: "100vh", maxWidth: "100vw" }}/>:
                <div>LOADING DATABASE</div > )} 
                { value === 2 && 
                <PlayArea /> }
                { value === 3 && 
                (collection ? 
                    <OpenPacks collection = { collection } value={999}/>:
                    <div>LOADING DATABASE</div >) } 
            </div>);
        }
        
    }

export default DragDropContext(MultiBackend(HTML5toTouch))(SiteTabs);