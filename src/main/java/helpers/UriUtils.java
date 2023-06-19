package helpers;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс, содержпщий дополнительные методы работы с url
 * @author Кирилл Желтышев
 */
public class UriUtils {
    /**
     * Метод добавления новых параметров в url
     * @author Кирилл Желтышев
     * @param url базовый url
     * @param parameters новые параметры
     * @return новый url
     */
    public static String addQueryParameters(String url, Map<String, String> parameters) {
        try {
            List<NameValuePair> params = parameters.entrySet().stream()
                    .map(e -> new BasicNameValuePair(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
            return new URIBuilder(url)
                    .addParameters(params)
                    .build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Не удалось спарсить url", e);
        }
    }
}
