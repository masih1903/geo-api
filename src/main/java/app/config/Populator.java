package app.config;

import app.daos.CountryDAO;
import app.dtos.CountryDTO;
import app.entities.Country;
import app.security.entities.Role;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Populator
{

    private static EntityManagerFactory emf;
    private static CountryDAO countryDao;

    public Populator(EntityManagerFactory emf, CountryDAO countryDao)
    {
        Populator.emf = emf;
        Populator.countryDao = countryDao;
    }

    public List<CountryDTO> populateCountries()
    {
        if (emf == null)
        {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // Create country objects
            Country denmark = new Country("DK", true,
                    "Denmark", "The Kingdom of Denmark", "Danish Krone", "kr", "Europe", 5831404L,
                    List.of("Copenhagen"), "right", List.of("DK"), Map.of("da", "Danish"), "flag1");

            Country japan = new Country("JP", true,
                    "Japan", "Japan", "Yen", "¥", "Asia", 125960000L,
                    List.of("Tokyo"), "left", List.of("J"), Map.of("ja", "Japanese"), "flag1");

            Country usa = new Country("US", true,
                    "United States", "United States of America", "US Dollar", "$", "Americas", 331893745L,
                    List.of("Washington, D.C."), "right", List.of("USA"), Map.of("en", "English"), "flag1");

            // Persisting countries to the database
            em.persist(denmark);
            em.persist(japan);
            em.persist(usa);

            em.getTransaction().commit();

            return new ArrayList<>(List.of(new CountryDTO(denmark), new CountryDTO(japan), new CountryDTO(usa)));
        }
    }

    public static UserDTO[] populateUsers()
    {

        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);

        if (emf == null)
        {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }

    public void cleanUp()
    {
        if (emf == null)
        {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createQuery("DELETE FROM Country").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE country_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
