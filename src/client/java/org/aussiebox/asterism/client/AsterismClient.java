package org.aussiebox.asterism.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.client.render.hud.AstralWyrmtoothHud;

public class AsterismClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.addFirst(Asterism.id("astral_wyrmtooth"), new AstralWyrmtoothHud());
    }
}
