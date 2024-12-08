package app.config;

import app.controllers.ExceptionController;
import app.exceptions.ApiException;
import app.routes.Routes;
import app.security.controllers.AccessController;
import app.security.enums.Role;
import app.security.routes.SecurityRoutes;
import app.utils.ApiProps;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AppConfig {

    private static final Routes routes = new Routes();
    private static final ExceptionController exceptionController = new ExceptionController();
    private static final AccessController accessController = new AccessController();

    private static void configuration(JavalinConfig config) {

        config.router.contextPath = ApiProps.API_CONTEXT;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.bundledPlugins.enableDevLogging();
        config.router.apiBuilder(routes.getApiRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());

    }

    private static void exceptionContext(Javalin app) {

        app.exception(ApiException.class, exceptionController::apiExceptionHandler);
        app.exception(Exception.class, exceptionController::exceptionHandler);


    }

    // == CORS Configuration ==
    private static void corsHeaders(Context ctx) {
        String origin = ctx.header("Origin"); // Hent 'Origin' fra anmodningen
        if (origin != null && (origin.equals("http://localhost:5173") || origin.equals("https://atlasapi.ut-cphb.dk"))) {
            ctx.header("Access-Control-Allow-Origin", origin); // Sæt oprindelsen dynamisk
            ctx.header("Vary", "Origin"); // Tilføj dette for bedre browser-cache håndtering
        }
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true"); // Tillad credentials (valgfrit)
    }

    private static void corsHeadersOptions(Context ctx) {
        String origin = ctx.header("Origin"); // Hent 'Origin' fra anmodningen
        if (origin != null && (origin.equals("http://localhost:5173") || origin.equals("https://atlasapi.ut-cphb.dk"))) {
            ctx.header("Access-Control-Allow-Origin", origin); // Sæt oprindelsen dynamisk
            ctx.header("Vary", "Origin"); // Tilføj dette for bedre browser-cache håndtering
        }
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true"); // Tillad credentials (valgfrit)
        ctx.status(204); // No Content for OPTIONS
    }

    public static Javalin startServer() {
        var app = io.javalin.Javalin.create(AppConfig::configuration);

        // Tilføj fejlhåndtering
        exceptionContext(app);

        // Håndter adgangskontrol
        app.beforeMatched(accessController::accessHandler);

        // Tilføj CORS middleware
        app.before(AppConfig::corsHeaders);
        app.options("/*", AppConfig::corsHeadersOptions);

        // Fejl for 404
        app.error(404, ctx -> ctx.json("Welcome to our GEO API - See the different routes at /routes"));

        // Start serveren
        app.start(ApiProps.PORT);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}
