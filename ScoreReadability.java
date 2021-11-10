package readability;

import javax.swing.text.StyleContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class ScoreReadability {

    String text;
    int words;
    int characters;
    int sentences;
    int syllables;
    int polysyllables;

    public void startScore(String path) {
        countElements(path);
        System.out.println("The text is:\n" + text);
        System.out.printf("\nWords: %d\nSentences: %d\nCharacters: %d\nSyllables: %d\nPolysyllables: %d"
                , words, sentences, characters, syllables, polysyllables);
        System.out.println("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        String whichScore = new Scanner(System.in).next();
        switch (whichScore) {
            case "ARI":
                System.out.println(automatedReadability());
                break;
            case "FK":
                System.out.println(fleschKincaid());
                break;
            case "SMOG":
                System.out.println(SMOG());
                break;
            case "CL":
                System.out.println(colemanLiau());
                break;
            case "all":
                System.out.println(automatedReadability() + fleschKincaid() + SMOG() + colemanLiau());
        }
    }

    public void countElements(String path) {
        try (InputStream stream = Files.newInputStream(Path.of(path))) {
            text = new String(stream.readAllBytes()).trim();
            words = text.split(" ").length;
            sentences = text.split("[.|?|!]").length;
            characters = text.replace(" ", "").split("").length;
            int syllablesPerWord = 0;
            for (String s : text.toLowerCase().replaceAll("[.,?!]", "").split(" ")) {
                if (s.isEmpty()) {
                    continue;
                }
                if ("aeuio".contains(s.charAt(0) + "")) {
                    syllablesPerWord++;
                }
                for (int i = 1; i < s.length(); i++) {
                    if ("aeuyio".contains(s.charAt(i) + "") && !("aeuyio".contains(s.charAt(i - 1) + ""))
                        && !(i == s.length() - 1 && s.charAt(i) == 'e')) {
                        syllablesPerWord++;
                    }
                }
                if (syllablesPerWord < 1) {
                    syllablesPerWord++;
                }
                syllables += syllablesPerWord;
                if (syllablesPerWord > 2) {
                    polysyllables++;
                }
                System.out.println(s + "   syllablesPerWord " + syllablesPerWord + "   syllables " + syllables);
                syllablesPerWord = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String automatedReadability() {
        double scoreOfAutomatedReadability;
        scoreOfAutomatedReadability = (4.71 * characters / words) + (0.5 * words / sentences) - 21.43;
        return String.format("\nAutomated Readability Index:"
                        + " %f (about %d-year-olds).", scoreOfAutomatedReadability,
                (int)Math.ceil(scoreOfAutomatedReadability + 6));
    }


    public String fleschKincaid() {
        double scoreOfFleshKincaid;
        scoreOfFleshKincaid = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
        return String.format("\nFlesch–Kincaid readability tests: %f (about %d-year-olds)."
        , scoreOfFleshKincaid, (int)Math.ceil(scoreOfFleshKincaid + 6));
    }

    public String SMOG() {
        double scoreOfSMOG;
        scoreOfSMOG = 1.043 *  Math.sqrt(polysyllables * 30 / sentences) + 3.1291;
        return String.format("\nSimple Measure of Gobbledygook: %f (about %d-year-olds)."
        ,scoreOfSMOG, (int)Math.ceil(scoreOfSMOG + 6));
    }

    public String colemanLiau() {
        double scoreOfColemanLiau;
        double L; // average number of character per 100 words
        double S; // average number of sentences per 100 words
        double letters = 0;
        for (String s : text.split(" ")) {
            for (char c : s.toCharArray()) {
                if (Character.isAlphabetic(c)) {
                    letters++;
                }
            }
        }
        L = (letters / words) * 100;
        S = (sentences / words) * 100;
        scoreOfColemanLiau = 0.0588 * L - 0.296 * S - 15.8;
        return String.format("\nColeman–Liau index: %f (about %d-year-olds).",
                scoreOfColemanLiau, (int)Math.ceil(scoreOfColemanLiau + 6));
    }
}

