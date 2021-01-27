package fr.lp.ic.contact.evaluators;

public class TotoEvaluator implements IEvaluator{

    public static final int LENGHT = 25;

    @Override
    public boolean accept(String key) {
        return "Toto".equals(key);
    }

    @Override
    public String evaluate(String value) {
        return (value.length() > LENGHT) ? value.substring(0, LENGHT -1) : value;
    }
}
