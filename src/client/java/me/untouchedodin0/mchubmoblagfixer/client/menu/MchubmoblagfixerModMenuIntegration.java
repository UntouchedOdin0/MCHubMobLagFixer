package me.untouchedodin0.mchubmoblagfixer.client.menu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class MchubmoblagfixerModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new RenderDistanceConfigScreen(parent);
    }
}
