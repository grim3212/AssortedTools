package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.tools.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ToolsEntities {

    public static final RegistryProvider<EntityType<?>> ENTITIES = RegistryProvider.create(Registries.ENTITY_TYPE, Constants.MOD_ID);

    public static final IRegistryObject<EntityType<BoomerangEntity>> WOOD_BOOMERANG = register("wood_boomerang", EntityType.Builder.<BoomerangEntity>of(WoodBoomerangEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(32).updateInterval(1));
    public static final IRegistryObject<EntityType<BoomerangEntity>> DIAMOND_BOOMERANG = register("diamond_boomerang", EntityType.Builder.<BoomerangEntity>of(DiamondBoomerangEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(32).updateInterval(1));
    public static final IRegistryObject<EntityType<PokeballEntity>> POKEBALL = register("pokeball", EntityType.Builder.<PokeballEntity>of(PokeballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(32).updateInterval(1));
    public static final IRegistryObject<EntityType<BetterSpearEntity>> BETTER_SPEAR = register("better_spear", EntityType.Builder.<BetterSpearEntity>of(BetterSpearEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    private static <T extends Entity> IRegistryObject<EntityType<T>> register(final String name, final EntityType.Builder<T> builder) {
        // Entity types are built against their own registry key now, not a raw string
        final ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ENTITIES.register(name, () -> builder.build(key));
    }

    public static void init() {
    }
}
