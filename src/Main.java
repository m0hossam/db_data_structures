import hash_table.*;
public class Main {
    public static void main(String[] args)
    {
        StaticHashTable ht = new StaticHashTable(53);
        ht.insert("Hakuna", "Matata");
        ht.insert("Foo", "Bar");
        System.out.println("Hakuna: " + ht.search("Hakuna"));
        System.out.println("Foo: " + ht.search("Foo"));
        ht.delete("Hakuna");
        System.out.println("Hakuna: " + ht.search("Hakuna"));
        System.out.println("Foo: " + ht.search("Foo"));
    }
}