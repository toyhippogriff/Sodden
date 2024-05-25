package toyhippogriff.sodden;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toyhippogriff.sodden.config.SoddenConfig;
import toyhippogriff.sodden.data.WateringCanTiersLoader;
import toyhippogriff.sodden.item.SoddenItems;

public class Sodden
{
    public static final String MOD_ID = "sodden";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);
    public static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();
    public static SoddenConfig CONFIG = new SoddenConfig();

    public static void init()
    {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new WateringCanTiersLoader());

        SoddenItems.REGISTRY.register();
    }
}