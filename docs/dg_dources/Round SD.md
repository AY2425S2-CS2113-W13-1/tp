# Initialise the Round
```mermaid
sequenceDiagram
    actor Caller
    participant A as :Round
    participant B as config:RoundConfig
    participant C as state:RoundState
    participant E as <<class>> BossType
    participant D as observable:RoundObservable

    Caller ->> A: new Round(ante, remainingPlays, deck, heldJokers, name, description)#nbsp;#nbsp;
    activate A
    Note right of A: Constructor activated
    A ->> B: new RoundConfig(...)#nbsp;#nbsp;
    activate B
    Note right of B: Constructor activated
    B -->> A: this.config : RoundConfig
    deactivate B
    A ->> C: new RoundState(...)
    activate C
    Note right of C: Constructor activated
    C -->> A: this.state : RoundState
    deactivate C
    A ->> A: applyDeckInvariants(deck)
    activate A
    alt RED
        A ->> C: increaseRemainingDiscards(: int)
        activate C
        C -->> A: void
        deactivate C
    else BLUE
        A ->> C: setRemainingPlays(: int)
        activate C
        C -->> A: void
        deactivate C
    end
    deactivate A
 
    A ->> A: applyAnteInvariants(ante)
    activate A
    alt isBossBlind
        A ->> E: getBossType()
        activate E
        E -->> A: randomBoss:BossType
        deactivate E
        A ->> B: setBossType(randomBoss)
        activate B
        B -->> A: void
        deactivate B
    end
    A -->> A: void
    deactivate A
    A ->> A: applyBossVariants()
    activate A
    alt THE_NEEDLE
        A ->> C: setRemainingPlays(1)
        activate C
        C -->> A: void
        deactivate C
    else THE_WATER
        A ->> C: setRemainingDiscards(0)
        activate C
        C -->> A: void
        deactivate C
    else THE_PSYCHIC
        A ->> B: setMaxHandSize(5)
        activate B
        B -->> A: void
        deactivate B
        A ->> B: setMinHandSize(5)
        activate B
        B -->> A: void
        deactivate B
    end
    A -->> A: void
    deactivate A
    A ->> D: new RoundObservable
    activate D
    Note right of D: Constructor activated
    D -->> A: this.observable : RoundObservable
    deactivate D
    A ->> A: validateParameters()
    activate A
    A -->> A: void
    deactivate A
    A -->> Caller: currentRound : Round
    deactivate A
```

# Playing Cards Flow
```mermaid
sequenceDiagram
    actor Caller
    participant A as :Round
    participant B as <<class>> RoundActions
    participant C as state:RoundState
    participant D as observable:RoundObservable

    Caller->>A: playCards(cardIndices)
    activate A
    A->>B: playCards(C, config, cardIndices)
    activate B
    B-->>A: result:ActionResult
    deactivate B
    A->>C: setChosenCards(result.getCards())
    activate C
    C-->>A: void
    deactivate C
    A->>C: addScore(result.getPointsEarned())
    activate C
    C-->>A: void
    deactivate C
    A->>C: decrementPlays()
    activate C
    C-->>A: void
    deactivate C
    A->>D: updateRoundVariables(C, config)
    activate D
    D-->>A: void
    deactivate D
    A-->>Caller: void
    deactivate A
```

# Discarding Cards Flow
```mermaid
sequenceDiagram
    actor Caller
    participant A as :Round
    participant B as <<class>> RoundActions
    participant C as state:RoundState
    participant D as observable:RoundObservable

    Caller->>A: discardCards(cardIndices)
    activate A
    A->>B: discardCards(C, config, cardIndices)
    activate B
    B-->>A: result:ActionResult
    deactivate B
    A->>C: setChosenCards(result.getCards())
    activate C
    C-->>A: void
    deactivate C
    A->>C: decrementDiscards()
    activate C
    C-->>A: void
    deactivate C
    A->>D: updateRoundVariables(C, config)
    activate D
    D-->>A: void
    deactivate D
    A-->>Caller: void
    deactivate A
```

# Checking if the Round is Won
```mermaid
sequenceDiagram
    actor Caller
    participant A as :Round
    participant C as state:RoundState
    participant D as observable:RoundObservable

    Caller->>A: isWon()
    activate A
    A->>C: getCurrentScore()
    activate C
    C-->>A: currentScore:int
    deactivate C
    A->>D: getBlindScore()
    activate D
    D-->>A: blindScore:int
    deactivate D
    A-->>Caller: isWon:boolean
    deactivate A
```

# Checking if the Round is Over
```mermaid
sequenceDiagram
    actor Caller
    participant A as :Round
    participant C as state:RoundState

    Caller->>A: isRoundOver()
    activate A
    A->>C: getRemainingPlays()
    activate C
    C-->>A: remainingPlays:int
    deactivate C
    A->>A: isWon()
    activate A
    A-->>A: isWon:boolean
    deactivate A
    A-->>Caller: isRoundOver:boolean
    deactivate A
```