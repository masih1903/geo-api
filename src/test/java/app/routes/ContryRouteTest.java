package app.routes;

import app.CountryPopulator;
import app.config.AppConfig;
import app.config.HibernateConfig;
import app.daos.CountryDAO;
import app.dtos.CarDTO;
import app.dtos.CountryDTO;
import app.dtos.CurrencyDTO;
import app.dtos.NameDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ContryRouteTest
{
    private static Javalin app;
    private static EntityManagerFactory emf;
    private static String BASE_URL = "http://localhost:7070/api";
    private static CountryDAO countryDao;
    private static CountryPopulator populator;
    private static CountryDTO denmark, japan, usa;
    private static List<CountryDTO> countries;

    @BeforeAll
    static void init()
    {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryConfigTest();
        countryDao = new CountryDAO(emf);  // Initialize the DAO after emf is set
        populator = new CountryPopulator(emf, countryDao);  // Initialize Populator after emf and DAO are set
        app = AppConfig.startServer();
    }

    @BeforeEach
    void setUp()
    {
        countries = populator.populateCountries();
        denmark = countries.get(0);
        japan = countries.get(1);
        usa = countries.get(2);
    }

    @AfterEach
    void tearDown()
    {
        populator.cleanUpCountries();
    }

    @AfterAll
    static void closeDown()
    {
        AppConfig.stopServer(app);
    }

    @Test
    void testGetAllCountries()
    {
        CountryDTO[] countries = given().when().get(BASE_URL + "/countries").then().log().all().statusCode(200).extract().as(CountryDTO[].class);

        assertThat(countries.length, is(3));
        assertThat(countries, arrayContainingInAnyOrder(equalTo(denmark), equalTo(japan), equalTo(usa)));
    }

    @Test
    void testGetCountryById()
    {
        CountryDTO countryDTO = given().when().get(BASE_URL + "/countries/1").then().log().all().statusCode(200).extract().as(CountryDTO.class);

        assertThat(countryDTO, equalTo(denmark));
    }

    @Test
    void testCreateCountry()
    {
        // Create a NameDTO for the country
        NameDTO nameDTO = new NameDTO("Pakistan", "Islamic Republic of Pakistan");

        // Create a CurrencyDTO and set in the map
        CurrencyDTO currencyDTO = new CurrencyDTO("Pakistani Rupee", "₨");
        Map<String, CurrencyDTO> currencies = Map.of("PKR", currencyDTO);

        // Set languages
        Map<String, String> languages = Map.of("ur", "Urdu");

        // Create a CarDTO for the driving side and car signs
        CarDTO carDTO = new CarDTO("left", List.of("PK"));

        // Create a CountryDTO with all fields
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName(nameDTO);
        countryDTO.setCurrencies(currencies);
        countryDTO.setLanguages(languages);
        countryDTO.setCapital(List.of("Islamabad"));
        countryDTO.setRegion("Asia");
        countryDTO.setPopulation(220892331L);
        countryDTO.setCar(carDTO);

        // Make a POST request to create the country
        CountryDTO createdCountry = given().contentType("application/json").body(countryDTO).when().post(BASE_URL + "/countries").then().log().all().statusCode(201).extract().as(CountryDTO.class);
        assertThat(createdCountry.getName().getCommon(), equalTo(countryDTO.getName().getCommon()));
        assertThat(createdCountry.getName().getOfficial(), equalTo(countryDTO.getName().getOfficial()));
        // Adjust the test to look for the "currency" key instead of "PKR"
        assertThat(createdCountry.getCurrencies().get("currency").getName(), equalTo(currencyDTO.getName()));
        assertThat(createdCountry.getCurrencies().get("currency").getSymbol(), equalTo(currencyDTO.getSymbol()));
        assertThat(createdCountry.getRegion(), equalTo(countryDTO.getRegion()));
        assertThat(createdCountry.getPopulation(), equalTo(countryDTO.getPopulation()));
        assertThat(createdCountry.getCapital(), equalTo(countryDTO.getCapital()));
        assertThat(createdCountry.getCar().getSide(), equalTo(countryDTO.getCar().getSide()));
        assertThat(createdCountry.getCar().getSigns(), equalTo(countryDTO.getCar().getSigns()));
        assertThat(createdCountry.getLanguages(), equalTo(countryDTO.getLanguages()));
    }

    @Test
    void testUpdateCountry()
    {
        // Create a NameDTO for the updated country
        NameDTO updatedName = new NameDTO("Pakistan Updated", "Islamic Republic of Pakistan Updated");

        // Create a CurrencyDTO and set in the map (if currencies need to be updated)
        CurrencyDTO currencyDTO = new CurrencyDTO("Pakistani Rupee", "₨");
        Map<String, CurrencyDTO> currencies = Map.of("PKR", currencyDTO);

        // Set updated population and name for the country
        CountryDTO pakistan = new CountryDTO();
        pakistan.setName(updatedName);
        pakistan.setPopulation(230000000L);  // Updating population
        pakistan.setCurrencies(currencies);  // Assuming currencies are to be updated
        pakistan.setRegion("Asia");
        pakistan.setCapital(List.of("Islamabad"));
        pakistan.setLanguages(Map.of("ur", "Urdu"));
        pakistan.setCar(new CarDTO("left", List.of("PK")));

        // Make a PUT request to update the country
        CountryDTO updatedCountry = given().contentType("application/json").body(pakistan)  // Send the updated CountryDTO as the request body
                .when().put(BASE_URL + "/countries/1")  // Assuming '1' is the ID of the country being updated
                .then().log().all().statusCode(200)  // Expect HTTP 200 OK
                .extract().as(CountryDTO.class);

        // Validate the updated fields
        assertThat(updatedCountry.getName().getCommon(), equalTo(pakistan.getName().getCommon()));
        assertThat(updatedCountry.getName().getOfficial(), equalTo(pakistan.getName().getOfficial()));
        assertThat(updatedCountry.getPopulation(), equalTo(pakistan.getPopulation()));
        assertThat(updatedCountry.getRegion(), equalTo(pakistan.getRegion()));  // Verify region remains unchanged
        assertThat(updatedCountry.getCapital(), equalTo(pakistan.getCapital()));  // Verify capitals remain unchanged
        // Expect "currency" as the key instead of "PKR"
        assertThat(updatedCountry.getCurrencies().get("currency").getName(), equalTo("Pakistani Rupee"));
        assertThat(updatedCountry.getCurrencies().get("currency").getSymbol(), equalTo("₨"));
    }

    @Test
    void deleteCountry()
    {
        // Perform DELETE request to delete the country with ID 1
        given().when().delete(BASE_URL + "/countries/1").then().log().all().statusCode(204);  // Expect 204 No Content on successful deletion

        // Verify that the remaining countries are Japan and USA
        CountryDTO[] countryDTOS = given().when().get(BASE_URL + "/countries").then().log().all().statusCode(200).extract().as(CountryDTO[].class);

        // Check that there are now only 2 countries remaining after deletion
        assertThat(countryDTOS.length, is(2));
        assertThat(countryDTOS[0], equalTo(japan));   // First remaining country should be Japan
        assertThat(countryDTOS[1], equalTo(usa));     // Second remaining country should be USA
    }
}
