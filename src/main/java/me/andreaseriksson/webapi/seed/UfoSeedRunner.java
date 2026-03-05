package me.andreaseriksson.webapi.seed;

import me.andreaseriksson.webapi.entity.*;
import me.andreaseriksson.webapi.repository.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class UfoSeedRunner implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final LocationRepository locationRepository;
    private final ShapeRepository shapeRepository;
    private final SightingRepository sightingRepository;

    public UfoSeedRunner(CountryRepository countryRepository,
                         LocationRepository locationRepository,
                         ShapeRepository shapeRepository,
                         SightingRepository sightingRepository) {
        this.countryRepository = countryRepository;
        this.locationRepository = locationRepository;
        this.shapeRepository = shapeRepository;
        this.sightingRepository = sightingRepository;
    }

    @Override
//    @Transactional
    public void run(String... args) throws Exception {
        if (sightingRepository.count() > 0) {
            System.out.println("Database already seeded. Skipping import.");
            return;
        }

        Reader reader = new InputStreamReader(
                new ClassPathResource("data/ufo_sightings.csv").getInputStream()
        );

        CSVParser csvParser = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .get()
                .parse(reader);

        // Caches (avoid repeated DB lookups)
        Map<String, Country> countryCache = new HashMap<>();
        countryRepository.findAll().forEach(c -> countryCache.put(c.getCode(), c));

        Map<String, Shape> shapeCache = new HashMap<>();
        shapeRepository.findAll().forEach(s -> shapeCache.put(s.getName(), s));

        Map<String, Location> locationCache = new HashMap<>();

        final int BATCH_SIZE = 1000;
        List<Sighting> batch = new ArrayList<>(BATCH_SIZE);

        int count = 0;
        for (CSVRecord row : csvParser) {
            try {
                String countryCode = emptyToNull(row.get("country"));
                String city = emptyToNull(row.get("city"));
                String state = emptyToNull(row.get("state"));
                String shapeName = emptyToNull(row.get("shape"));

                BigDecimal latitude = parseBigDecimal(row.get("latitude"));
                BigDecimal longitude = parseBigDecimal(row.get("longitude"));

                Country country = null;
                if (countryCode != null) {
                    country = countryCache.computeIfAbsent(countryCode, code ->
                            countryRepository.save(new Country(code)));
                }

                String locKey = locationKey(city, state, country != null ? country.getCode() : null, latitude, longitude);
                final Country finalCountry = country;
                Location location = locationCache.computeIfAbsent(locKey, k ->
                        locationRepository
                                .findByCityAndStateAndCountryAndLatitudeAndLongitude(city, state, finalCountry, latitude, longitude)
                                .orElseGet(() -> locationRepository.save(
                                        new Location(city, state, finalCountry, latitude, longitude)
                                ))
                );

                Shape shape = null;
                if (shapeName != null) {
                    shape = shapeCache.computeIfAbsent(shapeName, name ->
                            shapeRepository.save(new Shape(name)));
                }

                Sighting sighting = new Sighting(
                        parseDateTime(row.get("datetime")),
                        parseInteger(row.get("duration (seconds)")),
                        emptyToNull(row.get("duration (hours/min)")),
                        emptyToNull(row.get("comments")),
                        parseDate(row.get("date posted")),
                        location,
                        shape
                );

                batch.add(sighting);
                if (batch.size() >= BATCH_SIZE) {
                    sightingRepository.saveAll(batch);
                    batch.clear();
                }

                count++;
            } catch (Exception e) {
                System.out.println("Skipping row " + row.getRecordNumber());
            }
        }

        if (!batch.isEmpty()) {
            sightingRepository.saveAll(batch);
        }

        System.out.println("Done. Imported rows: " + count);
    }

    private String locationKey(String city, String state, String countryCode, BigDecimal latitude, BigDecimal longitude) {
        return nullSafe(city) + "|" + nullSafe(state) + "|" + nullSafe(countryCode) + "|" + nullSafe(latitude) + "|" + nullSafe(longitude);
    }

    private String nullSafe(Object value) {
        return value == null ? "∅" : value.toString();
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return value.trim();
    }

    private Integer parseInteger(String value) {
        try {
            return (int) Double.parseDouble(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value.trim(), DateTimeFormatter.ofPattern("M/d/yyyy H:mm"));
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern("M/d/yyyy"));
        } catch (Exception e) {
            return null;
        }
    }
}