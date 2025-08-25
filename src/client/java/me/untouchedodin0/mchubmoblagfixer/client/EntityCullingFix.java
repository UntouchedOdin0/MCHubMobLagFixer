package me.untouchedodin0.mchubmoblagfixer.client;

import net.fabricmc.api.ClientModInitializer;

public class EntityCullingFix implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("[EntityCullingFix] Initialized");
        MCHubMobLagFixerConfig.load();
    }
}
