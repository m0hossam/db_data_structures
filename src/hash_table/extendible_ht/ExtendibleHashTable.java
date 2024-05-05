package hash_table.extendible_ht;

import utility.DSException;

import java.util.Arrays;

public class ExtendibleHashTable
{
    public final int maxGlobalDepth;
    public int globalDepth;
    private Block[] directory;

    public ExtendibleHashTable()
    {
        maxGlobalDepth = 4;
        globalDepth = 1;
        directory = new Block[1 << globalDepth]; // no. of entries = 2^globalDepth
        for (int i = 0; i < directory.length; i++)
            directory[i] = new Block();
    }

    private void splitAndInsert(Bucket originalBucket, Item newItem)
    {
        // Split bucket into 2 buckets & increase local depth
        Bucket firstBucket = new Bucket(originalBucket.localDepth + 1);
        Bucket secondBucket = new Bucket(originalBucket.localDepth + 1);

        // Redistribute & adjust pointers
        redistributeItems(originalBucket, firstBucket, secondBucket);
        adjustPointers(originalBucket, firstBucket, secondBucket);

        // Try to insert
        try
        {
            insertNewItem(firstBucket, secondBucket,newItem);
        }
        catch (DSException e)
        {
            insertKey(newItem.key); // Try to split and insert again
        }
    }

    private void redistributeItems(Bucket originalBucket, Bucket firstBucket, Bucket secondBucket)
    {
        // Copy items from original bucket
        Item[] originalBucketItems = new Item[originalBucket.numItems];
        System.arraycopy(originalBucket.items, 0, originalBucketItems, 0, originalBucket.numItems);

        // Redistribute items on new buckets based on their new depth-bit
        for (Item item : originalBucketItems)
        {
            if (((item.key >> (maxGlobalDepth - firstBucket.localDepth)) & 1) == 0) // Checking new bit
                firstBucket.insertItem(item);
            else
                secondBucket.insertItem(item);
        }
    }

    private void adjustPointers(Bucket originalBucket, Bucket firstBucket, Bucket secondBucket)
    {
        // Adjust pointers in the directory that pointed to the original bucket
        int indexMask = (1 << originalBucket.localDepth) << (maxGlobalDepth - originalBucket.localDepth); // 2^oldLocalDepth << (maxGlobalDepth - oldLocalDepth)
        int startingIndex = originalBucket.items[0].key & indexMask;
        for (int i = startingIndex; i < directory.length && ((i & indexMask) == startingIndex); i++)
        {
            Block block = directory[i];
            if (block.bucket == originalBucket)
            {
                if (((i >> (globalDepth - firstBucket.localDepth)) & 1) == 0) // Checking new bit // Determine which new bucket to point to according to new bit
                    block.bucket = firstBucket;
                else
                    block.bucket = secondBucket;
            }
        }
    }

    private void insertNewItem(Bucket firstBucket, Bucket secondBucket, Item newItem) throws DSException
    {
        // Insert new item & check for overflow (need another split)
        if (((newItem.key >> (maxGlobalDepth - firstBucket.localDepth)) & 1) == 0) // Checking new bit
        {
            if (firstBucket.isFull())
                throw new DSException("Bucket full, need another split!");
            else
                firstBucket.insertItem(newItem);
        }
        else
        {
            if (secondBucket.isFull())
                throw new DSException("Bucket full, need another split!");
            else
                secondBucket.insertItem(newItem);
        }
    }
    private void copyDirectory(Block[] destDirectory)
    {
        for (int i = 0; i < destDirectory.length; i++)
        {
            destDirectory[i] = new Block();
            destDirectory[i].bucket = directory[i].bucket;
        }
    }

    private void increaseGlobalDepth() throws DSException
    {
        if (globalDepth == maxGlobalDepth)
            throw new DSException("Max global depth reached!");

        globalDepth++;
        Block[] tempDirectory = new Block[directory.length];
        copyDirectory(tempDirectory);
        directory = new Block[1 << globalDepth];
        for (int i = 0; i < directory.length; i++) // assign buckets to the new directory
        {
            directory[i] = new Block();
            int oldIndex = i >> 1;
            directory[i].bucket = tempDirectory[oldIndex].bucket;
        }
    }

    private String intToBinary(int num, int bits)
    {
        char[] str = new char[bits];
        Arrays.fill(str, '0');
        int tmp = num, ptr = str.length - 1;
        while (tmp != 0)
        {
            str[ptr--] = (char) ('0' + (tmp % 2));
            tmp /= 2;
        }
        return new String(str);
    }

    public void insertKey(int key)
    {
        key %= (1 << maxGlobalDepth);
        Item newItem = new Item(key);
        int index = key >> (maxGlobalDepth - globalDepth); // Most significant bits
        Bucket bucket = directory[index].bucket;

        if (!bucket.isFull()) // If bucket is not full
        {
            bucket.insertItem(newItem);
        }
        else if (bucket.localDepth < globalDepth) // If local depth < global depth
        {
            splitAndInsert(bucket, newItem);
        }
        else // If local depth == global depth
        {
            try
            {
                increaseGlobalDepth();
                splitAndInsert(bucket, newItem);
            }
            catch (DSException e)
            {
                System.out.println(e.getMessage()); // Max global depth reached
            }
        }
    }

    public void deleteKey(int key)
    {

    }

    public void print()
    {
        System.out.println(">> MAX GLOBAL DEPTH: " + maxGlobalDepth);
        System.out.println(">> CURRENT GLOBAL DEPTH: " + globalDepth);
        for (int i = 0; i < directory.length; i++)
        {
            Bucket bucket = directory[i].bucket;
            System.out.println("Block: " + intToBinary(i, globalDepth) + " ->");
            System.out.println("\tBucket Local Depth: " + bucket.localDepth);
            for (int j = 0; j < bucket.numItems; j++)
                System.out.println("\t\tKey: " + intToBinary(bucket.items[j].key, maxGlobalDepth) + " - " + bucket.items[j].key);
        }
        System.out.println("##################################");
    }
}
