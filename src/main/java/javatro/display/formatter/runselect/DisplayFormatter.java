// author @@flyingapricot
package javatro.display.formatter.runselect;

import javatro.core.Deck.DeckType;
import javatro.storage.DataParser;
import javatro.storage.Storage;
import javatro.storage.utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

public class DisplayFormatter {

    public static List<String> formatRunInformation(Storage storage, int runNumber) {
        List<String> optionLines = new ArrayList<>();

        // Extract deck art and deck name
        String[] cardArt;
        String cardDesc;
        String deckIndexValue = storage.getValue(runNumber - 1, DataParser.DECK_INDEX);

        if (deckIndexValue.charAt(deckIndexValue.length() - 1) == ']') {
            cardArt =
                    CardUtils.fromStorageKey(
                                    deckIndexValue.substring(0, deckIndexValue.length() - 1))
                            .getArtLines();
            cardDesc =
                    CardUtils.fromStorageKey(
                                    deckIndexValue.substring(0, deckIndexValue.length() - 1))
                            .getDeckName();
        } else {
            cardArt = CardUtils.fromStorageKey(deckIndexValue).getArtLines();
            cardDesc = CardUtils.fromStorageKey(deckIndexValue).getDeckName();
        }

        // Deck Label
        String deckLabel = "\u001B[1;4mDECK\u001B[0m"; // Bold + Underlined
        String visibleCardLine = stripAnsi(cardArt[0]);
        int labelPadding = 80 + (visibleCardLine.length() - deckLabel.length()) / 2;
        String paddedDeckLabel = String.format("%" + labelPadding + "s", deckLabel);
        optionLines.add(paddedDeckLabel);
        optionLines.add("");

        // Italicized deck name
        int padding = 81;
        DeckType deckType = CardUtils.deckFromKey(deckIndexValue);
        if (deckType == DeckType.CHECKERED || deckType == DeckType.ABANDONED) {
            padding = 89;
        } else if (deckType == DeckType.RED) {
            padding = 80;
        }

        String italicDeckName = "\u001B[3m" + cardDesc + "\u001B[0m";
        visibleCardLine = stripAnsi(cardArt[0]);
        int deckNamePadding = padding + (visibleCardLine.length() - cardDesc.length()) / 2;
        String paddedDeckName = String.format("%" + deckNamePadding + "s", italicDeckName);

        // Retrieve run-specific data
        String handValue = storage.getValue(runNumber - 1, DataParser.HAND_INDEX);
        String discardValue = storage.getValue(runNumber - 1, DataParser.DISCARD_INDEX);

        String handsOutput = handValue.equals("-1") ? "NA" : handValue;
        String discardsOutput = discardValue.equals("-1") ? "NA" : discardValue;

        int screenSize = 11;

        for (int i = 0; i < screenSize - 1; i++) {
            String leftText = "";
            switch (i) {
            case 0 -> leftText =
                    "Round: " + storage.getValue(runNumber - 1, DataParser.ROUND_NUMBER_INDEX);
            case 1 -> leftText =
                    "Round Score: "
                            + storage.getValue(runNumber - 1, DataParser.ROUND_SCORE_INDEX);
            case 2 -> leftText = "Hands: " + handsOutput + " | Discards: " + discardsOutput;
            case 3, 6 -> leftText = "------------------------";
            case 4 -> leftText =
                    "Ante: " + storage.getValue(runNumber - 1, DataParser.ANTE_NUMBER_INDEX);
            case 5 -> leftText =
                    "Blind: " + storage.getValue(runNumber - 1, DataParser.BLIND_INDEX);
            case 7 -> leftText =
                    "Wins: "
                            + storage.getValue(runNumber - 1, DataParser.WINS_INDEX)
                            + " | Losses: "
                            + storage.getValue(runNumber - 1, DataParser.LOSSES_INDEX);
            default -> leftText = "";
            }

            String rightAnsi = (i < cardArt.length) ? cardArt[i] : "";
            String paddedLeft = String.format("%-67s", leftText);

            optionLines.add(paddedLeft + rightAnsi);
        }

        optionLines.add(paddedDeckName);

        return optionLines;
    }

    private static String stripAnsi(String input) {
        return input.replaceAll("\\u001B\\[[;\\d]*m", "");
    }
}
