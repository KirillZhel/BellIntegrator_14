package pages.elements;

import java.util.Objects;

public class Snippet {
    public String name;
    public int price;

    public Snippet(String name, int price)
    {
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snippet snippet = (Snippet) o;
        return Objects.equals(name, snippet.name) && Objects.equals(price, snippet.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
