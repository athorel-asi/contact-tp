package fr.lp.ic.contact;

import fr.lp.ic.contact.dao.ContactDaoImpl;
import fr.lp.ic.contact.dao.IContactDao;
import fr.lp.ic.contact.exception.ContactException;
import fr.lp.ic.contact.exception.ContactNotFoundException;
import fr.lp.ic.contact.model.Contact;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Service - Méthodes a développer ici
 *
 * @author athorel
 */
public class ContactService {

    private static final int MIN_NAME_CHARS = 3;
    private static final int MAX_NAME_CHARS = 40;
    private static final int MAX_WAIT_LIST_TIME = 3;
    // Ne pas bouger
    private IContactDao contactDao = new ContactDaoImpl();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * Développer ici la méthode qui retourne une liste de contact, trié par le nom
     *
     * @return list des contact triés
     */
    public List<String> listAll() {
        try {
            return executorService.submit(() ->
                    contactDao.findAll()
                            .stream()
                            .map(Contact::getName)
                            .collect(Collectors.toList())
            ).get(MAX_WAIT_LIST_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new IllegalStateException("Search is too long please limit your search");
        }
    }

    /**
     * Développer ici la méthode permettant d'afficher le nombre de contact
     *
     * @return nombre de contact
     */
    public int count() {
        return contactDao.findAll().size();
    }

    /**
     * Développer ici la méthode permettant d'ajouter un nouveau contact
     *
     * @param name        le nom doit être compris entre 3 et 40 caractéres
     * @param phoneNumber le numéro de téléphone doit commencer par 02 et contenir
     *                    10 chiffres
     * @param email       l'email doit contenir au mois 5 caractéres et avoir un @
     *                    et un .
     * @throws ContactException Le nom doit être unique, si il est déjà existant on
     *                          lève une ContactException
     */
    public void newContact(String name, String phoneNumber, String email) throws ContactException {

        if (name == null || name.length() < MIN_NAME_CHARS || name.length() > MAX_NAME_CHARS) {
            throw new IllegalArgumentException(
                    "Name should be a valid name from "
                            + MIN_NAME_CHARS + " to " + MAX_NAME_CHARS + " chars"
            );
        }

        Optional<Contact> contactFound = contactDao.findByName(name);
        if (contactFound.isPresent()) {
            throw new ContactException();
        }

        Contact contact = new Contact();
        contact.setName(name);
        contact.setEmail(email);
        contact.setPhone(phoneNumber);
        contactDao.save(contact);

    }

    /**
     * Développer ici la méthode permettant de mettre à jour un contact.
     *
     * @param name        le nom doit être compris entre 3 et 40 caractères
     * @param phoneNumber le numéro de téléphone doit commencer par 02 et contenir
     *                    10 chiffres
     * @param email       l'email doit contenir au mois 5 caractéres et avoir un @
     *                    et un .
     * @throws ContactException         Le nom doit être unique, si il est déjà
     *                                  existant on lève une ContactException
     * @throws ContactNotFoundException Si l'utilisateur n'existe pas on lève une
     *                                  ContactNotFoundException
     */
    public void updateContact(String name, String newName, String phoneNumber, String email)
            throws ContactException, ContactNotFoundException {

        if (name == null) {
            throw new IllegalArgumentException("name can't be null");
        }

        if (newName == null || newName.length() < MIN_NAME_CHARS || newName.length() > MAX_NAME_CHARS) {
            throw new IllegalArgumentException("newName should be ok");
        }

        //Raise an exception if contactName not exists
        contactDao.findByName(name)
                .orElseThrow(ContactNotFoundException::new);

        //If name has changed, check for conflicts
        if (!name.equalsIgnoreCase(newName) && contactDao.findByName(newName).isPresent()) {
            throw new ContactException();
        }

        Contact contact = new Contact();
        contact.setName(newName);
        contact.setPhone(phoneNumber);
        contact.setEmail(email);

        contactDao.update(name, contact);

    }

    /**
     * Développer ici la méthode permettant de supprimer un contact
     *
     * @param name le nom de l'utilisateur a supprimer
     * @throws ContactNotFoundException Si l'utilisateur n'existe pas on lève une
     *                                  ContactNotFoundException
     */
    public void deleteContact(String name) throws ContactNotFoundException {

        if (name == null) {
            throw new IllegalArgumentException("name can't be null");
        }
        //Call Java 8
        //contactDao.findByName(name)
        //      .orElseThrow(ContactNotFoundException::new);

        //Call Avant Java 8
        Optional<Contact> byName = contactDao.findByName(name);
        if (!byName.isPresent()) {
            throw new ContactNotFoundException();
        }
        //delete
        contactDao.delete(name);
    }

}
