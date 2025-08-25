package me.untouchedodin0.mchubmoblagfixer.client.mixin;

import me.untouchedodin0.mchubmoblagfixer.client.MCHubMobLagFixerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ,
                              float tickProgress, MatrixStack matrices,
                              VertexConsumerProvider vertexConsumers) {
        throw new AssertionError();
    }

    @Redirect(
            method = "renderEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity("
                            + "Lnet/minecraft/entity/Entity;"
                            + "DDDF"
                            + "Lnet/minecraft/client/util/math/MatrixStack;"
                            + "Lnet/minecraft/client/render/VertexConsumerProvider;)V"
            )
    )
    private void entitycullingfix$cullEntities(
            WorldRenderer instance,
            Entity entity,
            double camX, double camY, double camZ,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers) {

        var cameraEntity = MinecraftClient.getInstance().cameraEntity;
        if (cameraEntity == null) return;

        double maxDistanceSq = MCHubMobLagFixerConfig.getRenderDistanceSquared(); //squared * squared;

        // Skip far entities
        if (entity.squaredDistanceTo(cameraEntity) > maxDistanceSq) {
            return;
        }

        // Skip invisible entities
        if (!entity.shouldRender(camX, camY, camZ)) {
            return;
        }

        // Render everything else normally
        renderEntity(entity, camX, camY, camZ, tickDelta, matrices, vertexConsumers);
    }
}
