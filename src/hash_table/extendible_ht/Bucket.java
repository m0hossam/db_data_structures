package hash_table.extendible_ht;

class Bucket
{
    public final int maxItems;
    public int numItems;
    public Item[] items;
    public int localDepth;

    public Bucket()
    {
        maxItems = 2;
        numItems = 0;
        items = new Item[maxItems];
        localDepth = 1;
    }

    public Bucket(int localDepth)
    {
        maxItems = 2;
        numItems = 0;
        items = new Item[maxItems];
        this.localDepth = localDepth;
    }

    public void insertItem(Item item)
    {
        items[numItems++] = item;
    }

    public boolean isFull()
    {
        return numItems == maxItems;
    }
}
