package me.jacktym.aiomacro.rendering;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.NGGlobal;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class CosmeticRendering implements LayerRenderer<EntityLivingBase> {


    private final RenderPlayer playerRenderer;

    public CosmeticRendering(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entityLivingBaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (AIOMVigilanceConfig.renderingEnabled) {
            if (Main.boobMap.containsKey(entityLivingBaseIn.getUniqueID().toString())) {
                int boobSize = Main.boobMap.get(entityLivingBaseIn.getUniqueID().toString());

                this.playerRenderer.bindTexture(((AbstractClientPlayer) entityLivingBaseIn).getLocationSkin());

                GlStateManager.pushMatrix();
                ModelRenderer bipedBoobs = new ModelRenderer(playerRenderer.getMainModel(), 17, 20);
                bipedBoobs.addBox(-4.0F, -6.0F, -9.0F, 8, 4, 3, 0.0F);

                switch (boobSize) {
                    case 0:
                        GlStateManager.translate(0.0F, 0.5F, 0.3F);
                        break;
                    case 1:
                        GlStateManager.translate(0.0F, 0.6F, 0.36F);
                        break;
                    case 2:
                        GlStateManager.translate(0.0F, 0.7F, 0.45F);
                        break;
                    case 3:
                        GlStateManager.translate(0.0F, 0.8F, 0.55F);
                        break;
                    case 4:
                        GlStateManager.translate(0.0F, 0.9F, 0.65F);
                        break;
                    case 5:
                        GlStateManager.translate(0.0F, 1.28F, 1.0F);
                        break;
                    case 6:
                        GlStateManager.translate(0.0F, 2.4F, 2.14F);
                        break;
                    default:
                        break;
                }

                if (entityLivingBaseIn.isSneaking()) {
                    getSneakTranslation(boobSize);
                }
                getScale(boobSize);

                //ModelBase.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, bipedBoobs);
                bipedBoobs.rotationPointX = 0.0F;
                bipedBoobs.rotationPointY = 0.0F;
                bipedBoobs.render(0.0625F);
                GlStateManager.popMatrix();
            }
            if (Main.testicleMap.containsKey(entityLivingBaseIn.getUniqueID().toString())) {
                int testicleSize = Main.testicleMap.get(entityLivingBaseIn.getUniqueID().toString());

                this.playerRenderer.bindTexture(((AbstractClientPlayer) entityLivingBaseIn).getLocationSkin());

                GlStateManager.pushMatrix();
                switch (testicleSize) {
                    case 0:
                        GlStateManager.translate(0.0F, 0.563F, 0.3F);
                        break;
                    case 1:
                        GlStateManager.translate(0.0F, 0.53F, 0.36F);
                        break;
                    case 2:
                        GlStateManager.translate(0.0F, 0.5F, 0.5F);
                        break;
                    case 3:
                        GlStateManager.translate(0.0F, 0.47F, 0.55F);
                        break;
                    case 4:
                        GlStateManager.translate(0.0F, 0.435F, 0.65F);
                        break;
                    case 5:
                        GlStateManager.translate(0.0F, 0.313F, 1.0F);
                        break;
                    case 6:
                        GlStateManager.translate(0.0F, -0.06F, 2.15F);
                        break;
                    default:
                        break;
                }
                if (entityLivingBaseIn.isSneaking()) {
                    getSneakTranslation(testicleSize);
                }
                getScale(testicleSize);
                ModelRenderer bipedTesticles = new ModelRenderer(playerRenderer.getMainModel(), 0, 21);
                bipedTesticles.addBox(-2.0F, 2.0F, -9.0F, 4, 4, 3, 0.0F);
                bipedTesticles.addBox(-1.0F, 3.0F, -15.0F, 2, 2, 6, 0.0F);

                ModelBase.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, bipedTesticles);
                bipedTesticles.rotationPointX = 0.0F;
                bipedTesticles.rotationPointY = 0.0F;
                bipedTesticles.render(0.0625F);
                GlStateManager.popMatrix();
            }
            if (Main.assMap.containsKey(entityLivingBaseIn.getUniqueID().toString())) {
                int assSize = Main.assMap.get(entityLivingBaseIn.getUniqueID().toString());

                this.playerRenderer.bindTexture(((AbstractClientPlayer) entityLivingBaseIn).getLocationSkin());

                GlStateManager.pushMatrix();
                switch (assSize) {
                    case 0:
                        GlStateManager.translate(0.0F, 0.5F, 0.3F);
                        break;
                    case 1:
                        GlStateManager.translate(0.0F, 0.5F, 0.36F);
                        break;
                    case 2:
                    case 3:
                        GlStateManager.translate(0.0F, 0.5F, 0.41F);
                        break;
                    case 4:
                        GlStateManager.translate(0.0F, 0.5F, 0.50F);
                        break;
                    case 5:
                        GlStateManager.translate(0.0F, 0.55F, 0.69F);
                        break;
                    case 6:
                        GlStateManager.translate(0.0F, 0.65F, 1.25F);
                        break;
                    default:
                        break;
                }
                if (entityLivingBaseIn.isSneaking()) {
                    getSneakTranslation(assSize);
                }
                getScale(assSize);
                ModelRenderer bipedAss = new ModelRenderer(playerRenderer.getMainModel(), 18, 24);
                bipedAss.addBox(-4.0F, -0.6F, -3.0F, 8, 4, 3, 0.0F);

                ModelBase.copyModelAngles(this.playerRenderer.getMainModel().bipedBody, bipedAss);
                bipedAss.rotationPointX = 0.0F;
                bipedAss.rotationPointY = 0.0F;
                bipedAss.render(0.0625F);
                GlStateManager.popMatrix();
            }
            if (Main.haloMap.containsKey(entityLivingBaseIn.getUniqueID().toString())) {
                int haloSize = Main.haloMap.get(entityLivingBaseIn.getUniqueID().toString());

                GlStateManager.pushMatrix();
                if (entityLivingBaseIn.isSneaking()) {
                    getSneakTranslation(haloSize);
                }
                getScale(haloSize);
                this.playerRenderer.bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/HaloTexture.png"));
                ModelRenderer bipedHalo = new ModelRenderer(playerRenderer.getMainModel(), 0, 0);
                bipedHalo.addBox(-2.0F, -10.0F, -6.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(1.0F, -10.0F, 5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(0.0F, -10.0F, 5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-1.0F, -10.0F, 5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-2.0F, -10.0F, 5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-2.0F, -10.0F, 4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-3.0F, -10.0F, 4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-4.0F, -10.0F, 4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-4.0F, -10.0F, 3.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-5.0F, -10.0F, 3.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-5.0F, -10.0F, 2.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-5.0F, -10.0F, 1.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-6.0F, -10.0F, 1.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-6.0F, -10.0F, -2.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-6.0F, -10.0F, -1.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(1.0F, -10.0F, 4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(2.0F, -10.0F, 4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(3.0F, -10.0F, 4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(3.0F, -10.0F, 3.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(4.0F, -10.0F, 3.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(4.0F, -10.0F, 2.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(4.0F, -10.0F, 1.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(5.0F, -10.0F, 1.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(5.0F, -10.0F, -0.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(5.0F, -10.0F, -1.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(5.0F, -10.0F, -2.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-6.0F, -10.0F, 0.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-5.0F, -10.0F, -2.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-5.0F, -10.0F, -3.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-5.0F, -10.0F, -4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-4.0F, -10.0F, -4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-4.0F, -10.0F, -5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-3.0F, -10.0F, -5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-2.0F, -10.0F, -5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(4.0F, -10.0F, -2.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(4.0F, -10.0F, -3.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(4.0F, -10.0F, -4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(3.0F, -10.0F, -4.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(3.0F, -10.0F, -5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(2.0F, -10.0F, -5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(1.0F, -10.0F, -5.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(1.0F, -10.0F, -6.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(0.0F, -10.0F, -6.0F, 1, 1, 1, 0.0F);
                bipedHalo.addBox(-1.0F, -10.0F, -6.0F, 1, 1, 1, 0.0F);

                bipedHalo.rotationPointX = 0.0F;
                bipedHalo.rotationPointY = 0.0F;
                bipedHalo.render(0.0625F);
                GlStateManager.popMatrix();
            }

            /*int gooseSize = 1;
            GlStateManager.pushMatrix();
            if (entityLivingBaseIn.isSneaking()) {
                getSneakTranslation(gooseSize);
            }
            getScale(gooseSize);
            this.playerRenderer.bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/GooseTexture.png"));
            ModelRenderer bipedGoose = new ModelRenderer(playerRenderer.getMainModel(), 0, 0);
            bipedGoose.addBox(7.0F, 12.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 11.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 10.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 8.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 9.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 7.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 5.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 6.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 4.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -4.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -3.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -3.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -1.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 0.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -2.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 1.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 2.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, 3.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -3.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -6.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -4.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -4.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -3.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -5.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -6.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -5.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -4.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -8.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -7.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -9.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -5.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -4.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -6.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -3.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -6.0F, -3.0F, 1, 1, 1, 0.0F);
            bipedGoose.addBox(7.0F, -5.0F, -3.0F, 1, 1, 1, 0.0F);

            bipedGoose.rotationPointX = 0.0F;
            bipedGoose.rotationPointY = 0.0F;
            bipedGoose.render(0.0625F);
            GlStateManager.popMatrix();*/
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }


    private void getScale(int size) {
        switch (size) {
            case 0:
                GlStateManager.scale(1.0F, 1.0F, 1.0F);
                break;
            case 1:
                GlStateManager.scale(1.25F, 1.25F, 1.25F);
                break;
            case 2:
                GlStateManager.scale(1.5F, 1.5F, 1.5F);
                break;
            case 3:
                GlStateManager.scale(1.75F, 1.75F, 1.75F);
                break;
            case 4:
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                break;
            case 5:
                GlStateManager.scale(3.0F, 3.0F, 3.0F);
                break;
            case 6:
                GlStateManager.scale(6.0F, 6.0F, 6.0F);
                break;
            default:
                break;
        }
    }

    private void getSneakTranslation(int size) {
        switch (size) {
            case 0:
                GlStateManager.translate(0.0F, 0.01F, 0.17F);
                break;
            case 1:
                GlStateManager.translate(0.0F, -0.1F, 0.1F);
                break;
            case 2:
            case 3:
            case 4:
                GlStateManager.translate(0.0F, -0.2F, 0.05F);
                break;
            case 5:
                GlStateManager.translate(0.0F, -0.2F, 0.1F);
                break;
            case 6:
                GlStateManager.translate(0.0F, -0.2F, -0.3F);
                break;
            default:
                break;
        }
    }
}
