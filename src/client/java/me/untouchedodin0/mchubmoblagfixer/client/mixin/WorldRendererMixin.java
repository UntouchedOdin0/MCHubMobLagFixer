package me.untouchedodin0.mchubmoblagfixer.client.mixin;

import me.untouchedodin0.mchubmoblagfixer.client.MCHubMobLagFixerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ,
                              float tickProgress, MatrixStack matrices,
                              VertexConsumerProvider vertexConsumers) {
        throw new AssertionError();
    }

    @Shadow public abstract boolean isRenderingReady(BlockPos pos);

    @Shadow private int cameraChunkX;
    @Shadow private int cameraChunkY;
    @Shadow private int cameraChunkZ;
    // Queue for nametag rendering on the main thread
    @Unique
    private final Queue<Runnable> nametagQueue = new ConcurrentLinkedQueue<>();

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
        if (MinecraftClient.getInstance().world == null) return;

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


        // Queue nametag render if entity has one
        if (entity instanceof LivingEntity living) {
            queueNametagRender(living, matrices, vertexConsumers);
        }

        // Render everything else normally
        renderEntity(entity, camX, camY, camZ, tickDelta, matrices, vertexConsumers);
    }

    @Unique
    private void queueNametagRender(LivingEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumer) {
        var cameraEntity = MinecraftClient.getInstance().getCameraEntity();
        if (cameraEntity == null) return;

        // Distance culling for nametags
        double maxDistanceSq = 64 * 64; // You can make this configurable
        if (entity.squaredDistanceTo(cameraEntity) > maxDistanceSq) return;

        // Queue text rendering for main thread
        Text name = entity.getDisplayName();
        nametagQueue.add(() -> {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            minecraftClient.textRenderer.draw(
                    name,
                    0f,
                    0f,
                    0xFFFFFF,
                    false,
                    matrices.peek().getPositionMatrix(),
                    vertexConsumer,
                    TextRenderer.TextLayerType.NORMAL,
                    0,
                    15728880
            );
        });

//        // Distance culling for nametags
//        double maxDistanceSquared = 64 * 64; // You can make this configurable
//        if (entity.squaredDistanceTo(cameraEntity) > maxDistanceSquared) return;
//
//        // Skip invisible entities
//        if (!entity.shouldRender(cameraChunkX, cameraChunkY, cameraChunkZ)) return;
//
//        // Queue nametag render if entity has one
//        if (entity instanceof LivingEntity living) {
//            queueNametagRender(living, matrices, vertexConsumer);
//        }
    }
}
