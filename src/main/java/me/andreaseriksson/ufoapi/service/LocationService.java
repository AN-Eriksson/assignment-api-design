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

    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public Location save(Location location) {
        return repository.save(location);
    }

    public List<Location> findAll() {
        return repository.findAll();
    }

    public Optional<Location> findByCityAndStateAndCountryAndLatitudeAndLongitude(
            String city,
            String state,
            Country country,
            BigDecimal latitude,
            BigDecimal longitude) {
        return repository.findByCityAndStateAndCountryAndLatitudeAndLongitude(
                city, state, country, latitude, longitude);
    }

    public Optional<Location> findById(Long id) {
        return repository.findById(id);
    }

}
