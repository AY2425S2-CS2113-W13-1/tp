```mermaid
classDiagram
    class Round {
        -state: RoundState
        -config: RoundConfig
        -observable: RoundObservable
        +addPropertyChangeListener(pcl: PropertyChangeListener)
        +updateRoundVariables()
        +playCards(cardIndices: Integer)
        +discardCards(cardIndices: Integer)
        +getCurrentScore(): long
        +getRemainingDiscards(): int
        +getRemainingPlays(): int
        +getPlayerHand(): HoldingHand
        +getPlayerHandCards(): Card
        +getPlayerJokers(): HeldJokers
        +isLost(): boolean
        +isWon(): boolean
        +isRoundOver(): boolean
        -applyDeckVariants(deck: Deck)
        -applyAnteInvariants(ante: Ante)
        -applyBossVariants()
    }

    class RoundState {
        -currentScore: long
        -remainingDiscards: int
        -remainingPlays: int
        -playerJokers: HeldJokers
        -deck: Deck
        -playerHand: HoldingHand
        -chosenCards: Card
        +getPlayerJokers(): HeldJokers
        +getDeck(): Deck
        +drawInitialCards(count: int)
    }

    class RoundConfig {
        +INITIAL_HAND_SIZE: int
        +MAX_DISCARDS_PER_ROUND: int
        +DEFAULT_MAX_HAND_SIZE: int
        +DEFAULT_MIN_HAND_SIZE: int
        -blindScore: int
        -roundName: String
        -roundDescription: String
    }

    class RoundActions {
        +playCards(state: RoundState, config: RoundConfig, cardIndices: Integer): ActionResult
        +discardCards(state: RoundState, config: RoundConfig, cardIndices: Integer): ActionResult
        -validatePlayCards(cardIndices: Integer, minHandSize: int, maxHandSize: int, remainingPlays: int)
        -validateDiscardCards(cardIndices: Integer, remainingDiscards: int)
    }

    class RoundObservable {
        +addObserver(observer: Observer)
        +removeObserver(observer: Observer)
        +notifyObservers(event: String)
    }

    Round --> "1" RoundState
    Round --> "1" RoundConfig
    Round --> "1" RoundObservable
    Round ..> "1" RoundActions