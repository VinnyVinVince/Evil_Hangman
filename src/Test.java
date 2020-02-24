import java.util.*;

public class Test {

    public static void main(String[] args) {

        List<String> words = new ArrayList<>();
        words.add("shark"); words.add("fives"); words.add("jivez"); words.add("yeet"); words.add("shanp");
        System.out.println("input collection: " + words + "\n");

        HangmanManager potato = new HangmanManager(words, 5,10);
        printStuff(potato);

        System.out.println(potato.record('i'));
        printStuff(potato);

        System.out.println(potato.record('p'));
        printStuff(potato);

        System.out.println(potato.record('a'));
        printStuff(potato);

    }

    public static void printStuff(HangmanManager potato) {

        System.out.println("words set: " + potato.words());
        System.out.println("guesses left: " + potato.guessesLeft());
        System.out.println("guesses made: " + potato.guesses());
        System.out.println("pattern: " + potato.pattern() + "\n");

    }

}