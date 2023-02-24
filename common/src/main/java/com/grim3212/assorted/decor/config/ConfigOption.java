package com.grim3212.assorted.decor.config;

import java.util.function.Supplier;

public class ConfigOption<T> {

    private final String name;
    private final T defaultValue;
    private final String comment;
    private Supplier<T> currentValue;

    public ConfigOption(String name, T defaultValue, String comment) {
        this.defaultValue = defaultValue;
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return this.name;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getComment() {
        return this.comment;
    }

    public void setValueSupplier(Supplier<T> currentValue) {
        this.currentValue = currentValue;
    }

    public T getValue() {
        return this.currentValue == null ? this.getDefaultValue() : this.currentValue.get();
    }

    public static class ConfigOptionMinMax<T> extends ConfigOption<T> {
        private final T min;
        private final T max;

        public ConfigOptionMinMax(String name, T defaultValue, String comment, T min, T max) {
            super(name, defaultValue, comment);
            this.min = min;
            this.max = max;
        }

        public T getMin() {
            return min;
        }

        public T getMax() {
            return max;
        }
    }
}
