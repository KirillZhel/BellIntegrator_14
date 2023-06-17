package pages.elements;

import java.util.List;
import java.util.Map;

public class Checker {

    public boolean checkCount(int maxCount, List<Snippet> snippets)
    {
        return true;
    }

    public boolean checkManufacturer(List<String> manufacturerList, List<Snippet> snippets)
    {
        for (Snippet s: snippets) {
            boolean flag = false;
            for (String m: manufacturerList) {
                String name = s.name.toLowerCase();
                String manufacturer = m.toLowerCase();
                if (!name.contains(manufacturer)) {
                    flag = true;
                }
                else {
                    flag = false;
                    break;
                }
            }
            if (!flag)
            {
                return false;
            }
        }

        return true;
    }

    public boolean checkManufacturer(List<String> manufacturerList, Map<String, String> snippets)
    {
        for (Map.Entry<String,String> s: snippets.entrySet()) {
            boolean flag = false;
            for (String m: manufacturerList) {
                String name = s.getValue().toLowerCase();
                String manufacturer = m.toLowerCase();
                if (!name.contains(manufacturer)) {
                    flag = true;
                }
                else {
                    flag = false;
                    break;
                }
            }
            if (!flag)
            {
                return false;
            }
        }

        return true;
    }

    public boolean checkPrice(int minPrice, int maxPrice, List<Snippet> snippets)
    {
        return true;
    }
}
