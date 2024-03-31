import hash_table.StaticHashTable;

public class Main {
    public static void main(String[] args)
    {
        StaticHashTable ht = new StaticHashTable(53);

        ht.insert("Hakuna", "Matata");
        ht.insert("Foo", "Bar");

        System.out.println("Hakuna: " + ht.search("Hakuna"));
        System.out.println("Foo: " + ht.search("Foo"));

        ht.insert("Hakuna", "Banana");
        System.out.println("Hakuna: " + ht.search("Hakuna"));

        /*
        ht.delete("Hakuna");
        System.out.println("Hakuna: " + ht.search("Hakuna"));
        System.out.println("Foo: " + ht.search("Foo"));
         */
    }
}