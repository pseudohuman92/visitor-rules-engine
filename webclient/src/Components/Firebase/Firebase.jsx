import app from "firebase/app";
import "firebase/auth";
import "firebase/firestore";
import { getNewUserCollection } from "../Helpers/Constants";
import { toKnowledgeCost } from "../Helpers/Helpers";

const config = {
  apiKey: process.env.REACT_APP_API_KEY,
  authDomain: process.env.REACT_APP_AUTH_DOMAIN,
  databaseURL: process.env.REACT_APP_DATABASE_URL,
  projectId: process.env.REACT_APP_PROJECT_ID,
  storageBucket: process.env.REACT_APP_STORAGE_BUCKET,
  messagingSenderId: process.env.REACT_APP_MESSAGING_SENDER_ID
};

class Firebase {
  constructor() {
    app.initializeApp(config);
    this.auth = app.auth();
    this.db = app.firestore();
  }

  doCreateUserWithEmailAndPassword = (email, password) =>
    this.auth.createUserWithEmailAndPassword(email, password);

  doSignInWithEmailAndPassword = (email, password) =>
    this.auth.signInWithEmailAndPassword(email, password);

  doSignOut = () => this.auth.signOut();

  doPasswordReset = email => this.auth.sendPasswordResetEmail(email);

  user = uid => this.db.doc(`users/${uid}`);
  users = () => this.db.collection("users");
  deck = did => this.db.doc(`decks/${did}`);
  decks = () => this.db.collection("decks");
  collection = cid => this.db.doc(`collections/${cid}`);
  collections = () => this.db.collection("collections");

  createNewCollection = cards => {
    let coll = this.db.collection("collections").doc();
    coll.set({ cards: cards });
    return coll;
  };

  createNewUser = (uid, username) => {
    let collection = this.createNewCollection(getNewUserCollection());
    console.log("Collection: ", collection);
    console.log("ID: ", collection.id);
    this.user(uid).set(
      {
        username: username,
        coins: 1000000,
        collectionId: collection.id,
        dailyWins: 0,
        deckIds: [],
        dust: 1234567,
        packs: { Set1: 999 }
      }
    );
  };

  setUserData = (uid, updateState) => {
    this.user(uid)
      .get()
      .then(user => {
        let data = user.data();
        this.collection(data.collectionId)
          .get()
          .then(coll => {
            updateState(data);
            updateState({ collection: coll.data().cards });
          });
      });
  };

  //TODO Fix this. Return doesn't work with async
  fetchDeck = deckId => {
    let ret = {};
    this.deck(deckId)
      .get()
      .then(deck => {
        let data = deck.data();
        this.collection(data.collectionId)
          .get()
          .then(coll => {
            let cards = coll.data().cards;
            ret = { name: data.name, cards: cards };
          });
      });
    return ret;
  };

  setOpponentUsername = (uid, updateExtendedGameState) => {
    this.user(uid)
      .get()
      .then(user => {
        let data = user.data();
        updateExtendedGameState({ opponentUsername: data.username });
      });
  };

  createNewCustomCard = card => {
    let coll = this.db.collection("customCards").doc();
    coll.set(card);
  };

  getCustomCards = Return => {
    this.db
      .collection("customCards")
      .get()
      .then(function(querySnapshot) {
        let cards = [];
        querySnapshot.forEach(function(doc) {
          if (doc.id !== "structure") {
            let card = doc.data();
            cards.push({ ...card, knowledgeCost: toKnowledgeCost(card.knowledge) });
            console.log(doc.id, " => ", doc.data());
          }
        });
        console.log(cards);
        Return({cards: cards});
      });
  };
}

export default Firebase;
