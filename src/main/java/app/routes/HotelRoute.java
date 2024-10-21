package app.routes;

import app.config.HibernateConfig;
import app.controllers.HotelController;
import app.daos.HotelDAO;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class HotelRoute {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final HotelDAO hotelDao = new HotelDAO(emf);
    private final HotelController hotelController = new HotelController(hotelDao);


    public EndpointGroup getHotelRoutes() {
        return () ->
        {
            get("/{id}", hotelController::getById, Role.ANYONE);
            get("/", hotelController::getAll, Role.ANYONE );
            post("/", hotelController::create, Role.ADMIN);
            put("/{id}", hotelController::update, Role.ADMIN);
            delete("/{id}", hotelController::delete, Role.ADMIN);
        };
    }
}
