package ui;

import game.Guesser;
import game.SpellCheckerService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

public class GuesserConsole {

  public static void main(String[] args) {
    try {
      List<String> words = readWordsFromFile();
      System.out.println("Hello! Lets play Guess the Word!");
      startGame(words);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
  }

  private static void startGame(List<String> words) {
    Guesser guesser = new Guesser();

    guesser.setSpellCheckProvider(new SpellCheckerService());
    String word = guesser.pickARandomWord(words);
    String scrambledWord = guesser.scrambledAWord(word);

    String guessWord;

    while (true) {
      System.out.println("Guess the scrambled word : " + scrambledWord);
      System.out.println();

      Scanner input = new Scanner(System.in);
      guessWord = input.nextLine();

      System.out.println("Your have guessed: " + guessWord);

      var score = guesser.calculateScore(word, guessWord);

      System.out.println();
      System.out.println("Your score is: " + score);
      System.out.println();

      if (word.equals(guessWord)) {
        System.out.print("You Won!");
        break;
      }
    }

  }

  private static List<String> readWordsFromFile() throws IOException, URISyntaxException {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    String resourceUrl = Paths.get(classloader.getResource("wordList.txt").toURI()).toString();

    if (resourceUrl == null) {
      throw new IllegalArgumentException("File not found!");
    }

    return Files.lines(Paths.get(resourceUrl)).collect(toList());
  }
}