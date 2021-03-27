package game;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class SpellCheckerService implements SpellChecker {

  @Override
  public boolean isSpellingCorrect(String word) {
    try {
      return parseText(getResponse(word));
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  String getResponse(String word) throws IOException {
    var url = "http://agilec.cs.uh.edu/spell?check=" + word;

    return new Scanner(new URL(url).openStream()).nextLine();
  }

  boolean parseText(String text) {
    return switch (text) {
      case "true" -> true;
      case "false" -> false;
      default -> throw new IllegalArgumentException("response not boolean, got: something else");
    };
  }
}
