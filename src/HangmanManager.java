/**
 * @author Vincent Do
 * @version 1.5 (1/5/2020)
 *
 * internal manager for 'Evil Hangman' game program, assumes lowercase input for word dictionary and letter guesses
 */

import java.util.*;

public class HangmanManager
{
    private Set<String> currentWords;
    private Set<Character> guessedLetters;
    private int wrongGuessesLeft;
    private char[] currentPattern;

    /**
     * precondition:    @param dictionary is non-empty, @param length is non-zero, @param max is non-negative
     *
     * postcondition:   HangmanManager object constructed with this.currentWords containing elements of @param
     *                  dictionary with length equal to @param length, this.currentPattern auto-initialized
     *                  with 0 (ASCII null) values, this.wrongGuessesLeft set to @param max
     *
     * @throws IllegalArgumentException if @param length is less than 1 or if @param max is less than zero
     */
    public HangmanManager(Collection<String> dictionary, int length, int max)
    {
        if (length < 1)
            throw new IllegalArgumentException("length is less than 1");
        else if (max < 0)
            throw new IllegalArgumentException("max is less than 0");

        this.currentWords = new TreeSet<>();
        for (String word : dictionary)
            if (word.length() == length)
                this.currentWords.add(word);

        this.guessedLetters = new TreeSet<>();
        this.wrongGuessesLeft = max;
        this.currentPattern = new char[length];
    }

    /**
     * precondition:    HangmanManager object constructed
     *
     * postcondition:   @returns reference to collection of current words
     */
    public Set<String> words()
    {
        return this.currentWords;
    }

    /**
     * precondition:    HangmanManager object constructed
     *
     * postcondition:   @returns amount of incorrect guesses left
     */
    public int guessesLeft()
    {
        return this.wrongGuessesLeft;
    }

    /**
     * precondition:    HangmanManager object constructed
     *
     * postcondition:   @returns reference to collection of guessed letters
     */
    public Set<Character> guesses()
    {
        return this.guessedLetters;
    }

    /**
     * precondition:    HangmanManager object constructed, field collection of words is non-empty
     *
     * postcondition:   @returns String representation of currently chosen pattern
     *
     * @throws IllegalStateException if current collection of words is empty
     */
    public String pattern()
    {
        if (this.currentWords.isEmpty())
            throw new IllegalStateException("words is empty");

        String pattern = "";

        for (char letter : this.currentPattern)
            if (letter != 0)                        // ASCII value of 0 is null
                pattern += letter;
            else
                pattern += "-";

        return pattern;
    }

    /**
     * precondition:    HangmanManager object constructed, field collection of words is non-empty, amount of incorrect
     *                  guesses remaining is non-zero, @param guess is not contained in collection of guessed letters
     *
     * postcondition:   @param guess is added to collection of guessed letters, words not in the predominant pattern
     *                  are removed from current collection of words, amount of incorrect guesses left decremented if
     *                  @param guess is not in predominant pattern, field char array is updated if @param guess is
     *                  in predominant pattern, @returns number of occurrences of @param guess in predominate pattern
     *
     * @throws IllegalStateException if collection of current words is empty or if amount of incorrect guesses is zero
     * @throws IllegalArgumentException if @param guess is contained in collection of already guessed letters
     */
    public int record(char guess)
    {
        record_exceptions(guess);
        this.guessedLetters.add(guess);
        Map<String, Set<String>> wordPatterns = new TreeMap<>();

        for (String word : this.currentWords)
        {
            String pattern = "";

            for (int i = 0; i < word.length(); i++)
                if (word.charAt(i) != guess)
                    pattern += "-";
                else
                    pattern += guess;

            if (!wordPatterns.containsKey(pattern))
                wordPatterns.put(pattern, new TreeSet<>());

            wordPatterns.get(pattern).add(word);
        }

        String dominantPattern = record_findDominant(wordPatterns);
        this.currentWords.retainAll(wordPatterns.get(dominantPattern));

        if (dominantPattern.indexOf(guess) < 0)
        {
            this.wrongGuessesLeft--;
            return 0;
        }
        else
            return record_findOccurrences(guess, dominantPattern);
    }

    /**
     * helper method for record method
     * throws exceptions, check record method documentation for specifications
     */
    private void record_exceptions(char guess)
    {
        if (this.currentWords.isEmpty())
            throw new IllegalStateException("words is empty");
        else if (this.wrongGuessesLeft < 1)
            throw new IllegalStateException("0 guesses left");
        else if (this.guessedLetters.contains(guess))
            throw new IllegalArgumentException("letter already guessed");
    }

    /**
     * helper method for record method
     * @returns String representation of predominant pattern from a map of patterns and their corresponding words
     */
    private String record_findDominant(Map<String, Set<String>> wordPatterns)
    {
        int largest = 0;
        String dominantPattern = null;

        for (String pattern : wordPatterns.keySet())
        {
            int patternSize = wordPatterns.get(pattern).size();

            if (patternSize > largest)
            {
                largest = patternSize;
                dominantPattern = pattern;
            }
        }

        return dominantPattern;
    }

    /**
     * helper method for record method
     * counts the occurrences of guessed letter in the predominant pattern
     * !will modify current pattern field char array!
     * @returns number of occurrences that were counted
     */
    private int record_findOccurrences(char guess, String pattern)
    {
        int count = 0;

        for (int i = 0; i < pattern.length(); i++)
            if (pattern.charAt(i) == guess)
            {
                count++;
                this.currentPattern[i] = guess;                     // PATTERN MODIFIER
            }

        return count;
    }
}