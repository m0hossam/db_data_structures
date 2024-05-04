package hash_table.extendible_ht;

import utility.DSException;

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
        for (Block block : directory)
            block = new Block();
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
        catch (DSException needAnotherSplitException)
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
        int newBitMask = (1 << globalDepth) >> firstBucket.localDepth; // 2^globalDepth >> newLocalDepth
        for (Item item : originalBucketItems)
        {
            if ((item.key & newBitMask) == 0)
                firstBucket.insertItem(item);
            else
                secondBucket.insertItem(item);
        }
    }

    private void adjustPointers(Bucket originalBucket, Bucket firstBucket, Bucket secondBucket)
    {
        // Adjust pointers in the directory that pointed to the original bucket
        int newBitMask = (1 << globalDepth) >> firstBucket.localDepth; // 2^globalDepth >> newLocalDepth
        int indexMask = (1 << originalBucket.localDepth) << (maxGlobalDepth - originalBucket.localDepth); // 2^oldLocalDepth << (maxGlobalDepth - oldLocalDepth)
        int startingIndex = originalBucket.items[0].key & indexMask;
        for (int i = startingIndex; i < directory.length && ((i & indexMask) == startingIndex); i++)
        {
            Block block = directory[i];
            if (block.bucket == originalBucket)
            {
                if ((i & newBitMask) == 0) // Determine which new bucket to point to according to new bit
                    block.bucket = firstBucket;
                else
                    block.bucket = secondBucket;
            }
        }
    }

    private void insertNewItem(Bucket firstBucket, Bucket secondBucket, Item newItem) throws DSException
    {
        // Insert new item & check for overflow (need another split)
        int newBitMask = (1 << globalDepth) >> firstBucket.localDepth; // 2^globalDepth >> newLocalDepth
        if ((newItem.key & newBitMask) == 0)
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

    private void increaseGlobalDepth() throws DSException
    {
        if (globalDepth == maxGlobalDepth)
            throw new DSException("Max global depth reached!");

        globalDepth++;
        Block[] tempDirectory = directory.clone();
        directory = new Block[1 << globalDepth];
        for (int i = 0; i < directory.length; i++) // assign buckets to the new directory
        {
            int oldIndex = i >> 1;
            directory[i] = tempDirectory[oldIndex];
        }
    }

    public void insertKey(int key)
    {
        Item newItem = new Item(key);
        int index = key & ((1 << globalDepth) - 1); // mask = 2^globalDepth - 1
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

    }
}
