import hash_table.static_ht.StaticHashTable;
import hash_table.extendible_ht.ExtendibleHashTable;

import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static Scanner scanner;
    public static void main(String[] args)
    {
        scanner = new Scanner(System.in);

        scanner.close();
    }

    public static void testExtendibleHashTable()
    {
        ExtendibleHashTable ht = new ExtendibleHashTable();
        ht.insertKey(5);
        ht.insertKey(7);
        ht.insertKey(13);
        ht.insertKey(15);
        ht.insertKey(11);
        ht.insertKey(6);
        ht.insertKey(0);
        ht.insertKey(1);
        ht.insertKey(0);
        ht.print();
    }

    public static void testStaticHashTable()
    {
        StaticHashTable ht = new StaticHashTable(53);

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