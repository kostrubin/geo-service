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
    void testSendingWithRussianIp() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito
            .when(geoService.byIp(any(String.class)))
            .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        LocalizationService localizationService = new LocalizationServiceImpl();

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");

        String result = messageSender.send(headers);
        String expected = "Добро пожаловать";

        Assertions.assertEquals(expected, result);
    }

    @org.junit.jupiter.api.Test
    void testSendingWithEnglishIp() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito
            .when(geoService.byIp(any(String.class)))
            .thenReturn(new Location("New York", Country.USA, null,  0));

        LocalizationService localizationService = new LocalizationServiceImpl();

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");

        String result = messageSender.send(headers);
        String expected = "Welcome";

        Assertions.assertEquals(expected, result);
    }

    @org.junit.jupiter.api.Test
    void testGettingCorrectIpForRussianSending() {
        GeoService geoService = new GeoServiceImpl();

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
    void testGettingCorrectIpForEnglishSending() {
        GeoService geoService = new GeoServiceImpl();

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
        Country expectedRussia = Country.RUSSIA;
        Country expectedUSA = Country.USA;

        Country resultLocalhost = getResultCountry(GeoServiceImpl.LOCALHOST);
        Country resultMoscow = getResultCountry(GeoServiceImpl.MOSCOW_IP);
        Country resultNY = getResultCountry(GeoServiceImpl.NEW_YORK_IP);
        Country resultRussia = getResultCountry("172.105.66.77");
        Country resultUSA = getResultCountry("96.105.66.77");

        Assertions.assertNull(resultLocalhost);
        Assertions.assertEquals(expectedRussia, resultMoscow);
        Assertions.assertEquals(expectedUSA, resultNY);
        Assertions.assertEquals(expectedRussia, resultRussia);
        Assertions.assertEquals(expectedUSA, resultUSA);
    }

    private Country getResultCountry(String IP) {
        GeoService geoService = new GeoServiceImpl();

        return geoService.byIp(IP).getCountry();
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
