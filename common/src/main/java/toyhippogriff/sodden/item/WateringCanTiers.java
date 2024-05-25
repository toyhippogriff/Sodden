package toyhippogriff.sodden.item;

public enum WateringCanTiers implements WateringCanTier
{
    WOOD(1000, 25, 1000, 0, 0.1F, 0.0F, 0.0F, 0.1F),
    STONE(4000, 50, 1000, 1, 0.1F, 0.0F, 0.0F, 0.1F),
    IRON(16000, 100, 1000, 2, 0.1F, 0.0F, 0.0F, 0.1F),
    GOLD(4000, 50, 1000, 1, 0.05F, 0.0F, 0.025F, 0.05F),
    DIAMOND(64000, 250, 1000, 3, 0.1F, 0.0025F, 0.0F, 0.1F),
    NETHERITE(256000, 500, 1000, 4, 0.1F, 0.005F, 0.0F, 0.1F);

    private int capacity;
    private int usage;
    private int fill;
    private int radius;
    private float growthTickChance;
    private float bonemealChance;
    private float flowerChance;
    private float spreadTickChance;

    WateringCanTiers(int capacity, int usage, int fill, int radius, float growthTickChance, float bonemealChance, float flowerChance, float spreadTickChance)
    {
        this.capacity = capacity;
        this.usage = usage;
        this.fill = fill;
        this.radius = radius;
        this.growthTickChance = growthTickChance;
        this.bonemealChance = bonemealChance;
        this.flowerChance = flowerChance;
        this.spreadTickChance = spreadTickChance;
    }

    @Override
    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    @Override
    public int getUsage()
    {
        return usage;
    }

    public void setUsage(int usage)
    {
        this.usage = usage;
    }

    @Override
    public int getFill()
    {
        return fill;
    }

    public void setFill(int fill)
    {
        this.fill = fill;
    }

    @Override
    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    @Override
    public float getGrowthTickChance()
    {
        return growthTickChance;
    }

    public void setGrowthTickChance(float growthTickChance)
    {
        this.growthTickChance = growthTickChance;
    }

    @Override
    public float getBonemealChance()
    {
        return bonemealChance;
    }

    public void setBonemealChance(float bonemealChance)
    {
        this.bonemealChance = bonemealChance;
    }

    @Override
    public float getFlowerChance()
    {
        return flowerChance;
    }

    public void setFlowerChance(float flowerChance)
    {
        this.flowerChance = flowerChance;
    }

    @Override
    public float getSpreadTickChance()
    {
        return spreadTickChance;
    }

    public void setSpreadTickChance(float spreadTickChance)
    {
        this.spreadTickChance = spreadTickChance;
    }
}