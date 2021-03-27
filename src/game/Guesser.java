package game;

import java.util.*;
import java.util.stream.Stream;

public class Guesser {
  private SpellChecker spellChecker;

  public void setSpellCheckProvider(SpellChecker spellChecker) {
    this.spellChecker = spellChecker;
  }

  public String scrambleWord(String word) {
    var scrambledWord = scrambledAWord(word);
    
    return scrambledWord.equals(word) ? scrambledAWord(word) : scrambledWord;
  }
  
  public String scrambledAWord(String word) {
    var letters = Arrays.asList(word.split(""));
    Collections.shuffle(letters, new Random());

    return String.join("", letters).toLowerCase();    
  }

  public int calculateScore(String actualWord, String guessWord) {
    if(!spellChecker.isSpellingCorrect(guessWord) || !compareWordLogic(actualWord, guessWord)) return 0;

    return Stream.of(guessWord.split(""))
      .mapToInt(this::computeScoreForALetter)
      .sum();
  }

  private boolean compareWordLogic(String actualWord, String guessWord){
    return Stream.of(guessWord.split(""))
      .noneMatch(letter -> countOccurrence(guessWord, letter) > countOccurrence(actualWord, letter));
  }

  private int countOccurrence(String word, String letter) {
    return word.length() - word.replaceAll(letter, "").length();
  }

  private int computeScoreForALetter(String letter) {
    return "aeiou".contains(String.valueOf(letter)) ? 1 : 2;
  }

  public String pickARandomWord(List<String> words) {
    return words.get(new Random().nextInt(words.size()));
  }
}
