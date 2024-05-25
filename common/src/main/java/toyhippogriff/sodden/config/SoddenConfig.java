package toyhippogriff.sodden.config;

import com.google.gson.annotations.SerializedName;
import dev.architectury.platform.Platform;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import toyhippogriff.sodden.Sodden;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SoddenConfig
{
    public static final File FILE = new File(Platform.getConfigFolder().toFile(), "sodden.json");

    @SerializedName("consumes_water")
    public boolean consumesWater = true;

    @SerializedName("cooldown")
    public int cooldown = 5;

    @SerializedName("fake_players_allowed")
    public boolean fakePlayersAllowed = true;

    @SerializedName("use_growth_tick_compatibility_check")
    public boolean useGrowthTickCompatibilityCheck = true;

    @SerializedName("use_bonemeal_compatibility_check")
    public boolean useBonemealCompatibilityCheck = true;

    @SerializedName("use_spread_tick_compatibility_check")
    public boolean useSpreadTickCompatibilityCheck = true;

    public static void read()
    {
        try(FileReader reader = new FileReader(FILE))
        {
            Sodden.CONFIG = Sodden.GSON.fromJson(reader, SoddenConfig.class);
        }
        catch(IOException exception)
        {
            Sodden.LOG.info("Failed to read config at {}", FILE.getPath());
        }
    }

    public static void write()
    {
        try(FileWriter writer = new FileWriter(FILE))
        {
            Sodden.GSON.toJson(Sodden.CONFIG, writer);
        }
        catch(IOException exception)
        {
            Sodden.LOG.info("Failed to write config to {}", FILE.getPath());
        }
    }

    @Environment(EnvType.CLIENT)
    public static Screen getConfigScreen(Screen parent)
    {
        ConfigBuilder builder = ConfigBuilder.create()
            .setTitle(new TranslatableComponent("config.%s".formatted(Sodden.MOD_ID)))
            .setParentScreen(parent)
            .setSavingRunnable(() ->
            {
                SoddenConfig.write();
                SoddenConfig.read();
            });
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableComponent("config.%s.general".formatted(Sodden.MOD_ID)));

        general.addEntry(entryBuilder
            .startBooleanToggle(new TranslatableComponent("config.%s.general.consumes_water".formatted(Sodden.MOD_ID)), Sodden.CONFIG.consumesWater)
            .setTooltip(new TranslatableComponent("config.%s.general.consumes_water.tooltip".formatted(Sodden.MOD_ID)))
            .setDefaultValue(true)
            .setSaveConsumer(value -> Sodden.CONFIG.consumesWater = value)
            .build());
        general.addEntry(entryBuilder
            .startIntSlider(new TranslatableComponent("config.%s.general.cooldown".formatted(Sodden.MOD_ID)), Sodden.CONFIG.cooldown, 1, 20)
            .setTooltip(new TranslatableComponent("config.%s.general.cooldown.tooltip".formatted(Sodden.MOD_ID)))
            .setDefaultValue(5)
            .setSaveConsumer(value -> Sodden.CONFIG.cooldown = value)
            .build());
        general.addEntry(entryBuilder
            .startBooleanToggle(new TranslatableComponent("config.%s.general.fake_players_allowed".formatted(Sodden.MOD_ID)), Sodden.CONFIG.fakePlayersAllowed)
            .setTooltip(new TranslatableComponent("config.%s.general.fake_players_allowed.tooltip".formatted(Sodden.MOD_ID)))
            .setDefaultValue(true)
            .setSaveConsumer(value -> Sodden.CONFIG.fakePlayersAllowed = value)
            .build());
        general.addEntry(entryBuilder
            .startBooleanToggle(new TranslatableComponent("config.%s.general.use_growth_tick_compatibility_check".formatted(Sodden.MOD_ID)), Sodden.CONFIG.useGrowthTickCompatibilityCheck)
            .setTooltip(new TranslatableComponent("config.%s.general.use_growth_tick_compatibility_check.tooltip.1".formatted(Sodden.MOD_ID)), new TranslatableComponent("config.%s.general.use_growth_tick_compatibility_check.tooltip.2".formatted(Sodden.MOD_ID)))
            .setDefaultValue(true)
            .setSaveConsumer(value -> Sodden.CONFIG.useGrowthTickCompatibilityCheck = value)
            .build());
        general.addEntry(entryBuilder
            .startBooleanToggle(new TranslatableComponent("config.%s.general.use_bonemeal_compatibility_check".formatted(Sodden.MOD_ID)), Sodden.CONFIG.useBonemealCompatibilityCheck)
            .setTooltip(new TranslatableComponent("config.%s.general.use_bonemeal_compatibility_check.tooltip.1".formatted(Sodden.MOD_ID)), new TranslatableComponent("config.%s.general.use_bonemeal_compatibility_check.tooltip.2".formatted(Sodden.MOD_ID)))
            .setDefaultValue(true)
            .setSaveConsumer(value -> Sodden.CONFIG.useBonemealCompatibilityCheck = value)
            .build());
        general.addEntry(entryBuilder
            .startBooleanToggle(new TranslatableComponent("config.%s.general.use_spread_tick_compatibility_check".formatted(Sodden.MOD_ID)), Sodden.CONFIG.useSpreadTickCompatibilityCheck)
            .setTooltip(new TranslatableComponent("config.%s.general.use_spread_tick_compatibility_check.tooltip.1".formatted(Sodden.MOD_ID)), new TranslatableComponent("config.%s.general.use_spread_tick_compatibility_check.tooltip.2".formatted(Sodden.MOD_ID)))
            .setDefaultValue(true)
            .setSaveConsumer(value -> Sodden.CONFIG.useSpreadTickCompatibilityCheck = value)
            .build());

        return builder.build();
    }
}