package app;

import app.config.AppConfig;

import java.io.IOException;
import java.net.URISyntaxException;

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
