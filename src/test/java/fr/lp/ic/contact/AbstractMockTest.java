package fr.lp.ic.contact;

import org.easymock.*;
import org.junit.Rule;

public abstract class AbstractMockTest extends EasyMockSupport {

    @Rule
    public EasyMockRule rule = new EasyMockRule(this);

    static <T> IExpectationSetters<T> expect(T value) {
        return EasyMock.expect(value);
    }

    static <T> Capture<T> newCapture() {
        return EasyMock.newCapture();
    }

    static <T> T capture(Capture<T> captured) {
        return EasyMock.capture(captured);
    }

}
