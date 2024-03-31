package hash_table;

public interface HashTable
{
    void insert(Object key, Object value);

    Object get(Object key);

    void delete(Object key);
}
