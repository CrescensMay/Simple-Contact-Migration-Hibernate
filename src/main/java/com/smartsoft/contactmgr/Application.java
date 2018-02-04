package com.smartsoft.contactmgr;


import com.smartsoft.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    //hold reusable reference to sessionfactory(one)

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        //create a standardServiceRegistry
        final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new Contact.ContactBuilder("Mike", "K")
                .withEmail("crescens.kob@qq.com")
                .withPhone(18240475607L)
                .build();
        int id = save(contact);

        //Display list of contacts
//        for(Contact c : fetchAllContacts()){
//            System.out.println(c);
//        }
        //Display list of contact before the update
        System.out.println("\n\nBefore the update\n\n");
        fetchAllContacts().stream().forEach(System.out::println);

        //Get the persisted contact
        Contact contact1 = findContactById(id);
        //update the contact
//        contact1.setFirstName("Fidele");
        contact1.getFirstName().equals("Carl");
        //persist the changes
        System.out.println("\n\nnUpdating\n\n");
//        update(contact1);
        delete(contact1);
//        delete(contact);
        System.out.println("\n\nUpdate Complete\n\n");
        //display a list of contact
        System.out.println("\n\nAfter the update\n\n");
        fetchAllContacts().stream().forEach(System.out::println);

    }

    private static Contact findContactById(int id){

        //Open session
        Session session = sessionFactory.openSession();
        //retrieve the persistent object(or null if not found)
        Contact contact = session.get(Contact.class,id);
        //close session
        session.close();
        //return the object
        return contact;

    }

    private static void update(Contact contact){
        //Open a session
        Session session = sessionFactory.openSession();
        //Begin a transaction
        session.beginTransaction();
        //Use the session to update the contact
        session.update(contact);
        //commit the transaction
        session.getTransaction().commit();
        //close the session
        session.close();
    }

    private static void delete(Contact contact){

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(contact);
        session.getTransaction().commit();
        session.close();
    }
    private static int save(Contact contact) {
        //Open a session
        Session session = sessionFactory.openSession();
        //Begin a transaction
        session.beginTransaction();
        //Use the session to save the contact
        int id = (int)session.save(contact);
        //Commit the transaction
        session.getTransaction().commit();
        //Close the session
        session.close();

        return id;

    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts(){

        //Open a session
        Session session = sessionFactory.openSession();
        //Create a criteria object
        Criteria criteria = session.createCriteria(Contact.class);
        //get a list of contact object
        List<Contact> contacts = criteria.list();
        //Close a session
        session.close();

        return contacts;
    }
}
