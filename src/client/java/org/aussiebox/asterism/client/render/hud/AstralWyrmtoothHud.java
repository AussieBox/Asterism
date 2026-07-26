package org.aussiebox.asterism.client.render.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec2f;
import org.aussiebox.asterism.AsterismConstants;
import org.aussiebox.asterism.cca.player.PlayerComponent;
import org.aussiebox.asterism.item.ModItems;
import org.aussiebox.circuit_core.client.helper.PlayerExclusiveItemClientHelper;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.NonNull;

public class AstralWyrmtoothHud implements HudElement {
    @Override
    public void render(@NonNull DrawContext context, @NonNull RenderTickCounter tickCounter) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || !PlayerExclusiveItemClientHelper.playerCanGet(ModItems.ASTRAL_WYRMTOOTH.build()) || !player.getMainHandStack().isOf(ModItems.ASTRAL_WYRMTOOTH.build())) return;

        drawSmallStars(context, player);
    }

    public static void drawSmallStars(DrawContext context, @NonNull PlayerEntity player) {
        PlayerComponent component = PlayerComponent.KEY.get(player);
        if (component.getStarHudOffsets().isEmpty() || component.getSyncedWyrmtoothTier() < 0) return;

        int activeStars = component.getSouls() - AsterismConstants.AstralWyrmtooth.tiers.get(component.getSyncedWyrmtoothTier()).soulRequirement();

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Vec2f offset : component.getStarHudOffsets()) {
            if (offset.y < minY) {
                minY = (int) offset.y - 19 - (25 / 2);
            }
            int bottomEdge = (int) (offset.y + 5);
            if (bottomEdge > maxY) {
                maxY = bottomEdge;
            }
        }

        int totalHeight = maxY - minY;

        int centerY = (context.getScaledWindowHeight() / 2) - (totalHeight / 2);

        Matrix3x2fStack matrixStack = context.getMatrices();
        matrixStack.pushMatrix();

        matrixStack.translate(5, centerY - minY);

        for (Vec2f offset : component.getStarHudOffsets()) {
            if (component.getStarHudOffsets().getLast() != offset) {
                Vec2f next = component.getStarHudOffsets().get(component.getStarHudOffsets().indexOf(offset) + 1);
                drawDiagonalLine(context, 0, 0, offset.x+2.5F, offset.y+2.5F, next.x+2.5F, next.y+2.5F, 1.0F, 0xFFA0D2BB);
            }
        }

        Vec2f top = component.getStarHudOffsets().getFirst();
        drawDiagonalLine(context, 0, 0, top.x+2.5F, top.y+2.5F, (float) 25/2-0.75F, top.y-25+18.5F, 1.0F, 0xFFA0D2BB);

        for (Vec2f offset : component.getStarHudOffsets()) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    AsterismConstants.Textures.Hud.AstralWyrmtooth.STAR_SMALL,
                    (int) offset.x,
                    (int) offset.y,
                    0, 0,
                    5, 5,
                    5, 5,
                    5, 5,
                    component.getStarHudOffsets().indexOf(offset) < activeStars ? 0xFFAAAAAA : 0xFFFFFFFF
            );
        }

        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                AsterismConstants.Textures.Hud.AstralWyrmtooth.STAR_BIG,
                (25 / 2) - (17 / 2),
                (int) (top.y - 25),
                0, 0,
                17, 19,
                17, 19,
                17, 19,
                0xFFFFFFFF
        );

        matrixStack.popMatrix();
    }

    public static void drawDiagonalLine(DrawContext context, float x1, float y1, float x2, float y2, float thickness, int color) {
        if (x1 == x2 && y1 == y2) return;

        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        float angle = (float) Math.atan2(dy, dx);

        Matrix3x2fStack matrixStack = context.getMatrices();
        matrixStack.pushMatrix();

        matrixStack.translate(x1, y1);

        matrixStack.rotate(angle);

        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                AsterismConstants.Textures.Hud.WHITE,
                0,
                (int) (-thickness / 2.0f),
                0.0f,
                0.0f,
                (int) length,
                (int) thickness,
                1,
                1,
                1,
                1,
                color
        );

        matrixStack.popMatrix();
    }

    public static void drawDiagonalLine(DrawContext context, float additionalX, float additionalY, float x1, float y1, float x2, float y2, float thickness, int color) {
        if (x1 == x2 && y1 == y2) return;

        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        float angle = (float) Math.atan2(dy, dx);

        Matrix3x2fStack matrixStack = context.getMatrices();
        matrixStack.pushMatrix();

        matrixStack.translate(x1 + additionalX, y1 + additionalY);

        matrixStack.rotate(angle);

        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                AsterismConstants.Textures.Hud.WHITE,
                0,
                (int) (-thickness / 2.0f),
                0.0f,
                0.0f,
                (int) length,
                (int) thickness,
                1,
                1,
                1,
                1,
                color
        );

        matrixStack.popMatrix();
    }
}
