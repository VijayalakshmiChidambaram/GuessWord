package game;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;

public class GuesserTest {
  Guesser guesser;
  SpellChecker spellChecker;

  @BeforeEach
  public void init()  {
    guesser = new Guesser();
    spellChecker = Mockito.mock(SpellChecker.class);
    guesser.setSpellCheckProvider(spellChecker);
    Mockito.when(spellChecker.isSpellingCorrect(anyString())).thenReturn(true);
  }

  @Test
  public void testScrambleWords() {
    assertAll(
      () -> assertNotEquals("apple", guesser.scrambleWord("apple")),
      () -> assertNotEquals("matter", guesser.scrambleWord("matter")));
  }
  
  @Test
  public void testScrambleWordRescambledIfResultIsSameAsGiven() {
    guesser = Mockito.spy(Guesser.class);
    
    doReturn("apple", "palep").when(guesser).scrambledAWord("apple");
    
    assertEquals("palep", guesser.scrambleWord("apple"));
  }

  @Test
  public void testScrambleAppleTwice() {
    assertNotEquals(guesser.scrambleWord("apple"), guesser.scrambleWord("apple"));
  }

  @Test
  public void testScrambleAnEmptyString() {
    assertEquals("", guesser.scrambleWord(""));
  }

  @Test
  public void testScrambleWordInMixedCaseToResultLowercase() {
    var scrambled = guesser.scrambleWord("Apple");

    assertEquals(scrambled, scrambled.toLowerCase());
  }

  @Test
  public void testScrambleWordResultHasSameCharacterAsOriginalString() {
    assertEquals(sort("apple"), sort(guesser.scrambleWord("apple")));
  }

  private String sort(String word) {
    return Stream.of(word.split(""))
      .sorted()
      .collect(joining(""));
  }

  @Test
  public void testScoreForaLetterGuessThatIsPartOfWord()  {
    String guessWord = "a";

    assertEquals(1, guesser.calculateScore("apple", guessWord));
  }

  @Test
  public void testScoreForGuessThatIsPartOfTheWord()  {
    String guessWord = "monk";

    assertEquals(7, guesser.calculateScore("monkey", guessWord));
  }

  @Test
  public void testScoreForGuessPartOfWordButNotContinuous()  {
    String guessWord = "ape";

    assertEquals(4, guesser.calculateScore("apple", guessWord));
  }

  @Test
  public void testScoreForGuessWithNoVowels()  {
    String guessWord = "by";

    assertEquals(4, guesser.calculateScore("bayou", guessWord));
  }

  @Test
  public void testScoreForGuessLettersNotInWords()  {
    String guessWord = "bye";

    assertEquals(0, guesser.calculateScore("bayou", guessWord));
  }

  @Test
  public void testScoreForGuessWithRepeatLetters()  {
    String guessWord = "rear";

    assertEquals(0, guesser.calculateScore("relate", guessWord));
  }

  @Test
  public void testScoreForWordWithIncorrectSpelling()  {
    String guessWord = "app";

    when(spellChecker.isSpellingCorrect(guessWord)).thenReturn(false);

    assertEquals(0, guesser.calculateScore("apple", guessWord));
  }


  @Test
  public void testScoreAnotherWordWithIncorrectSpelling()  {
    String guessWord = "ael";

    when(spellChecker.isSpellingCorrect(guessWord)).thenReturn(false);

    assertEquals(0, guesser.calculateScore("apple", guessWord));
  }

  @Test
  public void testScoreForWordWithCorrectSpellingThrowsRuntimeExceptionDueToNetworkError()  {
    String guessWord = "ale";

    when(spellChecker.isSpellingCorrect(guessWord)).thenThrow(new RuntimeException("Network Error"));

    var exception = assertThrows(RuntimeException.class, () -> guesser.calculateScore("apple", guessWord));

    assertEquals("Network Error", exception.getMessage());
  }

  @Test
  public void testPickARandomWordFromAListOfWords() {
    List<String> listOfWords = Arrays.asList("monkey", "fruit", "banana", "apple", "cosmopolitan");

    assertTrue(listOfWords.contains(guesser.pickARandomWord(listOfWords)));
  }

  @Test
  public void testSecondPickReturnsADifferentWord() {
    List<String> listOfWords = Arrays.asList("monkey", "fruit", "banana", "apple", "cosmopolitan");

    String firstRandomWordPicker = guesser.pickARandomWord(listOfWords);
    String secondRandomWordPicker = guesser.pickARandomWord(listOfWords);

    if(firstRandomWordPicker.equals(secondRandomWordPicker)) {
      secondRandomWordPicker = guesser.pickARandomWord(listOfWords);
    }

    assertNotEquals(firstRandomWordPicker, secondRandomWordPicker);
  }

  @Test
  public void testPickARandomWordWhenListIsEmpty() {
    List<String> listOfWords = new ArrayList<>();

    assertThrows(IllegalArgumentException.class, () -> guesser.pickARandomWord(listOfWords));
  }
}
