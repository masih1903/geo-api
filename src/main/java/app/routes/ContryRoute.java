package app.routes;

import app.config.HibernateConfig;
import app.controllers.CountryController;
import app.daos.CountryDAO;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ContryRoute
{
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final CountryDAO countryDao = new CountryDAO(emf);
    private final CountryController countryController = new CountryController(countryDao);

    public EndpointGroup getCountryRoutes()
    {
        return () ->
        {
            get("/{id}", countryController::getById, Role.ANYONE);
            get("/", countryController::getAll, Role.ANYONE);
            post("/", countryController::create, Role.ANYONE);
            put("/{id}", countryController::update, Role.ANYONE);
            delete("/{id}", countryController::delete, Role.ANYONE);
        };
    }
}
