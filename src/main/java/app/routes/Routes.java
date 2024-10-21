package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private HotelRoute hotelRoutes = new HotelRoute();

    public EndpointGroup getApiRoutes() {
        return () ->
        {
            path("/hotels", hotelRoutes.getHotelRoutes());


        };
    }
}
