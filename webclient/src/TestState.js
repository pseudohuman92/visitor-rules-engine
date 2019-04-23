const cards = "abcdefghijklmnopqrstuvwxyz".split("").map(l => ({
  id: l,
  name: l,
  depleted: false,
  marked: false,
  targets: [],
  cost: 4,
  knowledgeCost: [
    {
      knowledge: 2,
      count: 2
    }
  ],
  counters: [{ counter: 1, count: 5 }],
  type: "Action",
  description: "The best card ever. and ever and ever and ever and ever"
}));
const p1HandCards = cards.slice(0, 10);
const p2HandSize = 4;
const p1ScrapCards = cards.slice(7, 9);
const p2ScrapCards = cards.slice(9, 10);
const p1VoidCards = cards.slice(10, 12);
const p2VoidCards = [];
const p1PlayCards = cards.slice(12, 16);
const p2PlayCards = cards.slice(16, 19);
const stackCards = cards.slice(10, 23);

const p1 = {
  id: "p1",
  name: "Player 1",
  deckSize: 45,
  energy: 3,
  maxEnergy: 7,
  play: p1PlayCards,
  hand: p1HandCards,
  scrapyard: p1ScrapCards,
  void: p1VoidCards,
  knowledgePool: [
    {
      knowledge: 1,
      count: 3
    },
    {
      knowledge: 3,
      count: 2
    }
  ]
};

const p2 = {
  id: "p2",
  name: "Player 2",
  deckSize: 39,
  energy: 2,
  maxEnergy: 12,
  play: p2PlayCards,
  handSize: p2HandSize,
  scrapyard: p2ScrapCards,
  void: p2VoidCards,
  knowledgePool: [
    {
      knowledge: 1,
      count: 6
    },
    {
      knowledge: 3,
      count: 2
    },
    {
      knowledge: 4,
      count: 1
    }
  ]
};

const gameState = {
  id: "Empty Game",
  player: p1,
  opponent: p2,
  turnPlayer: p1.name,
  activePlayer: p2.name,
  stack: stackCards,
  canActivate: [p1PlayCards[0].id, p1PlayCards[2].id],
  canPlay: [p1HandCards[0].id, p1HandCards[2].id],
  canStudy: [p1HandCards[0].id, p1HandCards[1].id],
  phase: 0
};
