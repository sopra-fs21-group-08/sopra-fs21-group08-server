package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@WebAppConfiguration
@SpringBootTest
class StationServiceIntegrationTest {

    // given
    Station testStation;

    @Qualifier("stationRepository")
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationService stationService;

    @Test
    void testStationInitialization() throws Exception{
        List<Station> allStations = stationService.getStations();
        testStation = allStations.get(0);

        assertNotNull(testStation);
        assertNotNull(testStation.getStationId());
        assertNotNull(testStation.getName());
        assertNotNull(testStation.get_reachable_by_bus());
        assertNotNull(testStation.get_reachable_by_tram());
    }

}
