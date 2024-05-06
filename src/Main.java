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

        int[] keys = new int[]{5, 7, 13, 15, 11, 6, 0, 1, 0};
        for (int key : keys)
            ht.insertKey(key);
        ht.print();
        System.out.println("END OF TEST\n");

        ht = new ExtendibleHashTable();
        keys = new int[]{0b0000, 0b0001, 0b0010, 0b0011, 0b0100, 0b0110, 0b1000};
        for (int key : keys)
            ht.insertKey(key);
        ht.print();
        System.out.println("END OF TEST\n");

        ht = new ExtendibleHashTable();
        ht.insertKey(0b0001);
        ht.insertKey(0b1001);
        ht.insertKey(0b1100);
        ht.print();
        ht.insertKey(0b1010);
        ht.print();
        ht.insertKey(0b0000);
        ht.insertKey(0b0111);
        ht.print();
        ht.insertKey(0b1000);
        ht.print();
        System.out.println("END OF TEST\n");
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