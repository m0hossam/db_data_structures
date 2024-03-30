package hash_table;

import static java.lang.Math.pow;

public class StaticHashTable implements HashTable
{
    private final Object[] hashtableArray;
    public final int size;
    private final int hashingPrime;

    public StaticHashTable(int size)
    {
        hashtableArray = new Object[size];
        this.size = size;
        hashingPrime = 151;
    }

    private int hash(Object key)
    {
        String keyString = key.toString();
        long index = 0;
        for (int i = 0; i < keyString.length(); i++)
        {
            index += (long)pow(hashingPrime, keyString.length() - (i + 1)) * keyString.charAt(i);
            index %= size; //ensure the index is in [0, size[
        }
        return (int)index;
    }

    @Override
    public void insert(Object key, Object value)
    {
        int index = hash(key);
        hashtableArray[index] = value;
    }

    @Override
    public Object search(Object key)
    {
        int index = hash(key);
        if (hashtableArray[index] != null)
            return hashtableArray[index];
        return null;
    }

    @Override
    public void delete(Object key)
    {
        int index = hash(key);
        hashtableArray[index] = null;
    }
}
