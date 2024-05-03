package hash_table.extendible_ht;

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

    private Bucket[] splitBucket(Bucket originalBucket)
    {
        // Split bucket into 2 buckets & increase local depth
        Bucket firstBucket = new Bucket(originalBucket.localDepth + 1);
        Bucket secondBucket = new Bucket(originalBucket.localDepth + 1);
        return new Bucket[]{firstBucket, secondBucket};
    }

    private void redistributeItems(Bucket originalBucket, Bucket firstBucket, Bucket secondBucket, Item newItem)
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

        // TODO: Insert new item & check for overflow (another split)
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

    public void insertKey(int key)
    {
        Item newItem = new Item(key);
        int index = key & ((1 << globalDepth) - 1); // mask = 2^globalDepth - 1
        Bucket bucket = directory[index].bucket;

        // if bucket is not full
        if (!bucket.isFull())
        {
            bucket.insertItem(newItem);
            return;
        }

        // j < i
        if (bucket.localDepth < globalDepth)
        {
            Bucket[] newBuckets = splitBucket(bucket);
            Bucket firstBucket = newBuckets[0], secondBucket = newBuckets[1];
            redistributeItems(bucket, firstBucket, secondBucket, newItem);
            adjustPointers(bucket, firstBucket, secondBucket);
        }

        // j == i
        Block[] tempDirectory = directory.clone();
        globalDepth++; // TODO: Limit to max global depth
        directory = new Block[1 << globalDepth];
        for (int i = 0; i < directory.length; i++) // assign buckets to the new directory
        {
            int oldIndex = i >> 1;
            directory[i] = tempDirectory[oldIndex];
        }
        // TODO: perform same as (j < i) case
    }

    public void deleteKey(int key)
    {

    }

    public void print()
    {

    }
}
