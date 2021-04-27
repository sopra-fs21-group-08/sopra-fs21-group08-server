package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.service.StationService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
public class StationControllerTest {

    // given
    private Station testStation;
    private List<Station> testStationList;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService stationService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        testStation = new Station();
        testStation.setName("testName");
        testStation.setStationId(69L);
        testStation.setStop_lat(6.66f);
        testStation.setStop_lat(6.66f);

        testStationList = new ArrayList<>();
        testStationList.add(testStation);
    }

    @AfterEach
    void tearDown() {
        testStation = null;
        testStationList = null;
    }

    @Test
    public void returnStations() throws Exception{

        given(stationService.getStations()).willReturn(testStationList);

        MockHttpServletRequestBuilder getRequest = get("/stations");

        mockMvc.perform(getRequest).andExpect(status().isOk());
    }
}
