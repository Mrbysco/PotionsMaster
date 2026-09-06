package com.thevortex.potionsmaster.render.util.xray;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.thevortex.potionsmaster.reference.Reference;
import com.thevortex.potionsmaster.render.util.BlockInfo;
import com.thevortex.potionsmaster.render.util.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber(Dist.CLIENT)
public class Render {
    public static class OreList {
        protected List<BlockInfo> oreList = Collections.synchronizedList(new ArrayList<>());

        public void add(BlockInfo info) {
            oreList.add(info);
            INSTANCE.dataAvailable.set(true);
        }

        public void remove(BlockInfo info) {
            oreList.remove(info);
            INSTANCE.dataAvailable.set(true);
        }

        public boolean isEmpty() {
            return oreList.isEmpty();
        }

        public void clear() {
            oreList.clear();
            INSTANCE.dataAvailable.set(true);
        }

        public void addAll(Collection<? extends BlockInfo> list) {
            oreList.addAll(list);
            INSTANCE.dataAvailable.set(true);
        }
    }

    public static final Render INSTANCE = new Render();

    private static final RenderPipeline XRAY_PIPELINE = RenderPipeline
            .builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "xray_lines"))
            .withDepthStencilState(Optional.empty())
            .build();

    private static final RenderType XRAY_TYPE = RenderType.create(
            "xray",
            RenderSetup.builder(XRAY_PIPELINE).createRenderSetup()
    );

    private final AtomicBoolean dataAvailable = new AtomicBoolean(false);
    public static OreList ores = new OreList();

    @SubscribeEvent
    public static void registerPipeline(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(XRAY_PIPELINE);
    }

    public void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
        if (ores.isEmpty()) return;

        List<BlockInfo> snapshot;
        synchronized (ores.oreList) {
            snapshot = new ArrayList<>(ores.oreList);
        }
        if (snapshot.isEmpty()) return;

        PoseStack stack = event.getPoseStack();
        Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;

        stack.pushPose();
        stack.translate(-camera.x, -camera.y, -camera.z);

        event.getSubmitNodeCollector().submitCustomGeometry(stack, XRAY_TYPE, (pose, consumer) -> {
            for (var info : snapshot) {
                if (info != null) {
                    renderShape(pose, consumer, Shapes.block(), info.getX(), info.getY(), info.getZ(), info.color);
                }
            }
        });

        stack.popPose();
    }

    public static void renderShape(PoseStack.Pose posestack$pose, VertexConsumer vcon, VoxelShape shape, double x, double y, double z, int color) {
        float[] colors = Util.getComponents(color);
        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            float f = (float)(x2 - x1), f1 = (float)(y2 - y1), f2 = (float)(z2 - z1);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f /= f3; f1 /= f3; f2 /= f3;
            vcon.addVertex(posestack$pose, (float)(x1 + x), (float)(y1 + y), (float)(z1 + z))
                    .setColor(colors[0], colors[1], colors[2], colors[3])
                    .setNormal(f, f1, f2)
                    .setLineWidth(2.0f);
            vcon.addVertex(posestack$pose, (float)(x2 + x), (float)(y2 + y), (float)(z2 + z))
                    .setColor(colors[0], colors[1], colors[2], colors[3])
                    .setNormal(f, f1, f2)
                    .setLineWidth(2.0f);
        });
    }
}
