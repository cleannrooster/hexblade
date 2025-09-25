package com.cleannrooster.hexblade.mixin;

import com.cleannrooster.hexblade.Armors.MagusArmor;
import com.cleannrooster.hexblade.Hexblade;
import com.cleannrooster.hexblade.client.armor.renderer.MagusArmorRenderer;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cleannrooster.hexblade.client.entity.MagusRenderer.drawFireVertex;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin<E extends Entity>{
    Entity entity2;
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void renderMix(E entity, double d, double e, double f, float g, float h, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i, CallbackInfo info) {
        EntityRenderDispatcher renderer = (EntityRenderDispatcher) (Object) this;
        EntityRenderer entityRenderer = renderer.getRenderer(entity);
        if(MinecraftClient.getInstance() != null && entity instanceof LivingEntity living &&!(entity == MinecraftClient.getInstance().cameraEntity &&MinecraftClient.getInstance().options.getPerspective().equals(Perspective.FIRST_PERSON)) && Lists.newArrayList(living.getArmorItems().iterator()).stream().allMatch(itemStack -> itemStack.getItem() instanceof MagusArmor)){
            VertexConsumerProvider bufferSource = multiBufferSource;
            if( multiBufferSource != null && MagusArmorRenderer.getFlameTexture(entity) != null) {
                Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(MagusArmorRenderer.getFlameTexture(entity));
                Sprite sprite2 = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(MagusArmorRenderer.getFlameTexture(entity));
                poseStack.push();
                Vec3d vec3 = entityRenderer.getPositionOffset(entity, h);
                double d1 = MathHelper.lerp((double)h, entity.lastRenderY, entity.getY());

                double u = d + vec3.getX();
                double t = e + vec3.getY();
                double r = f + vec3.getZ();
                double y =  (e)+vec3.getY();



                poseStack.translate(u, t, r);
                float a = entity.getWidth() * 1.4F;
                poseStack.scale(a, a, a);
                float b = 2F;
                float c = 0.0F;
                float z = entity.getHeight() / a;
                float j = 0.0F;
                poseStack.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation());
                poseStack.translate(0.0F, 0.0F, -0.1F);


                float k = 0.0F;
                int l = 0;

                for (MatrixStack.Entry entry = poseStack.peek(); z > 0.0F; ++l) {
                    Sprite sprite3 = l % 2 == 0 ? sprite : sprite2;
                    float m = sprite3.getMinU();
                    float n = sprite3.getMinV();
                    float o = sprite3.getMaxU();
                    float p = sprite3.getMaxV();
                    if (l / 2 % 2 == 0) {
                        float q = o;
                        o = m;
                        m = q;
                    }

                    drawFireVertex(entry, bufferSource.getBuffer(TexturedRenderLayers.getEntityCutout()), -b - 0.0F, 0.0F - j, k, o, p);
                    drawFireVertex(entry, bufferSource.getBuffer(TexturedRenderLayers.getEntityCutout()), b - 0.0F, 0.0F - j, k, m, p);
                    drawFireVertex(entry, bufferSource.getBuffer(TexturedRenderLayers.getEntityCutout()), b - 0.0F, 1.4F - j, k, m, n);
                    drawFireVertex(entry, bufferSource.getBuffer(TexturedRenderLayers.getEntityCutout()), -b - 0.0F, 1.4F - j, k, o, n);
                    z -= 0.45F;
                    j -= 0.45F;
                    b *= 0.9F;
                    k -= 0.03F;
                }
                poseStack.pop();

            }
        }
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().options.getPerspective().equals(Perspective.FIRST_PERSON) && MinecraftClient.getInstance().player.equals(entity)) {
            return;
        }
      /*  if(MinecraftClient.getInstance() != null && !(MinecraftClient.getInstance().player != null && (MinecraftClient.getInstance().currentScreen instanceof CreativeInventoryScreen || MinecraftClient.getInstance().currentScreen instanceof InventoryScreen) && entity == MinecraftClient.getInstance().player) && entity.getWorld().getRegistryKey().equals(Hexblade.DIMENSIONKEY) && entity.getY() >= 62) {
            try {
                double d1 = MathHelper.lerp((double)h, entity.lastRenderY, entity.getY());

                Vec3d vec3 = entityRenderer.getPositionOffset(entity, h);
                double j = d + vec3.getX();
                double k = - (e)+(-62.5+d1)*2-3F/8F;
                double l = f + vec3.getZ();
                double y =  (e)+vec3.getY();

                poseStack.push();

                //quaternion.mul(-1);
                poseStack.scale(1,-1,1);

                poseStack.translate(j, k, l);
                entityRenderer.render(entity, g, h, poseStack, multiBufferSource, i);


                poseStack.translate(-vec3.getX(), -vec3.getY(), -vec3.getZ());


                poseStack.pop();


            } catch (Throwable var24) {
                CrashReport crashReport = CrashReport.create(var24, "Rendering entity in world");
                CrashReportSection crashReportCategory = crashReport.addElement("Entity being rendered");
                entity.populateCrashReport(crashReportCategory);
                CrashReportSection crashReportCategory2 = crashReport.addElement("Renderer details");
                crashReportCategory2.add("Assigned renderer", entityRenderer);
                crashReportCategory2.add("Location", CrashReportSection.createPositionString(entity.getWorld(), d, e, f));
                crashReportCategory2.add("Rotation", g);
                crashReportCategory2.add("Delta", h);
                throw new CrashException(crashReport);
            }
        }*/
    }

}