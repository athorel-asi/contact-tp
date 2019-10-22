package fr.lp.ic.contact;

import fr.lp.ic.contact.dao.IContactDao;
import fr.lp.ic.contact.exception.ContactException;
import fr.lp.ic.contact.exception.ContactNotFoundException;
import fr.lp.ic.contact.model.Contact;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactServiceMockTest extends BaseMockTest {

    private static final String VALID_PHONE_NUMBER = "0254414512";
    private static final String VALID_EMAIL = "test@yopmail.com";
    @TestSubject
    private ContactService contactService = new ContactService();

    @Mock
    private IContactDao contactDao;

    @Test(expected = ContactException.class)
    public void shouldFailIfNameAlreadyExists() throws ContactException {
        String name = "Arnaud";
        // Enregistrement des comportements
        expect(contactDao.findByName(name))
                .andReturn(Optional.of(new Contact()));

        // Fin de l'enregistrement
        replayAll();

        contactService.newContact(name, "0254414512", VALID_EMAIL);
    }

    @Test
    public void shouldInsertElementExists() throws ContactException {
        String name = "Arnaud";
        // Enregistrement des comportements
        expect(contactDao.findByName(name))
                .andReturn(Optional.empty());
        Capture<Contact> capturedContact = newCapture();

        expect(contactDao.save(capture(capturedContact)))
                .andReturn(true);

        // Fin de l'enregistrement
        replayAll();

        contactService.newContact(name, VALID_PHONE_NUMBER, VALID_EMAIL);
        Contact value = capturedContact.getValue();
        Assert.assertEquals(name, value.getName());
        Assert.assertEquals("Phone error", VALID_PHONE_NUMBER, value.getPhone());
        Assert.assertEquals("email error", VALID_EMAIL, value.getEmail());
    }

    @Test(expected = ContactNotFoundException.class)
    public void shouldFailDeletionIfNameDoesntExist() throws ContactNotFoundException {
        String name = "Arnaud";
        //Record
        EasyMock.expect(contactDao.findByName(name))
                .andReturn(Optional.empty());
        //End Record
        EasyMock.replay(contactDao);
        //Test
        contactService.deleteContact(name);
        //Assertions
    }

    @Test
    public void shouldDeleteIfNameExists() throws ContactNotFoundException {
        String name = "Arnaud";
        //Record
        EasyMock.expect(contactDao.findByName(name))
                .andReturn(Optional.of(new Contact()));
        EasyMock.expect(contactDao.delete(name)).andReturn(true);
        //End Record
        EasyMock.replay(contactDao);
        //Test
        contactService.deleteContact(name);
    }


    @Test
    public void shouldListContact() {

        //Record
        EasyMock.expect(contactDao.findAll()).andReturn(new ArrayList<>());
        //End Record
        EasyMock.replay(contactDao);
        //Test
        List<String> strings = contactService.listAll();
        System.out.println(strings);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldCancelCallTimeout() {

        //Record
        EasyMock.expect(contactDao.findAll()).andAnswer(() -> {
            Thread.sleep(3100);
            return new ArrayList<>();
        });
        //End Record
        EasyMock.replay(contactDao);
        //Test
        List<String> strings = contactService.listAll();
        Assert.assertEquals(0, strings.size());

    }

    //
    //---------------------Update ----------------//
    @Test(expected = ContactNotFoundException.class)
    public void shouldFailUpdateWhenContactNotExists() throws ContactNotFoundException, ContactException {
        String name = "Arnaud";
        expect(contactDao.findByName(name)).andReturn(Optional.empty());
        replayAll();

        contactService.updateContact(name, name, VALID_PHONE_NUMBER, VALID_EMAIL);
    }

    @Test(expected = ContactException.class)
    public void shouldFailUpdateWhenNewContactExists() throws ContactNotFoundException, ContactException {
        String name = "Arnaud";
        String newName = "George";
        expect(contactDao.findByName(name)).andReturn(Optional.of(new Contact()));
        expect(contactDao.findByName(newName)).andReturn(Optional.of(new Contact()));

        replayAll();

        contactService.updateContact(name, newName, VALID_PHONE_NUMBER, VALID_EMAIL);
    }

    @Test
    public void shouldPassUpdate() throws ContactNotFoundException, ContactException {
        String name = "Arnaud";
        String newName = "George";
        expect(contactDao.findByName(name)).andReturn(Optional.of(new Contact()));
        expect(contactDao.findByName(newName)).andReturn(Optional.empty());
        Capture<Contact> captured = newCapture();
        expect(contactDao.update(EasyMock.eq(name), capture(captured))).andReturn(true);

        replayAll();

        contactService.updateContact(name, newName, VALID_PHONE_NUMBER, VALID_EMAIL);

        Contact value = captured.getValue();
        Assert.assertEquals(newName, value.getName());
        Assert.assertEquals(VALID_PHONE_NUMBER, value.getPhone());
        Assert.assertEquals(VALID_EMAIL, value.getEmail());

    }

}
