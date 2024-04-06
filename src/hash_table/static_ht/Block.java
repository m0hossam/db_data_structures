package hash_table.static_ht;

class Block
{
    final int maxItems = 2;
    final Item[] items = new Item[maxItems];
    int numItems = 0;

    boolean isFull()
    {
        return numItems == maxItems;
    }

    boolean isEmpty()
    {
        return numItems == 0;
    }

    void insert(Item item)

    {
        items[numItems] = new Item(item.key, item.value);
        numItems++;
    }

    void deleteItem(Item item)
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
