package hash_table.components;

public class Bucket
{
    public final int maxItems = 2;
    public final Item[] items = new Item[maxItems];
    public int numItems = 0;

    public boolean isFull()
    {
        return numItems == maxItems;
    }

    public boolean isEmpty()
    {
        return numItems == 0;
    }

    public void insert(Item item)

    {
        items[numItems] = new Item(item.key, item.value);
        numItems++;
    }

    public void deleteItem(Item item)
    {
        for (int i = 0; i < numItems; i++)
        {
            if (item.isEqualTo(items[i]))
            {
                items[i] = null;
                numItems--;
            }
        }
    }
}
