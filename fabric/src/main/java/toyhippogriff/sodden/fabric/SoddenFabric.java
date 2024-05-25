package toyhippogriff.sodden.fabric;

import net.fabricmc.api.ModInitializer;
import toyhippogriff.sodden.Sodden;

public class SoddenFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        Sodden.init();
    }
}