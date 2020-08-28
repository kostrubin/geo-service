package ru.netology;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import ru.netology.entity.*;
import ru.netology.geo.*;
import ru.netology.i18n.*;
import ru.netology.sender.*;

class Test {
    @org.junit.jupiter.api.Test
    void testAlwaysRussianSending() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito
            .when(geoService.byIp(any(String.class)))
            .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito
            .when(localizationService.locale(any(Country.class)))
            .thenReturn("Добро пожаловать");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");

        String result = messageSender.send(headers);
        String expected = "Добро пожаловать";

        Assertions.assertEquals(expected, result);
    }

    @org.junit.jupiter.api.Test
    void testAlwaysEnglishSending() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito
            .when(geoService.byIp(any(String.class)))
            .thenReturn(new Location("New York", Country.USA, null,  0));

        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito
            .when(localizationService.locale(any(Country.class)))
            .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");

        String result = messageSender.send(headers);
        String expected = "Welcome";

        Assertions.assertEquals(expected, result);
    }

    @org.junit.jupiter.api.Test
    void testGettingLocationById() {
        GeoService geoService = new GeoServiceImpl();

        Country resultLocalhost = geoService.byIp(GeoServiceImpl.LOCALHOST).getCountry();

        Country resultMoscow = geoService.byIp(GeoServiceImpl.MOSCOW_IP).getCountry();
        Country expectedMoscow = Country.RUSSIA;

        Country resultNY = geoService.byIp(GeoServiceImpl.NEW_YORK_IP).getCountry();
        Country expectedNY = Country.USA;

        Country resultRussia = geoService.byIp("172.105.66.77").getCountry();
        Country expectedRussia = Country.RUSSIA;

        Country resultUSA = geoService.byIp("96.105.66.77").getCountry();
        Country expectedUSA = Country.USA;

        Assertions.assertNull(resultLocalhost);
        Assertions.assertEquals(expectedMoscow, resultMoscow);
        Assertions.assertEquals(expectedNY, resultNY);
        Assertions.assertEquals(expectedRussia, resultRussia);
        Assertions.assertEquals(expectedUSA, resultUSA);
    }

    @org.junit.jupiter.api.Test
    void testLocalization() {
        LocalizationService localizationService = new LocalizationServiceImpl();

        String resultRus = localizationService.locale(Country.RUSSIA);
        String expectedRus = "Добро пожаловать";

        String resultEng = localizationService.locale(Country.USA);
        String expectedEng = "Welcome";

        Assertions.assertEquals(expectedRus, resultRus);
        Assertions.assertEquals(expectedEng, resultEng);
    }
}
