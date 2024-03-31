package hash_table;

import hash_table.components.*;

import java.util.LinkedList;

import static java.lang.Math.pow;

public class StaticHashTable implements HashTable
{
    public final int size;
    private final Block[] blocks;
    private final int hashingPrime;

    public StaticHashTable(int size)
    {
        this.size = size;
        blocks = new Block[size];
        hashingPrime = 151;
    }

    private int hash(Object key)
    {
        String keyString = key.toString();
        long index = 0;
        for (int i = 0; i < keyString.length(); i++)
        {
            index += (long)pow(hashingPrime, keyString.length() - (i + 1)) * keyString.charAt(i);
            index %= size; //ensure the index is in [0, size[
        }
        return (int)index;
    }

    @Override
    public void insert(Object key, Object value)
    {
        int index = hash(key);
        Item item =  new Item(key, value);

        if (blocks[index] == null)
            blocks[index] = new Block();
        LinkedList<Bucket> block = blocks[index].buckets;

        for (Bucket bucket : block) //traverse buckets inside block
            for (int i = 0; i < bucket.numItems; i++) //traverse items inside bucket
                if (bucket.items[i].key == key)
                {
                    bucket.items[i] = item;
                    return;
                }

        if (block.isEmpty())
            block.add(new Bucket());
        Bucket bucket = block.getLast();
        if (bucket.isFull())
        {
            block.add(new Bucket());
            bucket = block.getLast();
        }
        bucket.insert(item);
    }

    @Override
    public Object search(Object key)
    {
        int index = hash(key); //find block
        for (Bucket bucket : blocks[index].buckets) //traverse buckets inside block
            for (int i = 0; i < bucket.numItems; i++) //traverse items inside bucket
                if (bucket.items[i].key == key)
                    return bucket.items[i].value;
        return null;
    }

    @Override
    public void delete(Object key)
    {
        //TODO: Implement this
    }
}
