package toyhippogriff.sodden.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import toyhippogriff.sodden.Sodden;

public class SoddenItems
{
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Sodden.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> WOODEN_WATERING_CAN = REGISTRY.register("wooden_watering_can", () ->
        new WateringCanItem(WateringCanTiers.WOOD));
    public static final RegistrySupplier<Item> STONE_WATERING_CAN = REGISTRY.register("stone_watering_can", () ->
        new WateringCanItem(WateringCanTiers.STONE));
    public static final RegistrySupplier<Item> IRON_WATERING_CAN = REGISTRY.register("iron_watering_can", () ->
        new WateringCanItem(WateringCanTiers.IRON));
    public static final RegistrySupplier<Item> GOLDEN_WATERING_CAN = REGISTRY.register("golden_watering_can", () ->
        new WateringCanItem(WateringCanTiers.GOLD));
    public static final RegistrySupplier<Item> DIAMOND_WATERING_CAN = REGISTRY.register("diamond_watering_can", () ->
        new WateringCanItem(WateringCanTiers.DIAMOND));
    public static final RegistrySupplier<Item> NETHERITE_WATERING_CAN = REGISTRY.register("netherite_watering_can", () ->
        new WateringCanItem(WateringCanTiers.NETHERITE));

    public static final CreativeModeTab CREATIVE_TAB = CreativeTabRegistry.create(new ResourceLocation(Sodden.MOD_ID, "creative_tab"), () ->
        new ItemStack(WOODEN_WATERING_CAN.get()));
}