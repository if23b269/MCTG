# Structure

├── main\
│  ├── java\
│  │  └── at\
│  │      └── technikum\
│  │          ├── api\
│  │          │  ├── controller\
│  │          │  │  ├── Controller.java\
│  │          │  │  ├── SessionController.java\
│  │          │  │  └── UserController.java\
│  │          │  └── info.txt\
│  │          ├── DAL\
│  │          │  ├── CardData.java\
│  │          │  ├── DAO\
│  │          │  │  ├── Card.java\
│  │          │  │  └── User.java\
│  │          │  ├── SessionData.java\
│  │          │  └── UserData.java\
│  │          ├── httpserver\
│  │          │  ├── http\
│  │          │  │  ├── ContentType.java\
│  │          │  │  ├── HttpStatus.java\
│  │          │  │  └── Method.java\
│  │          │  ├── server\
│  │          │  │  ├── HeaderMap.java\
│  │          │  │  ├── Request.java\
│  │          │  │  ├── Response.java\
│  │          │  │  ├── Server.java\
│  │          │  │  ├── Service.java\
│  │          │  │  └── Session.java\
│  │          │  └── utils\
│  │          │      ├── RequestBuilder.java\
│  │          │      ├── RequestHandler.java\
│  │          │      └── Router.java\
│  │          ├── Main.java\
│  │          └── service\
│  │              ├── SessionService.java\
│  │              └── UserService.java\
│  └── resources\
└── test\
    └── java

# Class diagram:
```mermaid
  classDiagram
    class User {
        +String username
        +String password
        +int coins
        +List<Card> cards
        +Deck deck
        +List<Card> stack
        +int elo
        +register()
        +login()
        +manageCards()
        +acquirePackage()
        +defineDeck()
        +battle(opponent: User)
        +compareStats()
        +tradeCard(card: Card, requirements)
        +addCardToStack(card: Card)
        +removeCardFromStack(card: Card)
    }

    class Card {
        +int ID
        +String name
        +int damage
        +ElementType elementType
        +boolean isSpellCard
        +boolean isMonsterCard
        +getDamage()
        +isEffectiveAgainst(opponentCard: Card)
    }

    class Session {
        +int id;
        +User user;
        +String token;
    }

    class Package {
        +List<Card> cards
        +int price = 5
        +getCards()
    }

    class Deck {
        +List<Card> selectedCards
        +selectCard(card: Card)
        +removeCard(card: Card)
    }

    class Battle {
        +User player1
        +User player2
        +int roundLimit = 100
        +battle()
        +roundOutcome(card1: Card, card2: Card)
    }

    class Scoreboard {
        +List<User> users
        +updateStats(user: User)
        +display()
    }

    class Trading {
        +Card cardForTrade
        +Requirements requirements
        +requestTrade()
    }

    class Requirements {
        +boolean isSpell
        +boolean isMonster
        +int minDamage
    }

    class ElementType {
        +String type
        +isEffectiveAgainst(opponent: ElementType)
    }
User "1" o-- "*" Deck : uses
User "1" o-- "*" Package : can acquire
User "1" o-- "1" Scoreboard : has
%%Stack "1" o-- "*" Card : contains
Deck "1" o-- "*" Card : consists of
User "1" ..> "1" Battle : engages in
User "1" .. "1" Session
Battle "1" o-- "2" User : participants
Trading "1" o-- "1" Card : offers
Trading "1" o-- "1" Requirements : based on
Card "1" o-- "1" ElementType : assigned
```