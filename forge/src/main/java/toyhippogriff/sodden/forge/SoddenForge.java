package toyhippogriff.sodden.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import toyhippogriff.sodden.Sodden;

@Mod(Sodden.MOD_ID)
public class SoddenForge
{
    public SoddenForge()
    {
        EventBuses.registerModEventBus(Sodden.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        Sodden.init();
    }
}