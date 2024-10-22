package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private ContryRoute countryRoutes = new ContryRoute();

    public EndpointGroup getApiRoutes() {
        return () ->
        {
            path("/countries", countryRoutes.getCountryRoutes());
        };
    }
}
