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

    public void insertKey(int key)
    {
        Item newItem = new Item(key);
        int index = key & ((1 << globalDepth) - 1); // mask = 2^globalDepth - 1
        Bucket bucket = directory[index].bucket;

        if (!bucket.isFull())
        {
            bucket.insertItem(newItem);
            return;
        }

        if (bucket.localDepth < globalDepth)
        {
            // Split bucket into 2 buckets & increase local depth
            int oldLocalDepth = bucket.localDepth;
            Bucket firstBucket = new Bucket(oldLocalDepth + 1);
            Bucket secondBucket = new Bucket(oldLocalDepth + 1);

            // Copy & delete items from full bucket
            Item[] itemsToBeRedistributed = new Item[bucket.numItems + 1];
            if (bucket.numItems >= 0)
                System.arraycopy(bucket.items, 0, itemsToBeRedistributed, 0, bucket.numItems);
            itemsToBeRedistributed[bucket.numItems] = newItem;

            // Redistribute items based on their new local depth bit to both old & new buckets
            int mask = 1 << (globalDepth - (oldLocalDepth + 1)); //
            for (Item item : itemsToBeRedistributed)
            {
                if ((item.key & mask) == 0) // TODO: Overflow case: Split new full bucket into 2 buckets with higher local depth
                    firstBucket.insertItem(item);
                else
                    secondBucket.insertItem(item);
            }

            // Adjust pointers in the directory that pointed to the old bucket
            for (int i = 0; i < directory.length; i++)
            {
                Block block = directory[i];
                if (block.bucket == bucket)
                {
                    if ((i & mask) == 0)
                        block.bucket = firstBucket;
                    else
                        block.bucket = secondBucket;
                }
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
