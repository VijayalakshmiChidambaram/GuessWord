package game;

import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpellCheckerServiceTest {
  SpellCheckerService spellCheckerServiceSpy;

  @BeforeEach
  public void init() {
    spellCheckerServiceSpy = spy(SpellCheckerService.class);
  }

  @Test
  public void testGetResponseFromServiceURLReturnsText() throws Exception {
    assertFalse(spellCheckerServiceSpy.getResponse("monk").isEmpty());
  }

  @Test
  public void testParseTrueReturnsTrue() {
    assertTrue(spellCheckerServiceSpy.parseText("true"));
  }

  @Test
  public void testParseFalseReturnsFalse() {
    assertFalse(spellCheckerServiceSpy.parseText("false"));
  }

  @Test
  public void testParseSomethingElse() {
    var exception = assertThrows(IllegalArgumentException.class, () -> spellCheckerServiceSpy.parseText("something else"));

    assertEquals("response not boolean, got: something else", exception.getMessage());
  }

  @Test
  public void testisSpellingCorrectTakesAWordAndReturnsTrueForWordRight() throws IOException, InterruptedException {
    doReturn("true").when(spellCheckerServiceSpy).getResponse("right");

    assertTrue(spellCheckerServiceSpy.isSpellingCorrect("right"));
  }

  @Test
  public void testisSpellingCorrectTakesAWordAndReturnsFalseForWordRigth() throws IOException, InterruptedException {
    doReturn("false").when(spellCheckerServiceSpy).getResponse("rigth");

    assertFalse(spellCheckerServiceSpy.isSpellingCorrect("rigth"));
  }

  @Test
  public void testisSpellingCorrectTakesAWordAndReturnsFalseForWordHaha() throws IOException, InterruptedException {
    doReturn("false").when(spellCheckerServiceSpy).getResponse("haha");

    assertFalse(spellCheckerServiceSpy.isSpellingCorrect("haha"));
  }

  @Test
  public void testisSpellingCorrectHandlesExceptionFromGetResponseProperly() throws IOException {
    when(spellCheckerServiceSpy.getResponse("monk"))
        .thenThrow(new IOException("Network error"));

    var exception = assertThrows(RuntimeException.class, () -> spellCheckerServiceSpy.isSpellingCorrect("monk"));

    assertEquals("java.io.IOException: Network error", exception.getMessage());
  }

  @Test
  public void integrationTestForIsSpellingCorrectTakesWordCorrectReturnsTrue() {
    assertTrue(spellCheckerServiceSpy.isSpellingCorrect("correct"));
  }
}
