package hash_table.static_ht;

import hash_table.HashTable;

import java.util.*;

import static java.lang.Math.pow;

public class StaticHashTable implements HashTable
{
    public final int size;
    private final Bucket[] buckets;
    private final int hashingPrime;

    public StaticHashTable(int size)
    {
        this.size = size;
        buckets = new Bucket[size];
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
        int index = hash(key); //find bucket
        if (buckets[index] == null)
            return null;

        for (Block block : buckets[index].blocks) //traverse blocks inside bucket
            for (int i = 0; i < block.numItems; i++) //traverse items inside block
                if (Objects.equals(block.items[i].key.toString(), key.toString()))
                    return block.items[i];
        return null;
    }

    private void reorderBlocks(int bucketIndex, Stack<Item> itemStack)
    {
        Bucket bucket = buckets[bucketIndex];

        if (bucket.blocks.isEmpty())
        {
            if (itemStack.isEmpty())
            {
                buckets[bucketIndex] = null;
                return;
            }
            bucket.blocks.add(new Block());
        }

        Block currentBlock = bucket.blocks.getLast();
        while (!itemStack.isEmpty())
        {
            if (currentBlock.isFull())
            {
                currentBlock = new Block();
                bucket.blocks.add(currentBlock);
            }

            currentBlock.insert(itemStack.pop());
        }
    }

    private void deleteAndReorder(int bucketIndex, Object key)
    {
        Bucket bucket = buckets[bucketIndex];
        Stack<Item> itemStack = new Stack<>();

        Iterator<Block> blocksReverseIter = bucket.blocks.descendingIterator();
        while (blocksReverseIter.hasNext())
        {
            Block block = blocksReverseIter.next();
            for (int i = block.numItems - 1; i >= 0; i--)
            {
                Item item = block.items[i];

                if (Objects.equals(key.toString(), item.key.toString()))
                {
                    block.deleteItem(item);
                    if (block.isEmpty())
                        blocksReverseIter.remove();

                    reorderBlocks(bucketIndex, itemStack);
                    return;
                }

                itemStack.push(item);
                block.deleteItem(item);
            }
            blocksReverseIter.remove();
        }
    }

    @Override
    public void insert(Object key, Object value)
    {
        int index = hash(key);
        if (buckets[index] == null)
            buckets[index] = new Bucket();

        Item newItem =  new Item(key, value);
        Item existingItem = search(key);
        if (existingItem != null)
        {
            existingItem.setItem(newItem);
            return;
        }

        LinkedList<Block> bucket = buckets[index].blocks;
        if (bucket.isEmpty())
            bucket.add(new Block());

        Block block = bucket.getLast();
        if (block.isFull())
        {
            bucket.add(new Block());
            block = bucket.getLast();
        }

        block.insert(newItem);
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
            if (buckets[i] == null)
                continue;

            System.out.println("Bucket #" + i);
            buckets[i].blocks.forEach(block ->
            {
                System.out.println("\tBlock" );
                for (int j = 0; j < block.numItems; j++)
                    System.out.println("\t\t" + block.items[j].key + " : " + block.items[j].value);
            });
        }
        System.out.println("##################################");
    }
}
