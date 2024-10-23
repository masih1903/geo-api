package app.daos;

import app.controllers.CountryController;
import app.dtos.CountryDTO;
import app.entities.Country;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CountryDAO implements IDAO<Country> {
    private final EntityManagerFactory emf;
    private final Logger log = LoggerFactory.getLogger(CountryDAO.class);

    public CountryDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Country getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Country.class, id);
        }
    }

    @Override
    public List<Country> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Country c", Country.class).getResultList();
        }
    }

    @Override
    public void create(Country country)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(country);
            em.getTransaction().commit();
        }
    }

    @Override
    public void update(Country country) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(country);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Country country = em.find(Country.class, id);
            em.remove(country);
            em.getTransaction().commit();
        }
    }

    public void saveCountriesToDb(List<CountryDTO> countryDtoList) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            for (CountryDTO countryDTO : countryDtoList) {
                Country country = new Country(countryDTO);
                em.persist(country);
            }
            em.getTransaction().commit();
        }
    }

    public void getCountryByName(String commonName) {
        try (EntityManager em = emf.createEntityManager()) {
            Country country = em.createQuery("SELECT c FROM Country c WHERE c.commonName = :commonname", Country.class)
                    .setParameter("commonname", commonName)
                    .getSingleResult();
            System.out.println(country);
        }
    }

    public List<Country> getCountriesByCurrency(String currencyName) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Country c WHERE c.currencyName = :currencyname", Country.class)
                    .setParameter("currencyname", currencyName)
                    .getResultList();
        }
    }

    public List<Country> getCountriesByLanguage(String language) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT c FROM Country c JOIN c.languages lang WHERE lang = :language_name", Country.class)
                    .setParameter("language_name", language)
                    .getResultList();
        }
    }

    public List<Country> getCountriesByLanguageAndIgnoreOtherLanguages(String language) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT c FROM Country c JOIN c.languages lang WHERE lang = :language_name AND SIZE(c.languages) = 1", Country.class)
                    .setParameter("language_name", "English")
                    .getResultList();
        }
    }

    public List<Country> getCountriesByCapital(String capital) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Country c WHERE :capitals MEMBER OF c.capitals", Country.class)
                    .setParameter("capitals", capital)
                    .getResultList();
        }
    }

    public List<Country> getSpecificRegion(String region)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT c FROM Country c WHERE c.region = :region", Country.class)
                    .setParameter("region", region)
                    .getResultList();
        }
    }

    public List<Country> getTop10HighestPopulation()
    {
        List<Country> countries = getAll();
        return countries.stream()
                .sorted((country2, country1) -> Double.compare(country1.getPopulation(), country2.getPopulation()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Country> getTop10LowestPopulation()
    {
        List<Country> countries = getAll();
        return countries.stream()
                .sorted((country1, country2) -> Double.compare(country1.getPopulation(), country2.getPopulation()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Country> getSpecificDrivingSide(String drivingSide)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT c FROM Country c WHERE c.drivingSide = :drivingSide", Country.class)
                    .setParameter("drivingSide", drivingSide)
                    .getResultList();
        }
    }



}
