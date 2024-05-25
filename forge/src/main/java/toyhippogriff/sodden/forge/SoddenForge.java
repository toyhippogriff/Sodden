package toyhippogriff.sodden.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import toyhippogriff.sodden.Sodden;
import toyhippogriff.sodden.config.SoddenConfig;

@Mod(Sodden.MOD_ID)
public class SoddenForge
{
    public SoddenForge()
    {
        EventBuses.registerModEventBus(Sodden.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        Sodden.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
            new ConfigGuiHandler.ConfigGuiFactory((client, screen) -> SoddenConfig.getConfigScreen(screen)));
    }
}