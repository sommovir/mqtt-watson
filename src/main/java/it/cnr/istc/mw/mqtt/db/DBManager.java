/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.db;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author Luca
 */
public class DBManager {

    private static DBManager _instance = null;
    private SessionFactory sessionFactory;

    public static DBManager getInstance() {
        if (_instance == null) {
            _instance = new DBManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private DBManager() {
        super();

// configures settings from hibernate.cfg.xml 
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//             SessionFactory sessionFactory = new Configuration()
//    .configure("/org/nitish/caller/hibernate.cfg.xml").buildSessionFactory();
        } catch (Exception e) {
            // handle the exception
            e.printStackTrace();
        }
    }

    public void test() {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Person p1 = new Person("Luca", "Coraci");
        Person p2 = new Person("Luana", "Mercuri");

        session.persist(p1);
        session.persist(p2);

        List<Person> result = session.createQuery("from Person", Person.class).list();

        result.forEach(person -> {
            System.out.println("Persona: "+person.getName()+" "+person.getSurname());  
        });

        session.getTransaction().commit();
        session.close();
    }

    }
