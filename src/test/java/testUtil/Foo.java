package testUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Foo {
    private Bar bar;

    public Foo() {
        this.bar = new Bar();
    }

    public String getString() {
        //kind of hard logic, etc
        bar.saveAndNotify("fooBar");
        return "fooBar";
    }

    public String getProperty(String paramName) {
        switch (paramName) {
            case "cat":
                return "CAT_PARAM";
            case "dog":
                return "DOG_PARAM";
            default:
                bar.saveAndNotify(paramName + " has default implementation");
                return paramName.toUpperCase();
        }
    }

    public List<String> filterProductsIds(String regexp) {
        List<String> list = new ArrayList<>();
        list.add("GDT-184");
        list.add("GDT-185");
        list.add("GDT-186");
        list.add("CFC-18090");
        list.add("CFT-18023");
        list.add("TTF-0345");

        //GDT-\d+

        return list.stream()
                .filter(product -> product.matches(regexp))
                .collect(Collectors.toList());
    }
}
