package toyhippogriff.sodden.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import toyhippogriff.sodden.Sodden;
import toyhippogriff.sodden.item.WateringCanTiers;

import java.util.Map;

public class WateringCanTiersLoader extends SimpleJsonResourceReloadListener
{
    public WateringCanTiersLoader()
    {
        super(Sodden.GSON, "watering_cans");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager manager, ProfilerFiller filler)
    {
        jsonElements.forEach((resourceLocation, jsonElement) ->
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if(!jsonObject.has("tier") || !jsonObject.has("capacity") || !jsonObject.has("usage") || !jsonObject.has("fill") || !jsonObject.has("radius") || !jsonObject.has("growth_tick_chance") || !jsonObject.has("bonemeal_chance") || !jsonObject.has("flower_chance") || !jsonObject.has("spread_tick_chance"))
            {
                Sodden.LOG.info("Invalid watering can tier found at {}", resourceLocation.toString());
            }

            WateringCanTiers tier = WateringCanTiers.valueOf(jsonObject.get("tier").getAsString().toUpperCase());

            tier.setCapacity(jsonObject.get("capacity").getAsInt());
            tier.setUsage(jsonObject.get("usage").getAsInt());
            tier.setFill(jsonObject.get("fill").getAsInt());
            tier.setRadius(jsonObject.get("radius").getAsInt());
            tier.setGrowthTickChance(jsonObject.get("growth_tick_chance").getAsFloat());
            tier.setBonemealChance(jsonObject.get("bonemeal_chance").getAsFloat());
            tier.setFlowerChance(jsonObject.get("flower_chance").getAsFloat());
            tier.setSpreadTickChance(jsonObject.get("spread_tick_chance").getAsFloat());
        });

        Sodden.LOG.info("Loaded {} watering can tiers", jsonElements.size());
    }
}