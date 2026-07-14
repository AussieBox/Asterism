package org.aussiebox.asterism.cca;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.aussiebox.asterism.Asterism;
import org.aussiebox.asterism.AsterismConstants;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class PlayerComponent implements AutoSyncedComponent {
    public static final ComponentKey<PlayerComponent> KEY = ComponentRegistry.getOrCreate(Asterism.id("player_component"), PlayerComponent.class);
    private final PlayerEntity player;

    @Getter private int souls;

    public void addSouls(int souls) {
        this.souls = Math.clamp(this.souls + souls, 0, AsterismConstants.SOUL_CAP);
    }

    public void setSouls(int souls) {
        this.souls = Math.clamp(souls, 0, AsterismConstants.SOUL_CAP);
    }

    public PlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void readData(ReadView tag) {
        this.souls = tag.getInt("souls", 0);
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putInt("souls", souls);
    }
}
