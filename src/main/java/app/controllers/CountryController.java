package app.controllers;

import app.daos.CountryDAO;
import app.dtos.CountryDTO;
import app.entities.Country;
import app.entities.Message;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CountryController implements IController {

    private final Logger log = LoggerFactory.getLogger(CountryController.class);
    private final CountryDAO countryDAO;

    public CountryController(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }


    @Override
    public void getById(Context ctx) {
        try {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));

            // == querying ==
            Country country = countryDAO.getById(id);

            // == response ==
            CountryDTO countryDTO = new CountryDTO(country);
            ctx.res().setStatus(200);
            ctx.json(countryDTO, CountryDTO.class);

        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void getAll(Context ctx) {
        try {
            // == querying ==
            List<Country> countries = countryDAO.getAll();

            // == response ==
            List<CountryDTO> countryDTOs = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOs, CountryDTO.class);
        } catch (Exception e) {
            log.error("500 {} ", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }

    }

    @Override
    public void create(Context ctx) {
        try {
            // == request ==
            CountryDTO countryDTO = ctx.bodyAsClass(CountryDTO.class);

            // == querying ==
            Country country = new Country(countryDTO);

            // Debug log: Check if capitals are set correctly before persisting
            log.debug("Capitals before persisting: {}", country.getCapitals());
            countryDAO.create(country);

            // == response ==
            ctx.status(201).json(new CountryDTO(country));  // Return the created CountryDTO in the response
        } catch (Exception e)
        {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));
            CountryDTO countryDTO = ctx.bodyAsClass(CountryDTO.class);

            // == querying for existing Country ==

            Country existingCountry = countryDAO.getById(id);

            if (existingCountry == null) {
                ctx.res().setStatus(404);
                ctx.json(new Message(404, "Country not found"), Message.class);
                return;
            }

            // == updating fields of the existing Country ==
            existingCountry.setCommonName(countryDTO.getName().getCommon());
            existingCountry.setOfficialName(countryDTO.getName().getOfficial());
            existingCountry.setCurrencyName(countryDTO.getCurrencies().values().stream().findFirst().orElse(null).getName());
            existingCountry.setCurrencySymbol(countryDTO.getCurrencies().values().stream().findFirst().orElse(null).getSymbol());
            existingCountry.setRegion(countryDTO.getRegion());
            existingCountry.setPopulation(countryDTO.getPopulation());
            existingCountry.setCapitals(countryDTO.getCapital());
            existingCountry.setDrivingSide(countryDTO.getCar().getSide());
            existingCountry.setCarSigns(countryDTO.getCar().getSigns());
            existingCountry.setLanguages(countryDTO.getLanguages());

            // Update any other fields necessary

            // == updating in DB ==
            countryDAO.update(existingCountry);

            // == response ==
            ctx.res().setStatus(200);
            ctx.json(new CountryDTO(existingCountry));  // Return the updated country as JSON
        }
        catch (NumberFormatException e)
        {
            log.error("Invalid Country ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid Country ID format");
        }
        catch (Exception e)
        {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));

            // == querying for the existing country ==
            Country country = countryDAO.getById(id);

            if (country == null) {
                ctx.res().setStatus(404);
                ctx.result("Country not found");
                return;
            }

            // == delete the country ==
            countryDAO.delete(id);

            // == response ==
            ctx.status(204);  // No Content
        } catch (NumberFormatException e)
        {
            log.error("400 {} ", e.getMessage());
            throw new ApiException(400, "Invalid Country ID format");
        } catch (Exception e)
        {
            log.error("500 {} ", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    public void getCountriesByASpecificRegion(Context ctx)
    {
        try
        {
            // == request ==
            String region = ctx.pathParam("region");

            // == querying ==
            List<Country> countries = countryDAO.getSpecificRegion(region);

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getTop10HighestPopulation(Context ctx) {
        try {
            // == querying ==
            List<Country> countries = countryDAO.getTop10HighestPopulation();

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getTop10LowestPopulation(Context ctx) {
        try {
            // == querying ==
            List<Country> countries = countryDAO.getTop10LowestPopulation();

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getCountriesByASpecificDrivingSide(Context ctx) {
        try {
            // == request ==
            String drivingSide = ctx.pathParam("drivingside");

            // == querying ==
            List<Country> countries = countryDAO.getSpecificDrivingSide(drivingSide);

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getCountryByName(Context ctx) {

        try {
            // == request ==
            String commonName = ctx.pathParam("name");

            // == querying ==
            Country country = countryDAO.getCountryByName(commonName);

            // == response ==
            CountryDTO countryDTO = new CountryDTO(country);
            ctx.res().setStatus(200);
            ctx.json(countryDTO, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }

    }

    public void getCountriesByCurrency(Context ctx) {

        try {
            // == request ==
            String currencyName = ctx.pathParam("currency");

            // == querying ==
            List<Country> countries = countryDAO.getCountriesByCurrency(currencyName);

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }

    }

    public void getCountriesByLanguage(Context ctx) {
        try {
            // == request ==
            String language = ctx.pathParam("languages");

            // == querying ==
            List<Country> countries = countryDAO.getCountriesByLanguage(language);

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getCountriesByLanguageAndIgnoreOtherLanguages(Context ctx) {

        try {
            // == request ==
            String language = ctx.pathParam("language");

            // == querying ==
            List<Country> countries = countryDAO.getCountriesByLanguageAndIgnoreOtherLanguages(language);

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }

    }

    public void getCountriesByCapital(Context ctx) {

        try {
            // == request ==
            String capital = ctx.pathParam("capitals");

            // == querying ==
            List<Country> countries = countryDAO.getCountriesByCapital(capital);

            // == response ==
            List<CountryDTO> countryDTOList = CountryDTO.toCountryDTOList(countries);

            ctx.res().setStatus(200);
            ctx.json(countryDTOList, CountryDTO.class);
        } catch (Exception e) {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

}
