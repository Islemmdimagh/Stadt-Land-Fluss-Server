package ch.uzh.ifi.hase.soprafs22.utils;

import ch.uzh.ifi.hase.soprafs22.entity.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.ValidationResult;
import ch.uzh.ifi.hase.soprafs22.utils.Validator;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static ch.uzh.ifi.hase.soprafs22.utils.Validator.validate;
import static org.junit.jupiter.api.Assertions.*;

/*
 * LobbyControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */

class ValidatorTest {
    @Test
    void testValidateCitiesGeoNameID() throws Exception {
        // given
        String testCity = "zurich";

        // then
        assertEquals(2657896, Objects.requireNonNull(validate(1, testCity, 'z')).getGeoNameID());
    }
@Test
    void testValidateCitiesWikipediaLink() throws Exception {
        // given
        String testCity = "zurich";

        // then
        assertEquals("https://en.wikipedia.org/wiki/Z%C3%BCrich", Objects.requireNonNull(validate(1, testCity, 'z')).getWikipediaLink());
    }
    
    /*@Test
    public void testValidateCitiesGeoNameID_Invalid() throws Exception {
        // given
        String testCity = "rurich";

        // then
        assertEquals(null, validate(1, testCity, 'z').getGeoNameID());
    }*/

    @Test
    void testValidateCountriesGeonameId() throws Exception {
        // given
        String testCountry = "schweiz";
        // then
        assertEquals(2658434, Objects.requireNonNull(validate(2, testCountry, 's')).getGeoNameID());
    }

    @Test
    void testValidateCountriesWikipediaLink() throws Exception {
        // given
        String testCountry = "schweiz";
        // then
        assertEquals("https://en.wikipedia.org/wiki/Switzerland", Objects.requireNonNull(validate(2, testCountry, 's')).getWikipediaLink());
    }

    @Test
    void testValidateRiversGeonameID() throws Exception {
        // given
        String testRiver = "nile";
        // then
        assertEquals(351036, Objects.requireNonNull(validate(3, testRiver, 'n')).getGeoNameID());
    }

    @Test
    void testValidateRiversWikipediaLink() throws Exception {
        // given
        String testRiver = "nile";
        // then
        String expected = "https://en.wikipedia.org/wiki/River_Nile";
        String reality = Objects.requireNonNull(validate(3, testRiver, 'n')).getWikipediaLink();
        assertEquals("https://en.wikipedia.org/wiki/River_Nile", Objects.requireNonNull(validate(3, testRiver, 'n')).getWikipediaLink());
    }

    @Test
    void testValidateMunicipalitiesGeonameID() throws Exception {
    // given
        String testMunicipality = "wartau";
        // then
        assertEquals(2658049, Objects.requireNonNull(validate(4, testMunicipality, 'w')).getGeoNameID());
    }

    @Test
    void testValidateMunicipalitiesWikipediaLink() throws Exception {
    // given
        String testMunicipality = "wartau";
        // then
        assertEquals("https://en.wikipedia.org/wiki/Wartau", Objects.requireNonNull(validate(4, testMunicipality, 'w')).getWikipediaLink());
    }

    @Test
    void testValidateWithWrongLetter() throws Exception {
        // given
        String testCountry = "schweiz";
        // then
        assertNull(validate(2, testCountry, 'r'));
    }

    @Test
    void testValidateWithWrongCategory() throws Exception {
        // given
        String testCountry = "schweiz";
        // then
        assertNull(validate(5, testCountry, 's'));
    }


}
