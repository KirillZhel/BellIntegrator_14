package models;

import java.util.Objects;

/**
 * Класс карточки товара.
 * @author Кирилл Желтышев
 */
public class Snippet {
    /**
     * Название товара.
     * @author Кирилл Желтышев
     */
    public String name;
    /**
     * Цена товара.
     * @author Кирилл Желтышев
     */
    public int price;

    /**
     * Конструктор класса.
     * @author Кирилл Желтышев
     * @param name Название товара
     * @param price Цена товара
     */
    public Snippet(String name, int price)
    {
        this.name = name;
        this.price = price;
    }

    /**
     * Метод сравнения двух экземпяров карточек.
     * @author Кирилл Желтышев
     * @param o объект для сравнения
     * @return true - в случае, если объекты являются эквивалентными, false - в обратном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snippet snippet = (Snippet) o;
        return Objects.equals(name, snippet.name) && Objects.equals(price, snippet.price);
    }

    /**
     * Метод создания хеш-кода объекта карточки.
     * @author Кирилл Желтышев
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
