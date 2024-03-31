import hash_table.StaticHashTable;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        StaticHashTable ht = new StaticHashTable(53);

        Scanner scanner = new Scanner(System.in);

        String alphabet = "abcdefghijklmnopqrtsuvwxyz";
        for (int i = 0; i < 53; i++)
            ht.insert(generateString(new Random(), alphabet, 5), generateString(new Random(), alphabet, 5));
        ht.print();

        String k = scanner.nextLine();
        ht.delete(k);
        ht.print();
    }

    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}