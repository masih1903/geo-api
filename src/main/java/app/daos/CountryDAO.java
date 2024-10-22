package app.daos;

import app.dtos.CountryDTO;
import app.entities.Country;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CountryDAO implements IDAO<Country>
{
    private final EntityManagerFactory emf;

    public CountryDAO(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    @Override
    public Country getById(Integer id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.find(Country.class, id);
        }
    }

    @Override
    public List<Country> getAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
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
    public void update(Country country)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.merge(country);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Integer id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Country country = em.find(Country.class, id);
            em.remove(country);
            em.getTransaction().commit();
        }
    }

    public void saveCountriesToDb(List<CountryDTO> countryDtoList)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            for (CountryDTO countryDTO : countryDtoList)
            {
                Country country = new Country(countryDTO);
                em.persist(country);
            }
            em.getTransaction().commit();
        }
    }
}
