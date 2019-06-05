import app from "firebase/app";
import firebase from "firebase/app";
import "firebase/auth";
import "firebase/firestore";
import { getNewUserCollection } from "../Helpers/Constants";
import { toKnowledgeCost, debug } from "../Helpers/Helpers";

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
  customCard = cid => this.db.doc(`customCards/${cid}`);
  customCards = () => this.db.collection("customCards");
  bugReport = bid => this.db.doc(`bugReports/${bid}`);
  bugReports = () => this.db.collection("bugReports");
  feedback = fid => this.db.doc(`feedbacks/${fid}`);
  feedbacks = () => this.db.collection("feedbacks");

  createNewCollection = cards => {
    let coll = this.collections().doc();
    coll.set({ cards: cards });
    return coll;
  };

  openPack = (userId, packName, cards) => {
    let userRef = this.user(userId);
    this.db
      .runTransaction(transaction => {
        return transaction.get(userRef).then(userDoc => {
          let user = userDoc.data();
          let collectionRef = this.collection(user.collectionId);
          return transaction.get(collectionRef).then(collectionDoc => {
            let packs = user.packs;
            if (!packs[packName] || packs[packName] === 0) {
              return;
            }
            packs[packName] -= 1;
            let collection = collectionDoc.data().cards;
            Object.keys(cards).forEach(cardName => {
              if (collection[cardName]) {
                collection[cardName] += cards[cardName];
              } else {
                collection[cardName] = cards[cardName];
              }
            });
            transaction.update(collectionRef, { cards: collection });
            transaction.update(userRef, { packs: packs });
            return;
          });
        });
      });
  };

  getPacks = (userId, Return) => {
    this.user(userId)
      .get()
      .then(userDoc => {
        Return(userDoc.data().packs);
      });
  };

  createNewUser = (uid, username) => {
    let collection = this.createNewCollection(getNewUserCollection());
    this.user(uid).set({
      username: username,
      coins: 1000000,
      collectionId: collection.id,
      dailyWins: 0,
      deckIds: [],
      dust: 1234567,
      packs: { Set1: 999 }
    });
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

  createNewDeck = (uid, name, Return) => {
    let collection = this.createNewCollection({});
    let deck = this.decks().doc();
    deck.set({ name: name, collectionId: collection.id });
    this.user(uid).update({
      deckIds: firebase.firestore.FieldValue.arrayUnion(deck.id)
    });
    Return(deck.id);
  };

  updateDeck = (deckId, name, cards) => {
    if (name) {
      this.deck(deckId).update({ name: name });
    }
    if (cards) {
      this.deck(deckId)
        .get()
        .then(deck => {
          this.collection(deck.data().collectionId).update({ cards: cards });
        });
    }
  };

  getDeck = (deckId, Return) => {
    this.deck(deckId)
      .get()
      .then(deckDoc => {
        let deck = deckDoc.data();
        this.collection(deck.collectionId)
          .get()
          .then(coll => {
            let cards = coll.data().cards;
            Return(deck.name, cards);
          });
      });
  };

  getAllDecks = (uid, Return) => {
    this.user(uid)
      .get()
      .then(userDoc => {
        let user = userDoc.data();
        user.deckIds.forEach(deckId => {
          this.deck(deckId)
            .get()
            .then(deckDoc => {
              let deck = deckDoc.data();
              if (deck) {
                Return({ id: deckId, name: deck.name });
              }
            })
            .catch(debug("Error in getAllDecks. ID: ", deckId));
        });
      });
  };

  deleteDeck = (userId, deckId) => {
    this.deck(deckId)
      .get()
      .then(deck => {
        this.collection(deck.data().collectionId).delete();
        this.deck(deckId).delete();
        this.user(userId).update({
          deckIds: firebase.firestore.FieldValue.arrayRemove(deckId)
        });
      });
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
    this.customCards().add(card);
  };

  getCustomCards = Return => {
    this.customCards()
      .get()
      .then(function(querySnapshot) {
        let cards = [];
        querySnapshot.forEach(function(doc) {
          if (doc.id !== "structure") {
            let card = doc.data();
            cards.push({
              ...card,
              knowledgeCost: toKnowledgeCost(card.knowledge)
            });
          }
        });
        Return({ cards: cards });
      });
  };

  submitBugReport = report => {
    this.bugReports().add(report);
  };

  submitFeedback = feedback => {
    this.feedbacks().add(feedback);
  };
}

export default Firebase;
