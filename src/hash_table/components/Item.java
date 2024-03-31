package hash_table.components;

import java.util.Objects;

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
        return (Objects.equals(item.key.toString(), key.toString())) && (Objects.equals(item.value.toString(), value.toString()));
    }

    public void setItem(Item item)
    {
        this.key = item.key;
        this.value = item.value;
    }
}
