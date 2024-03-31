package hash_table.components;

public class Item
{
    public Object key;
    public Object value;

    public Item(Object key, Object value)
    {
        this.key = key;
        this.value = value;
    }

    public boolean isEqualTo(Item item)
    {
        return (item.key == key) && (item.value == value);
    }
}
