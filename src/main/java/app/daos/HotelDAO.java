package app.daos;

import app.entities.Hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HotelDAO implements IDAO<Hotel> {

    private final EntityManagerFactory emf;

    public HotelDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public Hotel getById(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Hotel.class, id);
        }


    }

    @Override
    public List<Hotel> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Hotel> query = em.createQuery("SELECT h FROM Hotel h", Hotel.class);
            return query.getResultList();
        }
    }

    @Override
    public void create(Hotel hotel) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(hotel);
            em.getTransaction().commit();
        }

    }

    @Override
    public void update(Hotel hotel) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(hotel);
            em.getTransaction().commit();
        }

    }

    @Override
    public void delete(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, id);
            em.remove(hotel);
            em.getTransaction().commit();
        }

    }
}
