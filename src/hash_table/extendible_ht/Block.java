package hash_table.extendible_ht;

class Block
{
    public Bucket bucket;

    public Block()
    {
        bucket = new Bucket();
    }
}
