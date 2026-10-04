package net.yteron.BWaC.block.system;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.yteron.BWaC.init.ModClimbing;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClimbingClient {
    @SubscribeEvent
    public static void onFMLCLientSetup(FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(ModClimbing.SIT_ENTITY_TYPE, EmptyRenderer::new);
    }

    private static class EmptyRenderer extends EntityRenderer<ClimbingEntity>
    {
        protected EmptyRenderer(EntityRendererManager renderManager)
        {
            super(renderManager);
        }

        @Override
        public boolean shouldRender(ClimbingEntity entity, ClippingHelper camera, double camX, double camY, double camZ)
        {
            return false;
        }

        @Override
        public ResourceLocation getTextureLocation(ClimbingEntity p_110775_1_) {
            return null;
        }
    }
}
