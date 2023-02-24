package com.grim3212.assorted.decor.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigBuilder {
    private final String name;
    private final List<ConfigGroup> groups;

    public ConfigBuilder(String name) {
        this.name = name;
        this.groups = new ArrayList<>();
    }

    public List<ConfigGroup> getGroups() {
        return groups;
    }

    public void addGroups(ConfigGroup... groups) {
        this.groups.addAll(Arrays.asList(groups));
    }
}
