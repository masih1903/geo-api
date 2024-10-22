package app;

import app.Service.CountryService;
import app.config.AppConfig;
import app.config.HibernateConfig;
import app.daos.CountryDAO;
import app.dtos.CountryDTO;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException
    {
        AppConfig.startServer();

//        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
//
//        CountryService countryService = new CountryService();
//        List<CountryDTO> countryDTOList = countryService.fetchAllData();
//
//        CountryDAO countryDAO = new CountryDAO(emf);
//       countryDAO.saveCountriesToDb(countryDTOList);

    }
}
