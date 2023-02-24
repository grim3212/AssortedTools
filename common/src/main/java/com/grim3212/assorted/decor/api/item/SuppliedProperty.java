package com.grim3212.assorted.decor.api.item;

import java.util.function.Supplier;

public class SuppliedProperty<T> {
    private final T defaultValue;
    private Supplier<T> valueSupplier;

    public SuppliedProperty(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setValueSupplier(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return this.valueSupplier == null ? this.getDefaultValue() : valueSupplier.get();
    }
}
