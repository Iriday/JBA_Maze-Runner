package maze;

import java.util.Objects;

public class Tuple<T, E> {
    private final T a;
    private final E b;

    public Tuple(T a, E b) {
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return a;
    }

    public E getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(getA(), tuple.getA()) &&
                Objects.equals(getB(), tuple.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getA(), getB());
    }
}
