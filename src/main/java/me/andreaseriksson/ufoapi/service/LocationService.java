package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Country;
import me.andreaseriksson.ufoapi.entity.Location;
import me.andreaseriksson.ufoapi.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findByCityAndStateAndCountryAndLatitudeAndLongitude(
            String city,
            String state,
            Country country,
            BigDecimal latitude,
            BigDecimal longitude) {
        return locationRepository.findByCityAndStateAndCountryAndLatitudeAndLongitude(
                city, state, country, latitude, longitude);
    }
}
