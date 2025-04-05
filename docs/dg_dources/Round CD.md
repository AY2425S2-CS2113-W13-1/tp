---
config:
  theme: default
  layout: elk
---
```mermaid

classDiagram
    class Round {
        +Round(...)
        +addPropertyChangeListener(...)
        +updateRoundVariables()
        +playCards(...)
        +discardCards(...)
        +getCurrentScore()
        +isLost()
        +isWon()
        +isRoundOver()
        +getRoundName()
        +setRoundName(...)
        +getRoundDescription()
        +setRoundDescription(...)
        +getState()
        +getConfig()
        +getObservable()
        +getBossType()
        +setBossType(...)
        +getDeck()
        +getPlayedCards()
        +getPlayedHand()
        +setPlayerHandCards(...)
    }
    class RoundState {
        +RoundState(...)
        +getCurrentScore()
        +addScore(...)
        +getRemainingDiscards()
        +setRemainingDiscards(...)
        +increaseRemainingDiscards(...)
        +decrementDiscards()
        +getRemainingPlays()
        +setRemainingPlays(...)
        +decrementPlays()
        +getPlayerJokers()
        +getDeck()
        +getPlayerHand()
        +getPlayerHandCards()
        +setPlayerHandCards(...)
        +getChosenCards()
        +setChosenCards(...)
        +drawInitialCards(...)
    }
    class RoundConfig {
        +RoundConfig(...)
        +getRoundName()
        +setRoundName(...)
        +getRoundDescription()
        +setRoundDescription(...)
        +getBlindScore()
        +getMinHandSize()
        +setMinHandSize(...)
        +getMaxHandSize()
        +setMaxHandSize(...)
        +getBossType()
        +setBossType(...)
    }
    class RoundObservable {
        +RoundObservable(...)
        +addPropertyChangeListener(...)
        +removePropertyChangeListener(...)
        +updateRoundVariables(...)
    }
    class RoundActions {
        +playCards(...)
        +discardCards(...)
    }
    class ActionResult {
        +ActionResult(...)
        +getCards()
        +getPointsEarned()
    }
    class Deck {
    }
    class HeldJokers {
    }
    class BossType {
        <<enumeration>>
        NONE
        THE_NEEDLE
        THE_WATER
        THE_PSYCHIC
    }
    class Card {
    }
    class HoldingHand {
    }
    class PokerHand {
    }
    class Joker {
    }
    Round *-- RoundState : contains
    Round *-- RoundConfig : contains
    Round *-- RoundObservable : contains
    Round ..> RoundActions : uses
    RoundActions *.. ActionResult : inner class
    RoundState o-- HeldJokers : contains
    RoundState o-- Deck : contains
    RoundState o-- HoldingHand : contains
    RoundState o-- "0..*" Card : contains via chosenCards
    RoundConfig o-- BossType : contains
    HoldingHand o-- "0..*" Card : contains via hand
    HeldJokers o-- "0..*" Joker : contains via jokers
    PokerHand o-- "0..*" Card : contains via cards
    RoundObservable o-- Round : references
