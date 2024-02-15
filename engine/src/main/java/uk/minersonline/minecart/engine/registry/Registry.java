package uk.minersonline.minecart.engine.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Registry<V> {
	private final Map<Identifier, V> registryMap = new HashMap<>();
	private boolean frozen = false;

	public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry) {
		if (registry.isFrozen()) {
			throw new IllegalStateException("Cannot register entries to a frozen registry.");
		}
		registry.registryMap.put(id, entry);
		return entry;
	}

	public V get(Identifier id) {
		return registryMap.get(id);
	}

	public void freeze() {
		this.frozen = true;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public Map<Identifier, V> getEntries() {
		return Collections.unmodifiableMap(registryMap);
	}
}

