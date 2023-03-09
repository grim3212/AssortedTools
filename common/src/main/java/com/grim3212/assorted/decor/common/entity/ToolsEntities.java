package com.grim3212.assorted.decor.common.entity;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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
        return ENTITIES.register(name, () -> builder.build(new ResourceLocation(Constants.MOD_ID, name).toString()));
    }

    public static void init() {
    }
}
