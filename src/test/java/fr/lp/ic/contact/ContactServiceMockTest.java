package fr.lp.ic.contact;

import fr.lp.ic.contact.dao.IContactDao;
import fr.lp.ic.contact.exception.ContactException;
import fr.lp.ic.contact.exception.ContactNotFoundException;
import fr.lp.ic.contact.model.Contact;
import org.easymock.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactServiceMockTest extends BaseMockTest {

    @TestSubject
    ContactService contactService = new ContactService();

    @Mock
    IContactDao contactDao;

    @Test(expected = ContactException.class)
    public void shouldFailIfNameAlreadyExists() throws ContactException {
        String name = "Arnaud";
        // Enregistrement des comportements
        expect(contactDao.findByName(name))
                .andReturn(Optional.of(new Contact()));

        // Fin de l'enregistrement
        replayAll();

        contactService.newContact(name, "0254414512", "test@yopmail.com");
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

        contactService.newContact(name, "0254414512", "test@yopmail.com");
        Contact value = capturedContact.getValue();
        Assert.assertEquals(name, value.getName());
        Assert.assertEquals("Phone error", "0254414512", value.getPhone());
        Assert.assertEquals("email error", "test@yopmail.com", value.getEmail());
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
    public void shouldShowSomeEasyMockSamples() throws ContactNotFoundException {
        String name = "Arnaud";
        //Record
        EasyMock.expect(contactDao.findByName(name)).andAnswer(new IAnswer<Optional<Contact>>() {
            @Override
            public Optional<Contact> answer() throws Throwable {
                Thread.sleep(3000);
                return Optional.empty();
            }
        });
        //End Record
        EasyMock.replay(contactDao);
        //Test
        contactService.deleteContact(name);
    }

    @Test
    public void shouldListContact() throws ContactNotFoundException {

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
        EasyMock.expect(contactDao.findAll()).andAnswer(new IAnswer<List<Contact>>() {
            @Override
            public List<Contact> answer() throws Throwable {
                Thread.sleep(3100);
                return new ArrayList<>();
            }
        });
        //End Record
        EasyMock.replay(contactDao);
        //Test
        List<String> strings = contactService.listAll();

    }

}
