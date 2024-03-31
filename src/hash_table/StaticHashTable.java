package hash_table;

import hash_table.components.*;

import java.util.*;

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

    private Item search(Object key)
    {
        int index = hash(key); //find block
        if (blocks[index] == null)
            return null;

        for (Bucket bucket : blocks[index].buckets) //traverse buckets inside block
            for (int i = 0; i < bucket.numItems; i++) //traverse items inside bucket
                if (Objects.equals(bucket.items[i].key.toString(), key.toString()))
                    return bucket.items[i];
        return null;
    }

    private void reorderBuckets (int blockIndex, Stack<Item> itemStack)
    {
        Block block = blocks[blockIndex];

        if (block.buckets.isEmpty())
        {
            if (itemStack.isEmpty())
            {
                blocks[blockIndex] = null;
                return;
            }
            block.buckets.add(new Bucket());
        }

        Bucket currentBucket = block.buckets.getLast();
        while (!itemStack.isEmpty())
        {
            if (currentBucket.isFull())
            {
                currentBucket = new Bucket();
                block.buckets.add(currentBucket);
            }

            currentBucket.insert(itemStack.pop());
        }
    }

    private void deleteAndReorder(int blockIndex, Object key)
    {
        Block block = blocks[blockIndex];
        Stack<Item> itemStack = new Stack<>();

        Iterator<Bucket> bucketRevIt = block.buckets.descendingIterator();
        while (bucketRevIt.hasNext())
        {
            Bucket bucket = bucketRevIt.next();
            for (int i = bucket.numItems - 1; i >= 0; i--)
            {
                Item item = bucket.items[i];

                if (Objects.equals(key.toString(), item.key.toString()))
                {
                    bucket.deleteItem(item);
                    if (bucket.isEmpty())
                        bucketRevIt.remove();

                    reorderBuckets(blockIndex, itemStack);
                    return;
                }

                itemStack.push(item);
                bucket.deleteItem(item);
            }
            bucketRevIt.remove();
        }
    }

    @Override
    public void insert(Object key, Object value)
    {
        int index = hash(key);
        if (blocks[index] == null)
            blocks[index] = new Block();

        Item newItem =  new Item(key, value);
        Item existingItem = search(key);
        if (existingItem != null)
        {
            existingItem.setItem(newItem);
            return;
        }

        LinkedList<Bucket> block = blocks[index].buckets;
        if (block.isEmpty())
            block.add(new Bucket());

        Bucket bucket = block.getLast();
        if (bucket.isFull())
        {
            block.add(new Bucket());
            bucket = block.getLast();
        }

        bucket.insert(newItem);
    }

    @Override
    public Object get(Object key)
    {
        Item item = search(key);
        return (item != null) ? item.value : null;
    }

    @Override
    public void delete(Object key)
    {
        Item item = search(key);
        if (item != null)
            deleteAndReorder(hash(key), key);
    }

    public void print()
    {
        for (int i = 0; i < size; i++)
        {
            if (blocks[i] == null)
                continue;

            System.out.println("Block #" + i);
            blocks[i].buckets.forEach(bucket ->
            {
                System.out.println("\tBucket" );
                for (int j = 0; j < bucket.numItems; j++)
                    System.out.println("\t\t" + bucket.items[j].key + " : " + bucket.items[j].value);
            });
        }
        System.out.println("##################################");
    }
}
