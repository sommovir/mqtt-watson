/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.db;

import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import it.cnr.istc.mw.mqtt.exceptions.DBAlreadyInstalledException;
import it.cnr.istc.mw.mqtt.exceptions.DBNotExistingException;
import it.cnr.istc.mw.mqtt.logic.logger.LogTitles;
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
        initConnection();
    }
    
    private void initConnection(){
        // configures settings from hibernate.cfg.xml 
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            System.out.print(LogTitles.DATABASE.getTitle() + ConsoleColors.YELLOW_BRIGHT + " checking if db "+ConsoleColors.PURPLE_BRIGHT+"[watsondb]"+ ConsoleColors.YELLOW_BRIGHT +" is already created .." + ConsoleColors.ANSI_RESET);
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            System.out.println(LogTitles.DATABASE.getTitle() + ConsoleColors.ANSI_GREEN + " YES" + ConsoleColors.ANSI_RESET);
//             SessionFactory sessionFactory = new Configuration()
//    .configure("/org/nitish/caller/hibernate.cfg.xml").buildSessionFactory();
        } catch (Exception e) {
            // handle the exception
            System.out.println(LogTitles.DATABASE.getTitle() + ConsoleColors.ANSI_RED + " NO" + ConsoleColors.ANSI_RESET);
            
        }
    }

    public void install() throws DBAlreadyInstalledException, DBNotExistingException {
        System.out.println(LogTitles.DATABASE.getTitle() + ConsoleColors.ANSI_GREEN + " installing database .." + ConsoleColors.ANSI_RESET);
//        System.out.print(LogTitles.DATABASE.getTitle() + ConsoleColors.YELLOW_BRIGHT + " checking if db "+ConsoleColors.PURPLE_BRIGHT+"[watsondb]"+ ConsoleColors.YELLOW_BRIGHT +" is already created .." + ConsoleColors.ANSI_RESET);

        if (sessionFactory == null) {
//            System.out.println(LogTitles.DATABASE.getTitle() + ConsoleColors.ANSI_RED + " NO" + ConsoleColors.ANSI_RESET);
            throw new DBNotExistingException();
        }
        System.out.print(LogTitles.DATABASE.getTitle() + ConsoleColors.YELLOW_BRIGHT + " checking if db is already existing .." + ConsoleColors.ANSI_RESET);
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Laboratory> allLaboratories = session.createQuery("from Laboratory", Laboratory.class).list();

        session.getTransaction().commit();
        session.close();

        if (allLaboratories.size() == 1) {
            System.out.println(ConsoleColors.ANSI_RED + " YES" + ConsoleColors.ANSI_RESET);
            throw new DBAlreadyInstalledException();
        }
        System.out.println(ConsoleColors.ANSI_GREEN + " NO" + ConsoleColors.ANSI_RESET);
        System.out.print(LogTitles.DATABASE.getTitle() + ConsoleColors.YELLOW_BRIGHT + " populating table "+ConsoleColors.CYAN_BOLD+"[Laboratory]"+ConsoleColors.YELLOW_BRIGHT +" .. " + ConsoleColors.ANSI_RESET);
        session = sessionFactory.openSession();
        session.beginTransaction();
        Laboratory labCucina = new Laboratory();
        labCucina.setName("Laboratorio di Cucina");

        session.persist(labCucina);

        session.getTransaction().commit();
        session.close();
        System.out.println(ConsoleColors.ANSI_GREEN + "DONE" + ConsoleColors.ANSI_RESET);

    }

    public List<Laboratory> getAllLaboratories() {
        System.out.println(LogTitles.DATABASE.getTitle() + "fetching data.. [select * from laboratory]");
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Laboratory> result = session.createQuery("from Laboratory", Laboratory.class).list();

        session.getTransaction().commit();
        session.close();

        return result;
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
            System.out.println("Persona: " + person.getName() + " " + person.getSurname());
        });

        session.getTransaction().commit();
        session.close();
    }

}
