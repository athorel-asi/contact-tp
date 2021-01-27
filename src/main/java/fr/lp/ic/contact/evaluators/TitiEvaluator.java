package fr.lp.ic.contact.evaluators;

import java.util.Locale;

public class TitiEvaluator implements IEvaluator {


    @Override
    public boolean accept(String key) {
        return "Titi".equals(key);
    }

    @Override
    public String evaluate(String value) {
        return value.toUpperCase(Locale.ROOT);
    }
}
