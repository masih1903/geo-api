package app;

import app.config.HibernateConfig;
import app.daos.CountryDAO;
import app.dtos.CountryDTO;
import app.entities.Country;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountryPopulator
{

    private static EntityManagerFactory emf;
    private static CountryDAO countryDao;

    public CountryPopulator(EntityManagerFactory emf, CountryDAO countryDao) {
        CountryPopulator.emf = emf;
        CountryPopulator.countryDao = countryDao;
    }

    public List<CountryDTO> populateCountries() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create country objects
            Country denmark = new Country(
                "Denmark", "The Kingdom of Denmark", "Danish Krone", "kr", "Europe", 5831404L,
                List.of("Copenhagen"), "right", List.of("DK"), Map.of("da", "Danish"));

            Country japan = new Country(
                    "Japan", "Japan", "Yen", "¥", "Asia", 125960000L,
                    List.of("Tokyo"), "left", List.of("J"), Map.of("ja", "Japanese"));

            Country usa = new Country(
                    "United States", "United States of America", "US Dollar", "$", "Americas", 331893745L,
                    List.of("Washington, D.C."), "right", List.of("USA"), Map.of("en", "English"));

            // Persisting countries to the database
            em.persist(denmark);
            em.persist(japan);
            em.persist(usa);

            em.getTransaction().commit();

            return new ArrayList<>(List.of(new CountryDTO(denmark), new CountryDTO(japan), new CountryDTO(usa)));
        }
    }

    public void cleanUpCountries() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Country").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE country_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
