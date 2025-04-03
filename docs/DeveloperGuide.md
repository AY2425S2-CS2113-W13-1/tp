# Developer Guide

## Table of Contents

1. [Acknowledgements](#acknowledgements)
2. [Setting up, getting started](#setting-up-getting-started)
3. [Design](#design)
   - [Architecture](#architecture)
   - [UI Component](#ui-component)
   - [Logic Component](#logic-component)
   - [Model Component](#model-component)
   - [Storage Component](#storage-component)
   - [Round Component](#round-component)
   - [Score Component](#score-component)
4. [Implementation](#implementation)
5. [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
6. [Documentation](#documentation)
7. [Appendix: Requirements](#appendix-requirements)
   - [Product Scope](#product-scope)
   - [User Stories](#user-stories)
   - [Use Cases](#use-cases)
   - [Non-Functional Requirements](#non-functional-requirements)
   - [Glossary](#glossary)
8. [Appendix: Instructions for manual testing]()
   


## Acknowledgements
Thanks
   

## Setting up, getting started

This Developer Guide describes the design, architecture, and implementation of the Javatro application. It includes UML diagrams and explanations to help developers understand and contribute to the project.

1. **Ensure JDK 17 or later is installed**.
2. **Import the project as a Gradle project**.
3. **Build and run the application using the provided Gradle tasks**.

## Design

### Architecture

The application follows a **Model-View-Controller (MVC)** architecture pattern. The core logic, display components, and manager are modularized to promote separation of concerns.

```mermaid
graph TD;
    Javatro --> JavatroCore;
    Javatro --> UI;
    Javatro --> JavatroManager;
    
    JavatroCore --> CommandMap;
    CommandMap --> Options;
    Options --> SortBySuitOption;
    Options --> SortByRankOption;
    Options --> PokerHandOption;
    Options --> CardSelectOption;
    Options --> ResumeGameOption;

    JavatroCore --> GameState;  
    JavatroCore --> GameRules;  

    UI --> Screen;
    Screen --> GameScreen;
    Screen --> HelpScreen;
    Screen --> StartScreen;
    Screen --> DiscardScreen;  

    JavatroManager --> JavatroCore; 
    JavatroManager --> UI;

    JavatroCore --> Model;  
    Model --> Deck;
    Model --> Ante;
    Model --> PokerHand;
    Model --> PlanetCard;

    Deck --> Card;
    Ante --> HandResult;
    HandResult --> HoldingHand;

```


#### Application Initialization

```mermaid
graph TD;
    Javatro --> JavatroCore;
    Javatro --> UI;
    Javatro --> JavatroManager;
    
    JavatroCore --> CommandMap;
    CommandMap --> Options;
    Options --> SortBySuitOption;
    Options --> SortByRankOption;
    Options --> PokerHandOption;
    Options --> CardSelectOption;
    Options --> ResumeGameOption;

    JavatroCore --> Round;
    Round --> RoundActions;
    Round --> RoundConfig;
    Round --> RoundObservable;
    Round --> RoundState;

    RoundObservable --> UI; 
    Round --> Score;
    
    JavatroCore --> Model;  
    Model --> Deck;
    Model --> Ante;
    Model --> PokerHand;
    Model --> PlanetCard;

    Deck --> Card;
    Ante --> HandResult;
    HandResult --> HoldingHand;
    
    JavatroCore --> Jokers;
    Jokers --> HeldJokers;
    Jokers --> JokerFactory;
    Jokers --> Joker;
    
    JokerFactory --> AbstractJoker;
    AbstractJoker --> OddToddJoker;
    AbstractJoker --> ScaryFaceJoker;
    AbstractJoker --> GluttonousJoker;
    AbstractJoker --> GreedyJoker;
    AbstractJoker --> HalfJoker;
    AbstractJoker --> LustyJoker;
    AbstractJoker --> WrathfulJoker;

    UI --> Screen;
    Screen --> GameScreen;
    Screen --> HelpScreen;
    Screen --> StartScreen;
    Screen --> DiscardScreen;

    JavatroManager --> JavatroCore;
    JavatroManager --> UI;
```

#### Gameplay Interaction

```mermaid
sequenceDiagram
    participant User
    participant UI
    participant JavatroManager
    participant JavatroCore
    participant PokerHand

    User ->> UI: Make a Move (e.g., Play a Hand)
    UI ->> JavatroManager: Process Input
    JavatroManager ->> JavatroCore: Update Game State
    JavatroCore ->> PokerHand: Evaluate Hand
    PokerHand ->> JavatroCore: Return Evaluation Result
    JavatroCore ->> JavatroManager: Update Score
    JavatroManager ->> UI: Update Display
```

## UI Component

```mermaid  
sequenceDiagram  
    participant JavatroCore  
    participant UI  
    participant GameScreen  

    JavatroCore->>GameScreen: firePropertyChange("currentScore", 200)  
    GameScreen->>GameScreen: Update roundScore  
    GameScreen->>GameScreen: Re-render UI  
    GameScreen->>UI: Call displayScreen()  
    UI->>UI: Clear terminal  
    UI->>GameScreen: Render card art/stats  
```  
*Illustrates how `GameScreen` updates its display when the game state changes.*  



---

```mermaid  
classDiagram  
    class UI {  
        -currentScreen: Screen  
        -previousScreen: Screen  
        +setCurrentScreen(Screen)  
        +printBorderedContent(...)  
        +centerText(...)  
    }  

    class Screen {  
        -commandMap: List~Option~  
        +displayScreen()*  
        +displayOptions()  
    }  

    class GameScreen {  
        -blindScore: int  
        -roundScore: long  
        +propertyChange(...)  
    }  

    UI --> Screen : manages  
    Screen <|-- GameScreen : extends  
    GameScreen ..|> PropertyChangeListener : implements  
```  
*Simplified to highlight core relationships. The `UI` manages `Screen` instances, and `GameScreen` extends `Screen` while observing game state changes.*  



### 1. Overview  
The **UI module** manages screen transitions, user input, and formatted content rendering (e.g., bordered menus, card art). Designed using the **Singleton pattern**, it ensures a single point of control for display operations. This guide details the architecture, key components, and enhancements like dynamic screen rendering and transition logic.  

---

### 2. Architectural Design  

#### 2.1. High-Level Architecture  
The UI follows a **Model-View-Controller (MVC)**-inspired design:  
- **Model**: `JavatroCore` (game state).  
- **View**: `Screen` subclasses (e.g., `GameScreen`, `StartScreen`).  
- **Controller**: `UI` class (manages transitions, input parsing).  

##### Key Design Patterns:  
1. **Singleton Pattern**: Ensures a single `UI` instance for centralized screen management.  
2. **Observer Pattern**: `GameScreen` listens to `JavatroCore` for state changes (e.g., score updates).  
3. **State Pattern**: Screens encapsulate state-specific behavior (e.g., `HelpScreen` vs. `GameScreen`).  

#### 2.2. Rationale for Key Decisions  
- **ANSI/Unicode Formatting**: Enables rich terminal UIs without external libraries.  
- **Abstract `Screen` Class**: Promotes code reuse (e.g., `displayOptions()` for command menus).  
- **Centralized `UI` Class**: Simplifies debugging and ensures consistent transitions.  

---

### 3. Component-Level Design  

#### 3.1. The `UI` Class  
**Responsibilities**:  
- **Screen Management**: Stores static references to all screens and handles transitions via `setCurrentScreen()`.  
- **Formatted Output**: Methods like `printBorderedContent()` and `centerText()` standardize layouts.  
- **Utility Functions**: `clearScreen()`, `getCardArtLines()` (renders ASCII card art).  

**Key Code Snippet**:  
```java  
public void setCurrentScreen(Screen screen) {  
    previousScreen = currentScreen;  
    currentScreen = screen;  
    currentScreen.displayScreen(); // Triggers screen-specific rendering  
    PARSER.getOptionInput(); // Handle user input  
}  
```  

#### 3.2. The `Screen` Class Hierarchy  
- **Abstract `Screen` Class**:  
  - Defines `displayScreen()` (abstract) and `displayOptions()` (common command menu).  
  - Manages `commandMap` (list of user-selectable options).  

- **Concrete Screens**:  
  - **`GameScreen`**: Displays real-time game stats, card art, and listens for state changes.  
  - **`HelpScreen`**: Shows static help content.  

**Example: `GameScreen` Property Listener**  
```java  
public void propertyChange(PropertyChangeEvent evt) {  
    switch (evt.getPropertyName()) {  
        case "currentScore":  
            roundScore = (Long) evt.getNewValue();  
            break;  
        // ... other cases  
    }  
    displayScreen(); // Re-render UI on state change  
}  
```  

---

### 5. Key Enhancements  

#### 5.1. Enhancement: Screen Transition Mechanism  
**Implementation**:  
- **Transition Messages**: ANSI-formatted logs (e.g., `Transitioning to: GameScreen`).  
- **Back Navigation**: `previousScreen` allows reverting to prior states (e.g., exiting `HelpScreen`).  

**Sequence Diagram**:  
```mermaid  
sequenceDiagram  
    actor User  
    participant UI  
    participant StartScreen  
    participant GameScreen  

    User->>UI: Enters "play" command  
    UI->>UI: setCurrentScreen(GameScreen)  
    UI->>StartScreen: Save as previousScreen  
    UI->>GameScreen: displayScreen()  
    GameScreen-->>UI: Rendered content  
```  

**Alternatives Considered**:  
- **Stack-Based History**: Rejected due to limited navigation depth requirements.  
- **Immediate Input Handling**: Chose deferred parsing via `Parser` to decouple input from rendering.  

#### 5.2. Enhancement: Dynamic Card Rendering  
- **Card Art Generation**: `CardRenderer` converts `Card` objects to ASCII art with ANSI colour formatting.  
- **Grid Layout**: Renders 8 cards in two rows, spaced with ANSI backgrounds.  

**Code Snippet**:  
```java  
public static List<String> getCardArtLines(List<Card> hand) {  
    List<String> lines = new ArrayList<>();  
    String[][] renderedCards = hand.stream().map(CardRenderer::renderCard).toArray(String[][]::new);  
    // Combine lines horizontally  
    return lines;  
}  
```

#### **5.3. Challenge: Emoji and Unicode Compatibility**  

##### **Initial Attempt**  
Emojis and Unicode symbols (e.g., 🃏, ♥️, ♥) were initially incorporated into the UI to enhance visual appeal. For example:  
```java  
// Early prototype (discarded)  
private static String getSuitSymbol(Card.Suit suit) {  
    return switch (suit) {  
        case HEARTS -> "♥️";  
        case DIAMONDS -> "♦️";  
        case CLUBS -> "♣️";  
        case SPADES -> "♠️";  
    };  
}  
```  

##### **Challenges**  
1. **Inconsistent Spacing**:  
   - Emojis/Unicode symbols vary in display width (e.g., `♥️` vs. `♠️`), causing misalignment in card art.  
   - Example: A heart symbol might be very slightly larger than a regular spacing, breaking grid layouts.  

2. **Terminal Compatibility**:  
   - Terminals like **Windows CMD / Windows Powershell** require `chcp 65001` to display Unicode, which is error-prone for users.  
   - Colored emojis are unsupported in many terminals, leading to monochrome or placeholder symbols (e.g., ` `).  

3. **ANSI Overlap**:  
   - Combining ANSI color codes with Unicode symbols introduced unpredictable formatting (e.g., background colors bleeding into adjacent text).  

##### **Final Approach**  
To ensure cross-terminal compatibility and consistent spacing, **letters** (`H`, `D`, `C`, `S`) replaced symbols:  
```java  
// Current implementation (CardRenderer.java)  
private static String getSuitSymbol(Card.Suit suit) {  
    return switch (suit) {  
        case HEARTS -> "H";  
        case DIAMONDS -> "D";  
        case CLUBS -> "C";  
        case SPADES -> "S";  
    };  
}  
```  
- **Advantages**:  
  - Fixed-width characters ensure alignment in card art.  
  - Works universally across terminals without configuration.  
  - ANSI colors apply cleanly to letters (e.g., `H` in red for hearts).  

---

#### **UML Diagrams**  

##### **CardRenderer Class Diagram**  
```mermaid  
classDiagram  
    class CardRenderer {  
        +renderCard(Card): String[]  
        -getSuitSymbol(Suit): String  
        -getColour(Suit): String  
    }  
```  
*The `CardRenderer` class maps suits to letters (`H`, `D`, `C`, `S`) and colors using ANSI codes.*  

##### **Card Rendering Sequence Diagram**  
```mermaid  
sequenceDiagram  
    participant GameScreen  
    participant CardRenderer  
    participant Card  

    GameScreen->>CardRenderer: renderCard(card)  
    CardRenderer->>Card: getSuit()  
    CardRenderer->>CardRenderer: getSuitSymbol() → "H"  
    CardRenderer->>CardRenderer: getColour() → ANSI code  
    CardRenderer-->>GameScreen: [" H ", ...]  
    GameScreen->>UI: Display formatted card art  
```

#### **Key Takeaways**  
- **Prioritize Functionality Over Aesthetics**: Letters ensured reliability, while emojis introduced terminal-specific bugs.  
- **Test Across Environments**: The decision to avoid Unicode was driven by rigorous testing in CMD, PowerShell, and Unix terminals.  
- **Code Hygiene**: Unused symbols (e.g., `♠` in `UI`) should be removed in future refactoring.  

This approach balances visual clarity with technical robustness, ensuring the UI works seamlessly for all users.

---

### 6. Future Considerations  

1. **GUI Framework Integration**: Migrate to JavaFX for animations and mouse support.  
2. **Theming System**: Allow users to customize colors via config files.  
3. **Accessibility Mode**: Add high-contrast themes and screen-reader support.  

---

### 7. Conclusion  
The UI module’s design prioritizes modularity, extensibility, and terminal compatibility. By combining design patterns like Singleton and Observer with ANSI formatting, it delivers a responsive and visually consistent experience. Future developers can extend it by adding new `Screen` subclasses or integrating modern GUI frameworks.


## Model Component

## **Ante and Blind class system :**

### **1. Introduction**

The **Ante and Blind System** in the game determines the minimum required score to beat for each round and regulates the increasing stakes as the game progresses. The **Ante** sets a base score to beat, while the **Blind** system introduces different blind levels (SMALL, LARGE, BOSS) each with a unique multiplier score effect to ensure competitive play. This section provides an in-depth look into its implementation, design choices, and a UML sequence diagram illustrating interactions.

### **2. Implementation**

#### **2.1 Class Overview**

The Ante class manages the ante value and blind progression. There are a total of 8 antes where each ante consists of 3 blinds (SMALL, LARGE, BOSS). The ante class maintains the current ante level the gameplay is at , determines the betting stakes, and updates the blind stage as the game progresses. 

#### **2.2 Attributes**

_int MAX_ANTE_COUNT_ = 8; – Defines the maximum ante level progression.
_int anteCount;_ – Stores the current ante level.
_Blind blind;_ – Represents the current blind stage.
_int[] anteScore;_ – An array storing predefined ante values for each level.

#### **2.3 Enum: Blind Levels**

The **Blind** enum represents different blind levels, each with a unique multiplier affecting the ante base score. Additionally, the **SMALL** and **LARGE** blinds are optional, giving the players a choice to accept or reject, meanwhile the **BOSS** blind is compulsory for the players to proceed to the next round.

**Code Snippet** :

```
public enum Blind {
SMALL_BLIND(1.0, "SMALL BLIND"),
LARGE_BLIND(1.5, "LARGE BLIND"),
BOSS_BLIND(2.0, "BOSS BLIND");

private final double multiplier;
private final String name;

Blind(double multiplier, String name) {
this.multiplier = multiplier;
this.name = name;
}
public double getMultiplier() {
return multiplier;
}
public String getName() {
return name;
}
}
```

#### **2.4 Methods**
**_public Ante()_:** 
Initializes ante at level 1 with Small Blind.

**public int getRoundScore()** :
Returns the ante score multiplied by the unique blind multiplier.

**_public void setAnteCount(int anteCount)_:**
Updates the ante count within valid bounds.

**_public void resetAnte()_:**
Resets ante to level 1 and Small Blind.

**_public void nextRound()_:**
Progresses to the next blind level or increases ante level when Boss Blind is reached.

**### 3. Design Considerations**

## ** 3.1 Alternatives Considered**

**1. Fixed Ante and Blind System**:
   Pros: Simple to implement and requires minimal logic.
   Cons: Lacks adaptability, making the game less engaging over time.

**2. Dynamic Blind Adjustment**:
   Pros: Adjusts difficulty dynamically, providing a more balanced progression.
   Cons: Requires additional tracking and logic to manage changes effectively.

   ## **3.2 Chosen Approach**
   To balance simplicity and dynamic progression, the enum-based structured Blind System. It cycles through Small Blind → Large Blind → Boss Blind, ensuring controlled progression while increasing the ante at the end of each cycle. This approach keeps gameplay engaging without overcomplicating the logic. Moreover the array-based ante progression simplifies lookup and ensures consistent bet increases. This structure prevents unfair jumps in bet sizes, ensuring a fairer experience for the players.

## **Class Diagram:** 

```mermaid 
classDiagram
classDiagram
    class Ante {
        -anteCount: int
        -blind: Blind
        -anteScore: int[]
        +Ante()
        +getRoundScore() int
        +getAnteScore() int
        +setAnteCount(anteCount: int)
        +resetAnte()
        +nextRound()
        +setBlind(blind: Blind)
        +getBlind() Blind
        +getAnteCount() int
    }

    class Blind {
        <<enumeration>>
        SMALL_BLIND
        LARGE_BLIND
        BOSS_BLIND
        +getMultiplier() double
        +getName() String
    }

    Ante --> Blind : uses
```  
The Ante class manages the ante count, blind levels, and scores in the game, utilizing the Blind enumeration for different blind levels.

```mermaid 
classDiagram
class Option {
<<interface>>
+getDescription(): String
+execute(): void
}

    class AcceptBlindOption {
        +getDescription(): String
        +execute(): void
    }

    class RejectBlindOption {
        +getDescription(): String
        +execute(): void
    }

    class JavatroCore {
        +getAnte(): Ante
        +currentRound: Round
    }

    class JavatroManager {
        +setScreen(screen: Screen): void
    }

    class UI {
        +getGameScreen(): Screen
        +getBlindScreen(): Screen
        +printBorderedContent(title: String, lines: List): void
        +BOLD: String
        +END: String
    }

    Option <|.. AcceptBlindOption : implements
    Option <|.. RejectBlindOption : implements
    AcceptBlindOption --> JavatroCore : interacts with
    RejectBlindOption --> JavatroCore : interacts with
    AcceptBlindOption --> JavatroManager : interacts with
    RejectBlindOption --> JavatroManager : interacts with
    AcceptBlindOption --> UI : interacts with
    RejectBlindOption --> UI : interacts with
```  
The Option interface is implemented by AcceptBlindOption and RejectBlindOption, which interact with JavatroCore, JavatroManager, and UI to manage blind options in the game.

## **Sequence Diagram:**

```mermaid 
sequenceDiagram
    participant Ante
    participant Blind

    Ante->>Blind: setBlind(Blind)
    Blind->>Ante: getBlind()
    Ante->>Blind: getMultiplier()
    Ante->>Blind: getName()
    Ante->>Ante: getAnteCount()
    Ante->>Ante: setAnteCount(anteCount)
    Ante->>Ante: getAnteScore()
    Ante->>Ante: nextRound()
    Ante->>Ante: resetAnte()
```
This sequence diagram illustrates the interactions between the Ante and Blind classes, where Ante sets and retrieves blind information, as well as manages ante-related operations such as updating scores and counts.

```mermaid 
sequenceDiagram
    participant Option
    participant AcceptBlindOption
    participant RejectBlindOption
    participant JavatroCore
    participant JavatroManager
    participant UI

    Note over AcceptBlindOption, RejectBlindOption: Implements Option interface

    AcceptBlindOption->>JavatroCore: interact with
    RejectBlindOption->>JavatroCore: interact with

    AcceptBlindOption->>JavatroManager: interact with
    RejectBlindOption->>JavatroManager: interact with

    AcceptBlindOption->>UI: interact with
    RejectBlindOption->>UI: interact with

    AcceptBlindOption->>UI: getGameScreen()
    RejectBlindOption->>UI: getBlindScreen()

    AcceptBlindOption->>JavatroCore: beginGame()
    RejectBlindOption->>JavatroCore: updateAnte()
```
This sequence diagram shows the interactions between the Option implementations (AcceptBlindOption and RejectBlindOption), and their interactions with JavatroCore, JavatroManager, and UI, including actions such as beginning the game, updating ante, and retrieving screens.

---

### Logic Component

```mermaid
sequenceDiagram
    participant User
    participant JavatroCore
    participant CommandMap
    participant Options
    participant UI
    participant Manager

    User ->> UI: Issue Command
    UI ->> Manager: Forward Command
    Manager ->> JavatroCore: Process Command
    JavatroCore ->> CommandMap: Check Command Type
    CommandMap ->> Options: Execute Option (e.g., Sort, Discard, Select)
    Options ->> JavatroCore: Return Result
    JavatroCore ->> Manager: Update State
    Manager ->> UI: Render Updated State
```

The Logic Component serves as the Controller in the Model-View-Controller (MVC) architecture. It is responsible for managing the application's core logic, processing user commands, and interacting with the Model (e.g., Deck, Ante, PokerHand) through well-defined interfaces. It updates the View through the manager classes.

All classes within the core package make up the Logic Component. This does not include the Deck, Ante, and PokerHand classes, as these are part of the Model. Instead, the Logic Component uses these classes to perform operations and process commands.

- `JavatroCore`: The central controller managing the game flow, initializing components, and processing game state updates.

This component interacts with the UI and Manager components to process user inputs and update the view accordingly.

---

### Storage Component

```mermaid
sequenceDiagram
    participant User
    participant JavatroCore
    participant Storage

    User ->> JavatroCore: Save Game
    JavatroCore ->> Storage: Serialize Game State
    Storage ->> File: Write to Disk

    User ->> JavatroCore: Load Game
    JavatroCore ->> Storage: Read From Disk
    Storage ->> JavatroCore: Deserialize Game State
```

The Storage component is responsible for saving and loading game states. Although not fully implemented, this component would ideally include:
- Persistent storage management using file handling or databases.
- Serialization and deserialization of game objects.
- Handling user preferences and game settings.
---
### Round Component

The **Round Component** is responsible for managing the state and flow of a single round in the game. It encapsulates the logic for handling round-specific actions, configurations, and any special effects.

#### Responsibilities
- Manage the state of the current round, including the cards dealt, scores, and player actions.
- Handle round-specific configurations and validation.
- Notify other components (e.g., UI, Logic) of state changes during the round.

#### Key Classes
- **Round**: The main class that orchestrates the round's lifecycle.
- **RoundState**: Tracks the current state of the round (e.g., active, completed).
- **RoundActions**: Encapsulates actions that can be performed during the round (e.g., draw, discard).
- **RoundConfig**: Stores configuration settings for the round (e.g., max cards, scoring rules).
- **RoundObservable**: Implements the observer pattern to notify listeners (e.g., UI) of state changes.

``` mermaid
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

    Round --> RoundState: contains
    Round --> RoundConfig: contains
    Round --> RoundObservable: contains
    Round ..> RoundActions: uses
```

#### Sequence Diagram: Round Initialisation

The following sequence diagram illustrates the initialization process of a round, including configuration setup and state management:

```mermaid
sequenceDiagram
    participant User
    participant Round
    participant RoundConfig
    participant RoundState
    participant RoundObservable

    User ->>+ Round: new Round(...)
    Note right of Round: Constructor activated
    Round ->>+ RoundConfig: new RoundConfig(...)
    Note right of RoundConfig: Constructor activated
    RoundConfig -->>- Round: this.config : RoundConfig
    Round ->>+ RoundState: new RoundState(...)
    Note right of RoundState: Constructor activated
    RoundState -->>- Round: this.state : RoundState
    Round ->>+ Round: applyDeckInvariants()
    alt RED
        Round ->>+ RoundState: increaseRemainingDiscards(: int)
        RoundState -->>- Round: 
    else BLUE
        Round ->>+ RoundState: setRemainingPlays(: int)
        RoundState -->>- Round: 
    end
    Round -->>- Round: 
    Round ->> Round: Other configuration...
    Round ->>+ RoundObservable: new RoundObservable
    Note right of RoundObservable: Constructor activated
    RoundObservable -->>- Round: this.observable : RoundObservable
    Round -->>- User: Round instance
```

Note: Due to limitations of mermaid rendering, the activation bars for constructors may not appear exactly aligned with their corresponding object lifelines. The note in the diagram clarifies when the constructor is activated.

### Score Component
The **Score Component** is part of Javatro's scoring system, responsible for calculating the final score based on the played hand, cards, and active jokers. It encapsulates the logic for handling score calculations.


#### Responsibilities
- Handle calculation of scoring played hands, factoring in round-specific configurations and validation.

#### Class Diagram: Score and involved classes
``` mermaid
classDiagram
    class Score {
        +double totalChips
        +double totalMultiplier
        +static List<Card> playedCardsList
        +ArrayList<Joker> jokerList
        -Round.BossType bossType
        +Score()
        +Score(Round.BossType bossType)
        +long getScore(PokerHand pokerHand, List<Card> playedCardList, HeldJokers heldJokers)
        -static long calculateFinalScore(double totalChips, double totalMultiplier)
        -boolean isValidCard(Card card)
        -void scoreCard(Card card)
    }
    
    class PokerHand {
        +int getChips()
        +int getMult()
    }
    
    class Card {
        +getChips()
    }
    
    class Rank {
    <<enumeration>>
        TWO
        THREE
        FOUR
        FIVE
        SIX
        SEVEN
        EIGHT
        NINE
        TEN
        JACK
        QUEEN
        KING
        ACE
    }

    class Suit {
    <<enumeration>>
        HEARTS
        CLUBS
        SPADES
        DIAMONDS
        +getName()
    }

    class HeldJokers {
        -int HOLDING_LIMIT
        +List~Joker~ getJokers()
    }
    
    class Joker {
        <<abstract>>
        #String name;
        #String description;
        #String path;
        +String getName()
        +interact()*
    }

    class ScoreType {
        <<enumeration>>
        AFTERHANDPLAY
        ONCARDPLAY
    }
    
    class HandType {
        <<enumeration>>
        FLUSH_FIVE
        FLUSH_HOUSE
        FIVE_OF_A_KIND
        ROYAL_FLUSH
        STRAIGHT_FLUSH
        FOUR_OF_A_KIND
        FULL_HOUSE
        FLUSH
        STRAIGHT
        THREE_OF_A_KIND
        TWO_PAIR
        PAIR
        HIGH_CARD
    }
    
    class BossType {
        <<enumeration>>
        NONE
        THE_NEEDLE
        THE_WATER
        THE_CLUB
        THE_WINDOW
        THE_HEAD
        THE_GOAD
        THE_PLANT
        THE_PSYCHIC
    }

    Score --> PokerHand
    Score --> Card
    Score -->"1" HeldJokers
    Score -->"1" Round
    Round -->"1" BossType
    PokerHand -->"1" HandType
    Card -->"1" Rank
    Card -->"1" Suit
    HeldJokers -->"0..5" Joker
    Joker -->"1" ScoreType
```

#### Score Calculation Overview
Below is how the Score Class returns the calculated score:

1. `Round` initializes `Score`, and `Score` invokes the `getChips()` method from `PokerHand`.
```mermaid
sequenceDiagram
   activate Round
   Round->>+Score: getScore(PokerHand pokerHand, List<Card> playedCardList, HeldJokers heldJokers)
   Score->>+PokerHand: getChips(), getMultiplier()
   PokerHand-->>-Score: return chips, return multiplier
   deactivate Round

```
2. With each `Card` passed from `Score`, `Score` checks whether the card played satisfies BossType conditions, 
and applies effects if the condition is satisfied. 
Else, the `Card` is scored, and each `Joker` in `heldJokers` is checked if the `Card` can interact with the `Joker`

```mermaid
sequenceDiagram
    box Add contribution from valid cards  
        participant Score
        participant PokerHand
        participant Joker
    end
    activate Score
    loop Card card: playedCardList
        activate Score
        Score->>Score: Apply BossType conditions
        deactivate Score
        alt Card is valid
            activate Score
            Score->>Score: scoreCard(Card card)
            deactivate Score
            loop [Joker joker: heldJokers]
                alt Joker == ONCARDPLAY
                    Score->>+Joker: interact(Score score, Card card)
                    Joker-->>-Score: interact
                end
            end
        end
        deactivate Score
    end
```

3. After each `Card` is scored, any `Joker` with the `AFTERHANDPLAY` enum is interacted with.
```mermaid
sequenceDiagram
    activate Score
    loop [Joker joker: heldJokers]
        alt joker.ScoreType == AFTERHANDPLAY
            Score->>+Joker: interact(Score score, Card card)
            Joker-->>-Score: interact
        end
    end
    deactivate Score
```


4. `Score` rounds the final score using `calculateFinalScore()` and returns the final `long` value to `Round`.
```mermaid
sequenceDiagram
    activate Round
    activate Score
    activate Score
    Score->>Score: calculateFinalScore()
    deactivate Score
    Score-->>Round: return final score
    deactivate Score
    deactivate Round

```

#### Overall Sequence Diagram for Score Calculation

```mermaid
sequenceDiagram
box transparent Overall Scoring Algorithm
    participant Round
    participant Score
    participant PokerHand
    participant Joker
end


    Round->>+Score: getScore(PokerHand pokerHand, List<Card> playedCardList, HeldJokers heldJokers)
    Score->>+PokerHand: getChips(), getMultiplier()
    PokerHand-->>-Score: return chips, return multiplier
    loop Card card: playedCardList
        activate Score
        Score->>Score: Check BossType conditions
        deactivate Score
        alt Card is valid
            activate Score
            Score->>Score: scoreCard(Card card)
            deactivate Score
            loop [Joker joker: heldJokers]
                alt joker.ScoreType == ONCARDPLAY
                    Score->>+Joker: interact(Score score, Card card)
                    Joker-->>-Score: interact
                end
            end
        end
    end
    loop [Joker joker: heldJokers]
        alt joker.ScoreType == AFTERHANDPLAY
            Score->>+Joker: interact(Score score, Card card)
            Joker-->>-Score: interact
        end
    end
    Score->>Score: calculateFinalScore()
    Score-->>-Round: return final score

```




## Implementation

### Logic Flow

The application's logic flow is divided into several parts:

1. **Initialization**: The application initializes core components and prepares the user interface.
2. **Gameplay**: Users interact with the UI to perform actions, triggering methods in the core logic.
3. **Round Progression**: Ante handling and deck operations are updated between rounds.
4. **Evaluation**: Poker hands are evaluated using the `PokerHand` class.

### Overview

The Javatro application consists of several packages:

- ``: The main entry point and application setup.
- ``: Core logic, including card handling, ante management, and game rules.
- ``: User interface handling, including screens and rendering.
- ``: Management of different game states and transitions.

### Key Components

- `Javatro`: Initializes and starts the application.
- `JavatroCore`: Handles core game logic such as dealing cards, evaluating hands, and managing ante.
- `Deck`: Manages the deck of cards, shuffling, drawing, etc.
- `Ante`: Handles the ante mechanism, score calculation, and progression through rounds.
- `PokerHand`: Handles poker-specific card evaluation.

### Important Methods

1. 

   - `main()`: Initializes the application.

2. 

   - `initialize()`: Sets up core components.
   - `playRound()`: Manages the game round progression.

3. 

   - `draw()`: Draws a card from the deck.
   - `shuffle()`: Shuffles the deck.

4. 

   - `getRoundScore()`: Retrieves the score for the current round.
   - `nextRound()`: Progresses to the next round.

5. 

   - `evaluateHand()`: Evaluates a set of cards according to poker rules.

---

## Documentation, logging, testing, configuration, dev-ops

### Documentation

The documentation is structured as follows:

- **User Guide**: Located at `docs/UserGuide.md`. Describes how to set up and use the application.
- **Developer Guide**: Located at `docs/DeveloperGuide.md`. Contains design explanations, architecture, and implementation details.
- **API Documentation**: Generated using Javadoc. To generate, run:

```
./gradlew javadoc
```

Output will be available under `build/docs/javadoc/`.

- Rest To Be Add

---

## Appendix: Requirements

## Product Scope

### Target user profile:
- Card game enthusiasts and roguelike fans who enjoy strategic deck-building experiences and prefer playing games in a command-line interface (CLI) environment.
- Tech-savvy individuals who are comfortable with command-line interfaces and prefer typing commands over using graphical user interfaces.
- Roguelike and card game enthusiasts who enjoy strategic, turn-based gameplay and have an interest in card games, particularly those with roguelike elements.
- Minimalist gamers who appreciate minimalist design and are drawn to text-based gaming experiences that focus on gameplay depth rather than visual aesthetics.
- Cross-platform users who use various operating systems and seek a consistent gaming experience across platforms, which a CLI application can provide.

### Value proposition: 
Javatro provides a streamlined, text-based roguelike deck-building experience inspired by Balatro. It offers a strategic and replayable poker-based gameplay loop in a lightweight, no-graphics format, making it accessible for users who enjoy card games without requiring a graphical interface.

## User Stories

**Priorities:** High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | User Role       | User Story                                                                                        |
|----------|-----------------|---------------------------------------------------------------------------------------------------|
| * * *    | New Player      | I want to start a new game so that I can begin a new deck-building run.                           |
| * * *    | Active Player   | I want to play a hand of poker using my cards so that I can progress in the game.                 |
| * * *    | Active Player   | I want to discard and redraw a limited number of cards so that I can improve my current hand.     |
| * * *    | Active Player   | I want to see my score and chips after playing a hand so that I can keep track of my progress.    |
| * * *    | Active Player   | I want to earn multipliers based on the poker hand I complete so that I can maximize my score.    |
| * * *    | New Player      | I want to view instructions so that I can understand how to play the game.                        |
| * * *    | Active Player   | I want to save my progress so that I can resume the game later.                                   |
| * * *    | Active Player   | I want to load a saved game so that I can continue my progress.                                   |
| * *      | Active Player   | I want to sort my cards by rank or suit so that I can better organize my hand.                    |
| * *      | Active Player   | I want to view game statistics so that I can track my performance over multiple runs.             |
| * *      | Advanced Player | I want to enable advanced features so that I can enjoy a more complex gameplay experience.        |
| * *      | Player          | I want to view a help menu so that I can learn more about the available commands.                 |
| *        | Active Player   | I want to customize gameplay settings so that I can tailor the game experience to my preferences. |
| *        | Advanced Player | I want to use additional deck types so that I can have a varied experience.                       |
| *        | Advanced Player | I want to enable hard mode so that I can increase the difficulty of the game.                     |
| *        | Active Player   | I want to view my progress summary after each run so that I can evaluate my performance.          |
| *        | Active Player   | I want to undo my last move so that I can correct mistakes.                                       |
| *        | Active Player   | I want to replay previous rounds so that I can refine my strategies.                              |
| *        | Active Player   | I want to set a high score target so that I can challenge myself.                                 |
| *        | Active Player   | I want to compare my score with other players so that I can see where I stand.                    |
| *        | Active Player   | I want to pause the game so that I can take breaks when necessary.                                |
| *        | Active Player   | I want to change controls so that I can personalize the game experience.                          |
| *        | Active Player   | I want to view a log of previous moves so that I can analyze my strategies.                       |
| *        | Active Player   | I want to restart the game so that I can try different strategies.                                |
| *        | Active Player   | I want to adjust volume settings so that I can control the audio experience.                      |
| *        | Advanced Player | I want to customize card designs so that I can enhance my visual experience.                      |
| *        | Advanced Player | I want to view daily challenges so that I can have new objectives to pursue.                      |
| *        | Advanced Player | I want to participate in weekly events so that I can earn special rewards.                        |
| *        | Advanced Player | I want to access leaderboards so that I can compare my performance globally.                      |
| *        | Advanced Player | I want to unlock achievements so that I can track my milestones.                                  |
| *        | Advanced Player | I want to review past games so that I can learn from my mistakes.                                 |
| *        | Advanced Player | I want to export my progress so that I can preserve my achievements.                              |

## Use Cases

(For all use cases below, the **System** is the `Javatro` and the **Actor** is the `User`, unless specified otherwise)

---

### Use case: Discard and Draw Cards

**MSS (Main Success Scenario)**

1. User requests to view their current hand.
2. System displays the current hand of cards.
3. User selects cards to discard.
4. System removes the selected cards and draws new cards to replace them.
5. System displays the updated hand.

   Use case ends.

**Extensions**

- 2a. The hand is empty.
  - Use case ends.

- 3a. The user selects invalid cards for discarding.
  - 3a1. System shows an error message.
  - Use case resumes at step 2.

---

### Use case: Save Game Progress

**MSS (Main Success Scenario)**

1. User requests to save the game.
2. System serializes the current game state.
3. System saves the serialized data to a file.
4. System confirms that the game has been successfully saved.

   Use case ends.

**Extensions**

- 3a. The file cannot be created or written.
  - 3a1. System shows an error message indicating failure to save.
  - Use case ends.

---

### Use case: Load Saved Game

**MSS (Main Success Scenario)**

1. User requests to load a previously saved game.
2. System presents a list of available saved game files.
3. User selects a saved game file to load.
4. System reads the file and deserializes the game state.
5. System initializes the game to the loaded state.

   Use case ends.

**Extensions**

- 2a. No saved game files are found.
  - Use case ends.

- 4a. The selected file is corrupted or incompatible.
  - 4a1. System shows an error message.
  - Use case ends.

---

## Non-Functional Requirements

### Performance Requirements
- The system should be able to process user commands and update the game state within **0.5 seconds** of user input.
- The system should support a **minimum of 1000 hands played** without noticeable performance degradation.

### Reliability Requirements
- The game should **not crash or produce errors** during normal gameplay for at least **20 hours of continuous use**.
- Saved game data must be **correctly serialized and deserialized** to ensure that saved progress is accurately restored.

### Availability Requirements
- The application should be available **99.9% of the time** when executed on a compatible platform.
- Saved games should remain accessible even after **abrupt termination of the application**.

### Usability Requirements
- The system should provide **clear and concise error messages** when invalid commands are entered.
- The CLI interface should support **standard text-based commands** and provide detailed help instructions.

### Security Requirements
- Saved game files should be protected from **unauthorized modification** by ensuring **read-only access** unless within the game environment.
- The system should prevent **malformed input** from causing crashes or corrupting game data.

### Portability Requirements
- The game should be compatible with **Windows, Linux, and MacOS**.
- The system should work on **both 32-bit and 64-bit architectures**.

### Maintainability Requirements
- The codebase should maintain a **test coverage of at least 90%** to ensure robustness against changes.
- The architecture should allow for **adding new game rules and features with minimal modification to existing code**.

### Scalability Requirements
- The game should support **future expansion** to include additional game modes or card types without requiring a complete rewrite of the core system.
- The system should handle **at least 10 concurrent games** if implemented in a multiplayer environment.

### Documentation Requirements
- Comprehensive documentation should be available for:
  - **Installation and setup**
  - **Gameplay instructions**
  - **Developer guides** with architecture diagrams and explanations.

### Glossary

- **Ante**: The initial bet placed by players before cards are dealt.
- **Poker Hand**: A set of cards evaluated according to poker rules.
- **Deck**: A collection of cards used in the game.

---

### Dependencies

- TO ADD


