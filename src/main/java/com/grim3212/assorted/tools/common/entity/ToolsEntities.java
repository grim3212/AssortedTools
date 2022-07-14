package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ToolsEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AssortedTools.MODID);

	public static final RegistryObject<EntityType<BoomerangEntity>> WOOD_BOOMERANG = register("wood_boomerang", EntityType.Builder.<BoomerangEntity>of(WoodBoomerangEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).setTrackingRange(32).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true));
	public static final RegistryObject<EntityType<BoomerangEntity>> DIAMOND_BOOMERANG = register("diamond_boomerang", EntityType.Builder.<BoomerangEntity>of(DiamondBoomerangEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).setTrackingRange(32).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true));
	public static final RegistryObject<EntityType<PokeballEntity>> POKEBALL = register("pokeball", EntityType.Builder.<PokeballEntity>of(PokeballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).setTrackingRange(32).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true));
	public static final RegistryObject<EntityType<BetterSpearEntity>> BETTER_SPEAR = register("better_spear", EntityType.Builder.<BetterSpearEntity>of(BetterSpearEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(final String name, final EntityType.Builder<T> builder) {
		return ENTITIES.register(name, () -> builder.build(new ResourceLocation(AssortedTools.MODID, name).toString()));
	}
}
