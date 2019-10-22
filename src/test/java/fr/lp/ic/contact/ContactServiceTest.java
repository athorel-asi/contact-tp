package fr.lp.ic.contact;

import fr.lp.ic.contact.exception.ContactException;
import fr.lp.ic.contact.exception.ContactNotFoundException;
import org.junit.Assert;
import org.junit.Test;


public class ContactServiceTest {

    private static final String VALID_PHONE_NUMBER = "0225457845";
    private static final String VALID_EMAIL = "test@yopmail.com";
    private ContactService service = new ContactService();

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailedIfNameLessThanThree() throws ContactException {
        service.newContact("ab", VALID_PHONE_NUMBER, VALID_EMAIL);
    }

    @Test
    public void shouldFailedIfNameLessThanThreeSyntax() {
        try {
            service.newContact("ab", VALID_PHONE_NUMBER, VALID_EMAIL);
            Assert.fail("Should have IllegalArgumentException");
        } catch (Throwable e) {
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailedIfNameGreaterThanForty() throws ContactException {
        service.newContact("abcdefghijklmnopqrstuvwxyzabcdefghaijklma", VALID_PHONE_NUMBER, VALID_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailedIfNameIsNull() throws ContactException {
        service.newContact(null, VALID_PHONE_NUMBER, VALID_EMAIL);
    }

    @Test(expected = ContactException.class)
    public void shouldFailedIfNameAlreadyExists() throws ContactException {
        service.newContact("Arnaud", VALID_PHONE_NUMBER, VALID_EMAIL);
        service.newContact("Arnaud", VALID_PHONE_NUMBER, VALID_EMAIL);

    }

    @Test
    public void shouldInsertValidContact() throws ContactException {
        service.newContact("Arnaud", VALID_PHONE_NUMBER, VALID_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseExceptionIfNameIsNull() throws ContactNotFoundException {
        service.deleteContact(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseExceptionIfNameIsNullUpdate() throws ContactNotFoundException, ContactException {
        service.updateContact(null, "", "", "");
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseExceptionIfNewNameLengthIsShort() throws ContactNotFoundException, ContactException {
        service.updateContact("Arnaud", "ab", "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseExceptionIfNewNameLengthIsTooLong() throws ContactNotFoundException, ContactException {
        service.updateContact("Arnaud", "abcdefghijklmnopqrstuvwxyzabcdefghaijklma", "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseExceptionIfNewNameIsNull() throws ContactNotFoundException, ContactException {
        service.updateContact("Arnaud", null, "", "");
    }

}
