/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.db;

import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import it.cnr.istc.mw.mqtt.exceptions.DBAlreadyInstalledException;
import it.cnr.istc.mw.mqtt.exceptions.DBBadParamaterException;
import it.cnr.istc.mw.mqtt.exceptions.DBLoginException;
import it.cnr.istc.mw.mqtt.exceptions.DBNotExistingException;
import it.cnr.istc.mw.mqtt.exceptions.DBUniqueViolationException;
import it.cnr.istc.mw.mqtt.exceptions.MindGameException;
import it.cnr.istc.mw.mqtt.logic.logger.LogTitles;
import java.sql.SQLIntegrityConstraintViolationException;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Department;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Product;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameInstance;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameType;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.MindGame;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Luca 2
 */
public class DBManager {
    //try

    private static DBManager _instance = null;
    private SessionFactory sessionFactory;
    private boolean installed = false;
    private Person currentUser = null;

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

    public Person login(String username, String password) throws DBLoginException {
        this.currentUser = new Person("fantoccio", "super", "fantoccio", "fantapassowrd");
        return this.currentUser;
    }

    public boolean isInstalled() {
        return installed;
    }
    
    private void initConnection() {
    
        // configures settings from hibernate.cfg.xml 
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            System.out.print(LogTitles.DATABASE.getTitle() + ConsoleColors.YELLOW_BRIGHT + " checking if db " + ConsoleColors.PURPLE_BRIGHT + "[watsondb]" + ConsoleColors.YELLOW_BRIGHT + " is already created .." + ConsoleColors.ANSI_RESET);
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            System.out.println(LogTitles.DATABASE.getTitle() + ConsoleColors.ANSI_GREEN + " YES" + ConsoleColors.ANSI_RESET);
            installed = true;
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
        System.out.print(LogTitles.DATABASE.getTitle() + ConsoleColors.YELLOW_BRIGHT + " populating table " + ConsoleColors.CYAN_BOLD + "[Laboratory]" + ConsoleColors.YELLOW_BRIGHT + " .. " + ConsoleColors.ANSI_RESET);
        session = sessionFactory.openSession();
        session.beginTransaction();
        Laboratory labCucina = new Laboratory();
        labCucina.setName("Laboratorio di Cucina");

        session.persist(labCucina);

        session.getTransaction().commit();
        session.close();
        System.out.println(ConsoleColors.ANSI_GREEN + "DONE" + ConsoleColors.ANSI_RESET);

    }

    /**
     * Restituisce tutt l'elenco dei laboratori presenti nel database
     *
     * @return
     */
    public List<Laboratory> getAllLaboratories() {
        System.out.println(LogTitles.DATABASE.getTitle() + "fetching data.. [select * from laboratory]");
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Laboratory> result = session.createQuery("from Laboratory", Laboratory.class).list();

        session.getTransaction().commit();
        session.close();

        return result;
    }

    /**
     * Crea un laboratorio nel Database. Il nome del db è unico e non possono
     * esistere due laboratori con lo stesso nome.
     *
     * @param name il nome del laboratorio
     * @throws DBUniqueViolationException se il nome è già esistente nel
     * database viene lanciata una DBUniqueViolationException
     * @throws DBBadParamaterException Se il parametro del metodo è nullo o
     * vuoto.
     */
    public Laboratory createLab(String name) throws DBUniqueViolationException, DBBadParamaterException {
        if (name == null) {
            throw new DBBadParamaterException("name", DBBadParamaterException.ErrorType.NULL);
        }
        if (name.isEmpty()) {
            throw new DBBadParamaterException("name", DBBadParamaterException.ErrorType.EMPTY);
        }
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Laboratory lab = new Laboratory();
        lab.setName(name);
        try {
            session.persist(lab);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                throw new DBUniqueViolationException("Laboratory.name");
            }
            System.out.println("EXCEPTION CAUSE = " + ex.getCause().getClass().getCanonicalName());
            ex.printStackTrace();
        }
        session.getTransaction().commit();
        session.close();
        return lab;
    }

    /**
     * Permette di modificare sul database un istanza di Laboratorio
     *
     * @param lab L'istanza da modificare e persistere anche sul database.
     * @throws DBUniqueViolationException Se il nuovo nome del laboratorio è già
     * in uso.
     * @throws DBBadParamaterException Se il nome modificato è nullo o vuoto.
     */
    public void editLab(Laboratory lab) throws DBUniqueViolationException, DBBadParamaterException {
            
    }

    /**
     * Restituisce il laboratorio con l'id in argomento.
     *
     * @param id l'id del laboratorio
     * @return Il laboratorio se l'id è esistente, null viceversa o nel caso di
     * id negativi
     */
    public Laboratory getLaboratoryByID(long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Laboratory found = session.find(Laboratory.class, id);

        session.getTransaction().commit();
        session.close();
        
        return found;
    }

    public void test() {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Person p1 = new Person("Luca", "Coraci", "sommovir", "password1");
        Person p2 = new Person("Luana", "Mercuri", "lulu", "password2");

        Laboratory lab = new Laboratory();
        lab.setName("Laboratorio di Lettura");
        Laboratory lab2 = new Laboratory();
        lab2.setName("Laboratorio di Filatelia");
        Laboratory lab3 = new Laboratory();
        lab3.setName("Laboratorio di Informatica");

        session.persist(lab);
        session.persist(lab2);
        session.persist(lab3);

        p1.addLaboratory(lab3);
        p2.addLaboratory(lab);
        p2.addLaboratory(lab2);

        session.persist(p1);
        session.persist(p2);

        Person p3 = new Person("Alfonso", "Alfonsini", "alfio", "alfio");
        Laboratory lab4 = new Laboratory();
        lab4.setName("Laboratorio da rimuovere");
        p3.addLaboratory(lab4);

        session.persist(p3);

        List<Person> result = session.createQuery("from Person", Person.class).list();

        result.forEach(person -> {
            System.out.println("Persona: " + person.getName() + " " + person.getSurname());
        });

        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();

        Room room = new Room("Cucina");
        Room room1 = new Room("Cesso");

        House h1 = new House("Casa mia", "Via bamboccioni 33");
        h1.addRoom(room);
        h1.addRoom(room1);

        session.persist(h1);

        session.getTransaction().commit();
        session.close();

        JOptionPane.showMessageDialog(null, "sidasdj");

        session = sessionFactory.openSession();
        session.beginTransaction();

        session.remove(h1);
        session.getTransaction().commit();
        session.close();

    }

    /**
     * Ritorna l'id di un laboratorio che ha come nome il nome passato in
     * argomento
     *
     * @param name
     * @return ritorna l'id del laboratorio se viene trovato, -1 viceversa o se
     * l'input è errato.
     */
    public long getLabIdByName(String name) {
        Laboratory laboratory = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        laboratory = session.byNaturalId(Laboratory.class)
                .using("name", name)
                .load();

        session.getTransaction().commit();
        session.close();

        return laboratory == null ? -1 : laboratory.getId();

    }

    public void deleteLaboratory(long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Laboratory spacciato = new Laboratory();
        spacciato.setId(id);
        session.delete(spacciato);

        session.getTransaction().commit();
        session.close();
    }

    public void deletePerson(long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Person spacciato = new Person();
        spacciato.setId(id);
        session.delete(spacciato);

        session.getTransaction().commit();
        session.close();
    }

    //MOCKUP
    public List<Product> getAllProducts() {
        List<Product> prodotti = new LinkedList<Product>();
        try {

            prodotti.add(new Product(0, "Carota", new Department(0, "Verdura"), "una "));
            prodotti.add(new Product(1, "Cetriolo", new Department(10, "Verdura"), "un "));
            prodotti.add(new Product(2, "Salsiccia", new Department(1, "Carne"), "una "));
            prodotti.add(new Product(3, "Spaghetti", new Department(2, "Pasta"), "un pacco di "));
            prodotti.add(new Product(4, "Ciabattina", new Department(3, "Pane"), "una "));
            prodotti.add(new Product(5, "Ceci", new Department(4, "Legumi "), "alcuni "));
            prodotti.add(new Product(6, "Bistecca", new Department(1, "Carne"), "una "));
            prodotti.add(new Product(7, "Salmone", new Department(5, "Pesce"), "del"));
            prodotti.add(new Product(8, "Bastoncini Findus", new Department(5, "Surgelati"), "i "));
            prodotti.add(new Product(9, "Riso", new Department(5, "Pasta"), "il "));
            prodotti.add(new Product(10, "Petto di pollo", new Department(5, "Carne"), "del "));
            
            return prodotti;
        } catch (MindGameException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    //ce ne devono essere almeno 4 se no esplode tutto. 
    public List<Department> getAllDepartments() {
        List<Department> reparti = new LinkedList<Department>();
        reparti.add(new Department(1, "frutta"));
        reparti.add(new Department(2, "sport"));
        reparti.add(new Department(3, "Pesce"));
        reparti.add(new Department(4, "Surgelati"));
        return reparti;
    }

    public <G extends MindGame> List<GameInstance<G>> getLast5GameInstances(Person user, G mindGame) {

        return null;
    }

    public Person getCurrentUser() {
        return this.currentUser;
    }

}
