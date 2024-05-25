package toyhippogriff.sodden.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import toyhippogriff.sodden.Sodden;

public class SoddenTags
{
    public static final TagKey<Block> WATERING_CAN_GROWABLE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Sodden.MOD_ID, "watering_can_growable"));
    public static final TagKey<Block> WATERING_CAN_BONEMEALABLE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Sodden.MOD_ID, "watering_can_bonemealable"));
    public static final TagKey<Block> WATERING_CAN_SPREADABLE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Sodden.MOD_ID, "watering_can_spreadable"));
}