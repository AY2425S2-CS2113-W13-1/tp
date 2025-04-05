# John Woo Yi Kai - Project Portfolio Page

## Overview

**Javatro** is a roguelike deck-building game played via the command line, drawing inspiration from Balatro. It blends poker strategy with innovative deck mechanics, delivering a streamlined yet deeply engaging experience. With multiple deck types, escalating blind levels, and dynamic scoring systems, the game offers rich replayability and strategic depth.


## Summary of Contributions

### Code Contributed
[Link to tP Code Dashboard](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=markneoneo&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

### Features and Enhancements Implemented:
1. **Joker System**
    - **Features**: Implemented the _Joker_ mechanic, where special effects get applied to cards or hands to enhance your overall score.
    - **Depth**:
        - Implemented an abstract `Joker` class and related sub-classes to implement the Joker mechanic.
        - Implemented the `HeldJokers` class to hold `Jokers` that the user has in the game.
        - Designed the system of giving each player a `Joker` card everytime a Boss Blind is won.
        - Created the `JokerFactory` class using the _Factory_ design pattern to distribute a random `Joker` card.
    - **Considerations**:
        - In normal Balatro, there are many types of Jokers that affect more than just affect your score - such as increasing discards or changing hand sizes. This was considered, but discarded due to time constraints for v2.0.
        - For Jokers that affect score, there are 3 main types: **+Chips**, **+Mult** and **xMult** Jokers. For Javatro, we decided to focus on the first 2 types of Jokers, as **xMult** would increase the complexity of testing for score for card positions.
        - There are 2 types of trigger effects, `ONCARDPLAY` and `AFTERHANDPLAY`. Other ways of triggering the Jokers such as "on discard" were considered but later disregarded as they were too complex to test.


2. **Scoring Algorithm**
   - **Features**: Developed the `Score` class that manages the scoring algorithm of a played hand every round, including factors such as boss-specific rules and Joker effects.
   - **Depth**:
     - Integrated the **Joker** and **Ante** system to work with the Scoring Algorithm 


3. **Deck and Cards in Hand** 
   - **Features**: Implemented `HoldingHand` class that manages the cards a user holds during a round, and the `Deck` holding the cards in a round.


4. **Different Deck Types**
   - **Features**: Implemented different deck types of **Red**, **Blue**, **Checkered** and **Abandoned** to enhance the gameplay experience for unique runs.
   - **Depth**: 
     - Created different methods of instantiating deck types using a `DeckType`
     - Created the `DeckSelectScreen` that allows users to see the effects of each deck does before the start of a game.
   - **Considerations**:
     - Deck types that were related to economy such as the **Yellow** Deck were disregarded due to time constraints and over complexity of implementing a shop based system.
     - The **Plasma** Deck was considered due to its unique scoring mechanism, but ultimately decided against as it would require a major rehaul of the `Ante` and `Score` class to calculate the target score and played hand score.
     - Since the **Abandoned** Deck lacks face cards (K, Q, J), it was possible to run out of cards from the deck. As a result, it was the most troublesome deck to develop for. This issue was fixed by making the default _discards_ per round to **3**, thus making it impossible to run out of cards.

### Contributions to the UG:
- Documented Jokers and their implementation in the game.

### Contributions to the DG:
- Documented design and implementation of the `Score` component, including UML diagrams and sequence diagrams for respective methods.

### Team-Based Tasks:
- Tested and refined mechanics relating to round wins to ensure consistency and correct behaviour.
- Collaborated to seamlessly integrate the `Score` system with key components such as `Round` and `BossType`, maintaining cohesive gameplay flow.
- Assisted in integrating the `Score` component with the overall game flow.
- Took initiative in team meetings to clarify doubts and plan out the objectives of the next release version.

### Review/Mentoring Contributions:
- Reviewed pull requests related to the scoring mechanic and provided timely feedback on how to refactor the code.
- Kept track of and resolved bugs that popped up relating to the scoring mechanic.