package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolsEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, AssortedTools.MODID);

	public static final RegistryObject<EntityType<BoomerangEntity>> WOOD_BOOMERANG = register("wood_boomerang", EntityType.Builder.<BoomerangEntity>of(WoodBoomerangEntity::new, EntityClassification.MISC).sized(0.5f, 0.5f).setTrackingRange(32).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true));
	public static final RegistryObject<EntityType<BoomerangEntity>> DIAMOND_BOOMERANG = register("diamond_boomerang", EntityType.Builder.<BoomerangEntity>of(DiamondBoomerangEntity::new, EntityClassification.MISC).sized(0.5f, 0.5f).setTrackingRange(32).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true));
	public static final RegistryObject<EntityType<PokeballEntity>> POKEBALL = register("pokeball", EntityType.Builder.<PokeballEntity>of(PokeballEntity::new, EntityClassification.MISC).sized(0.5f, 0.5f).setTrackingRange(32).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(final String name, final EntityType.Builder<T> builder) {
		return ENTITIES.register(name, () -> builder.build(new ResourceLocation(AssortedTools.MODID, name).toString()));
	}
}
