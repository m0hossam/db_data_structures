package hash_table.static_ht;

import java.util.Objects;

class Item
{
    Object key;
    Object value;

    Item(Object key, Object value)
    {
        this.key = key;
        this.value = value;
    }

    boolean isEqualTo(Item item)
    {
        return (Objects.equals(item.key.toString(), key.toString())) && (Objects.equals(item.value.toString(), value.toString()));
    }

    void setItem(Item item)
    {
        this.key = item.key;
        this.value = item.value;
    }
}
