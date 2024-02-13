package uk.minersonline.minecart.engine.registry;

public class Identifier {
	private final String namespace;
	private final String path;

	public Identifier(String namespace, String path) {
		this.namespace = namespace;
		this.path = path;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Identifier that = (Identifier) o;

		if (!namespace.equals(that.namespace)) return false;
		return path.equals(that.path);
	}

	@Override
	public int hashCode() {
		int result = namespace.hashCode();
		result = 31 * result + path.hashCode();
		return result;
	}
}
