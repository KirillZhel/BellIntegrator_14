package helpers;

import org.aeonbits.owner.ConfigFactory;

/**
 * Класс, содержащий экземпляры объектов с глобальными свойствами.
 * @author Кирилл Желтышев
 */
public class Properties {
    /**
     * Экземпляр класса, содержащий глобальные свойства из tests.properties.
     * @author Кирилл Желтышев
     */
    public static TestProperties testProperties = ConfigFactory.create(TestProperties.class);
}
