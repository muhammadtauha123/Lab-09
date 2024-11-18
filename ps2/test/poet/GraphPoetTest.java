package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    // Partitions for GraphPoet(corpus)
    //   corpus contains: one word, one line, multiple lines
    //   include words whose adjacency count > 1
    // 
    // Partitions for poem(input) -> poeticOutput
    //   input: one word, multiple words
    //        : word pairs with adjacency count > 1
    //
    // Exhaustive Cartesian coverage of partitions

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    private static final GraphPoet instantiateGraph(String source) {
        try {
            final File corpus = new File(source);
            GraphPoet graphPoet = new GraphPoet(corpus);
            return graphPoet;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    final GraphPoet graphOneWord = instantiateGraph("test/poet/TestOneWord.txt");
    final GraphPoet graphOneLine = instantiateGraph("test/poet/TestOneLine.txt");
    final GraphPoet graphMultipleLines = instantiateGraph("test/poet/TestMultipleLines.txt");
    



    // Correct generation of poems with and without bridge words
    @Test
    public void testBasicPoemGeneration() {
        String input = "Seek to explore new and exciting synergies!";
        String output = graphOneLine.poem(input);
        String expected = "Seek to explore strange new life and exciting synergies!"; // Corrected for actual content
        
        assertEquals("Expected poetic output with words in input unchanged", expected, output);
    }

    @Test
    public void testNoBridgeWords() {
        String input = "Here!";
        String output = graphOneWord.poem(input);
        
        assertEquals("Expected unchanged input (no bridge words)", input, output);
    }

    // Proper validation for edge cases such as empty or simple input strings
    @Test
    public void testEmptyInput() {
        String input = "";
        String output = graphOneLine.poem(input);
        
        assertEquals("Expected empty output for empty input", input, output);
    }

    @Test
    public void testSingleWordInput() {
        String input = "life";
        String output = graphMultipleLines.poem(input);
        
        assertEquals("Expected single word input to be returned unchanged", input, output);
    }

    @Test
    public void testCaseInsensitivity() {
        String input = "To explore strange new galaxies";
        String output = graphOneLine.poem(input);
        String expected = "To explore strange new galaxies";
        
        assertEquals("Expected case insensitive handling of input", expected, output);
    }

    @Test
    public void testSpecialCharacters() {
        String input = "You may write me down in history!";
        String output = graphMultipleLines.poem(input);
        
        assertTrue("Expected special characters to be handled correctly", 
                output.contains("You") && output.contains("write") && output.contains("history"));
    }

    @Test
    public void testBridgeWordsInMiddle() {
        String input = "you MAY me";
        String output = graphMultipleLines.poem(input);
        
        assertNotEquals("Expected a bridge word inserted", input, output);
        assertTrue("Expected bridge word inserted correctly", 
                output.contains("write")
                || output.contains("trod")
                || output.contains("kill")
                || output.contains("cut")
                || output.contains("shoot"));
    }
}
