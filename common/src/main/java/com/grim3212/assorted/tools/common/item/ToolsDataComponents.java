package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.tools.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class ToolsDataComponents {

    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENTS = RegistryProvider.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    public static final IRegistryObject<DataComponentType<CapturedEntity>> CAPTURED_ENTITY = DATA_COMPONENTS.register("captured_entity",
            () -> new DataComponentType.Builder<CapturedEntity>().persistent(CapturedEntity.CODEC).networkSynchronized(CapturedEntity.STREAM_CODEC).build());
    public static final IRegistryObject<DataComponentType<WandModeInfo>> WAND_MODE_INFO = DATA_COMPONENTS.register("wand_mode_info",
            () -> new DataComponentType.Builder<WandModeInfo>().persistent(WandModeInfo.CODEC).networkSynchronized(WandModeInfo.STREAM_CODEC).build());
    public static final IRegistryObject<DataComponentType<BucketContents>> BUCKET_CONTENTS = DATA_COMPONENTS.register("bucket_contents",
            () -> new DataComponentType.Builder<BucketContents>().persistent(BucketContents.CODEC).networkSynchronized(BucketContents.STREAM_CODEC).build());

    // Runs before ToolsItems, whose pokeball, wands and buckets carry these as default components.
    public static void init() {
        Services.PLATFORM.showComponentTooltip(CAPTURED_ENTITY);
        Services.PLATFORM.showComponentTooltip(WAND_MODE_INFO);
        Services.PLATFORM.showComponentTooltip(BUCKET_CONTENTS);
    }
}
