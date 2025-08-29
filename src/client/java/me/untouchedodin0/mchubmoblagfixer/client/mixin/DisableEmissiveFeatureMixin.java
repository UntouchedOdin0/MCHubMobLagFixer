package me.untouchedodin0.mchubmoblagfixer.client.mixin;

import me.untouchedodin0.mchubmoblagfixer.client.MCHubMobLagFixerConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(LivingEntityRenderer.class)
@Mixin(EntityRenderer.class)
public class DisableEmissiveFeatureMixin<T extends Entity, S extends EntityRenderState> {

    @Unique private Entity entity;

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void updateRenderState(T entity1, S reusedState, float partialTick, CallbackInfo callbackInfo) {
        this.entity = entity1;
    }

    @Inject(
            method = "renderLabelIfPresent",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hideNametagIfNeeded(S state, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo callbackInfo) {
        if (MCHubMobLagFixerConfig.shouldHideNameTags()) {
            callbackInfo.cancel();
        }
    }
}
