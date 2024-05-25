package toyhippogriff.sodden.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import toyhippogriff.sodden.config.SoddenConfig;

public class SoddenModMenu implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return SoddenConfig::getConfigScreen;
    }
}