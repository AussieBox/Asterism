package org.aussiebox.asterism;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.aussiebox.asterism.command.MainCommand;
import org.aussiebox.asterism.component.ModDataComponentTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Asterism implements ModInitializer {
    public static final String MOD_ID = "asterism";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModDataComponentTypes.init();

        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            MainCommand.register(dispatcher, registryAccess);
        }));
    }
}
