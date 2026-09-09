package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Writes an {@code equipment_asset} definition for every armour material.
 * <p>
 * Since 1.21.4 the worn-armour texture is not derived from the material's name. An
 * {@code ArmorMaterial} carries a {@link ResourceKey} into the {@code equipment_asset} registry, and
 * the client resolves the layer textures through the JSON that key names. Without these files
 * armour renders untextured on the player - and nothing warns, because a missing equipment asset is
 * not a missing model.
 * <p>
 * This does not extend vanilla's {@code EquipmentAssetProvider}: its bootstrap is private and
 * hardcodes vanilla's own materials, so there is nothing to hook into. The write itself is two
 * lines, which is all that class does either.
 * <p>
 * {@code addHumanoidLayers} registers the {@code HUMANOID}, {@code HUMANOID_BABY} and
 * {@code HUMANOID_LEGGINGS} layers from one texture name, which is exactly the split the old
 * {@code <material>_layer_1.png} / {@code _layer_2.png} pair encoded. The textures now live under
 * {@code textures/entity/equipment/{humanoid,humanoid_leggings}/<material>.png} in the mod's own
 * namespace, rather than squatting in {@code assets/minecraft}.
 */
public class ToolsEquipmentAssetProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public ToolsEquipmentAssetProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> assets = new HashMap<>();

        for (ToolsArmorMaterials material : ToolsArmorMaterials.values()) {
            assets.put(material.assetId(), EquipmentClientInfo.builder()
                    .addHumanoidLayers(Identifier.fromNamespaceAndPath(Constants.MOD_ID, material.getName()))
                    .build());
        }

        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, assets);
    }

    @Override
    public String getName() {
        return "Equipment Asset Definitions: " + Constants.MOD_ID;
    }
}
