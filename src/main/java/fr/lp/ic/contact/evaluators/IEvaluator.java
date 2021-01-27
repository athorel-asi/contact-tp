package fr.lp.ic.contact.evaluators;

public interface IEvaluator {

    boolean accept(String key);

    String evaluate(String value);
}
