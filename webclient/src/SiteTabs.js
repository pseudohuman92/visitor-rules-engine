import React from 'react';
import ReactDOM from 'react-dom';
import {DragDropContextProvider} from 'react-dnd';
import MultiBackend from 'react-dnd-multi-backend';
import HTML5toTouch from 'react-dnd-multi-backend/lib/HTML5toTouch';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';

import './index.css';
import App from './App';
import DeckBuilder from './deckbuilder/DeckBuilder.js';

function readCardDatabase() {
  var result = [];
  fetch('/Cards.tsv').then(r => r.text()).then(file =>{  
	file.split("\n").forEach(line => {
	    let fields = line.split("\t");
	    if (fields[0] !== "") {
	      result.push({
		name: fields[0],
		type: fields[1],
		description: fields[3],
		cost: fields[4],
		knowledge: fields[5]
	      });
	    }
	  });
	}).then(() => { return result.map(c => ({count: 3, card: c})); });
}

class SiteTabs extends React.Component {
  state = {
    value: 0,
    collection: null,
  };

  componentDidMount() {
    var result = [];
    fetch('/Cards.tsv').then(r => r.text()).then(file =>{  
	file.split("\n").forEach(line => {
	    let fields = line.split("\t");
	    if (fields[0] !== "") {
	      result.push({
		name: fields[0],
		type: fields[1],
		description: fields[3],
		cost: fields[4],
		knowledge: fields[5]
	      });
	    }
	  });
	}).then(() => { 
	this.setState({ collection: result.map(c => ({count: 3, card: c}))}); 
     });
  }

  handleChange = (event, value) => {
    this.setState({ value : value });
  }

  render() {
    const { value, collection, ready } = this.state;

    return (
      <div style={{maxHeight : "100vh", maxWidth: "100vw"}}>
        <AppBar position="static">
          <Tabs value={value} onChange={this.handleChange}>
            <Tab label="Deck Builder"/>
            <Tab label="Game"/>
          </Tabs>
        </AppBar>
        {value === 0 && (collection?<DeckBuilder collection={collection} style={{maxHeight : "100vh", maxWidth: "100vw"}}/>:<div>LOADING DATABASE</div>)}
        {value === 1 && <App/>}
      </div>);
  }
}

export default SiteTabs; 
