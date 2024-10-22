package app.daos;

import app.config.HibernateConfig;
import app.entities.Country;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountryDAOTest {

    static EntityManagerFactory emf;
    static CountryDAO countryDAO;
    static Country c1, c2, c3;


    @BeforeAll
    static void setUpAll() {

        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryConfigTest();
        countryDAO = new CountryDAO(emf);

    }

    @BeforeEach
    void setUp() {

        c1 = new Country("USA", "United States of America", "Dollar",
                "$", "North America", 331449281, List.of("Washington D.C."),
                "right", List.of("USA"), Map.of("en", "English"));

        c2 = new Country("Denmark", "Denmark", "Dasnke kroner",
                "DKK", "Europe", 600000, List.of("Copenhagen"),
                "right", List.of("DK"), Map.of("da", "Danish"));

        c3 = new Country("Mexico", "United Mexican States", "Mexican Peso",
                "$", "North America", 128932753, List.of("Mexico City"),
                "right", List.of("MEX"), Map.of("es", "Spanish"));


            countryDAO.create(c1);
            countryDAO.create(c2);
            countryDAO.create(c3);
    }

    @AfterEach
    void tearDown() {

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Country").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE country_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getById() {

            Country country = countryDAO.getById(1L);
            assertEquals(c1.getId(), country.getId());
    }

    @Test
    void getAll() {

        List<Country> countries = countryDAO.getAll();
        assertEquals(3, countries.size());
    }

    @Test
    void create() {

        Country country = new Country("Canada", "Canada", "Canadian Dollar",
                "$", "North America", 37742154, List.of("Ottawa"),
                "right", List.of("CAN"), Map.of("en", "English"));

        countryDAO.create(country);
        assertEquals(4, country.getId());
    }

    @Test
    void update() {

        c1.setCommonName("United States of America");
        countryDAO.update(c1);
        assertEquals("United States of America", countryDAO.getById(c1.getId()).getCommonName());
    }

    @Test
    void delete() {

            countryDAO.delete(1L);
            assertNull(countryDAO.getById(1L));
    }
}