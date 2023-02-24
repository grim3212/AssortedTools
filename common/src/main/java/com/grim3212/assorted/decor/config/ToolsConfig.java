package com.grim3212.assorted.decor.config;

public class ToolsConfig {
    public static class Common {
        public final ConfigOption<Boolean> wandsEnabled;
        public final ConfigOption<Boolean> boomerangsEnabled;
        public final ConfigOption<Boolean> hammersEnabled;
        public final ConfigOption<Boolean> multiToolsEnabled;
        public final ConfigOption<Boolean> pokeballEnabled;
        public final ConfigOption<Boolean> chickenSuitEnabled;
        public final ConfigOption<Boolean> extraMaterialsEnabled;
        public final ConfigOption<Boolean> spearsEnabled;
        public final ConfigOption<Boolean> betterSpearsEnabled;
        public final ConfigOption<Boolean> betterBucketsEnabled;
        public final ConfigOption<Boolean> moreShearsEnabled;
        public final ConfigOption<Boolean> ultimateFistEnabled;

        public Common(ConfigBuilder builder) {
            ConfigGroup partGroup = new ConfigGroup("Parts");
            wandsEnabled = new ConfigOption<>("wandsEnabled", true, "Set this to true if you would like wands to be craftable and found in the creative tab.");
            boomerangsEnabled = new ConfigOption<>("boomerangsEnabled", true, "Set this to true if you would like boomerangs to be craftable and found in the creative tab.");
            hammersEnabled = new ConfigOption<>("hammersEnabled", true, "Set this to true if you would like hammers to be craftable and found in the creative tab.");
            multiToolsEnabled = new ConfigOption<>("multiToolsEnabled", true, "Set this to true if you would like multitools to be craftable and found in the creative tab.");
            pokeballEnabled = new ConfigOption<>("pokeballEnabled", true, "Set this to true if you would like the pokeball to be craftable and found in the creative tab.");
            chickenSuitEnabled = new ConfigOption<>("chickenSuitEnabled", true, "Set this to true if you would like the chicken suit to be craftable and found in the creative tab as well as if you want the Chicken Jump enchantment to be able to be applied.");
            extraMaterialsEnabled = new ConfigOption<>("extraMaterialsEnabled", true, "Set this to true if you would like to enable support for crafting the extra tools and armor that this supports. For example, Steel, Copper, or Ruby tools and armor.");
            spearsEnabled = new ConfigOption<>("spearsEnabled", false, "Set this to true if you would like the old DEPRECATED spears to be craftable and found in the creative tab.");
            betterSpearsEnabled = new ConfigOption<>("betterSpearsEnabled", true, "Set this to true if you would like the better spears (the ones that can be enchanted) to be craftable and found in the creative tab as well as the Enchantments for it to be enchanted on books.");
            betterBucketsEnabled = new ConfigOption<>("betterBucketsEnabled", true, "Set this to true if you would like better buckets to be craftable and found in the creative tab.");
            moreShearsEnabled = new ConfigOption<>("moreShearsEnabled", true, "Set this to true if you would like the extra shears to be craftable and found in the creative tab.");
            ultimateFistEnabled = new ConfigOption<>("ultimateFistEnabled", true, "Set this to true if you would like the ultimate fist to be craftable and found in the creative tab as well as the fragements generate in loot.");
            builder.addGroups(partGroup);
        }
    }
}
