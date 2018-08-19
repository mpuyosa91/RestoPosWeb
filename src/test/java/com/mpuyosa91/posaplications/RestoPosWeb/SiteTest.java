package com.mpuyosa91.posaplications.RestoPosWeb;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SiteTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders      headers      = new HttpHeaders();
    @LocalServerPort
    private int              port;

    @Test
    public void createSiteReadSiteDeleteSite() {

        Site site = new Site();
        site.setTradeName("ThePanera");
        site.setNit("NIT: 15.444.730-9");

        UUID site_id = restTemplate.exchange(
                createURLWithPort("/site/"),
                HttpMethod.POST,
                new HttpEntity<>(site, headers),
                UUID.class
        ).getBody();

        assert site_id != null;

        User user = new User();
        user.setFirstName("Moises Eduardo");
        user.setRole(User.Role.Owner);

        UUID user_id = restTemplate.exchange(
                createURLWithPort("/user/"),
                HttpMethod.POST,
                new HttpEntity<>(user, headers),
                UUID.class
        ).getBody();

        assert user_id != null;


        Map<String, UUID> user_site_json = new HashMap<>();
        user_site_json.put("user", user_id);
        user_site_json.put("site", site_id);

        ResponseEntity responseEntity = restTemplate.exchange(
                createURLWithPort("/user/add_to_site"),
                HttpMethod.PUT,
                new HttpEntity<>(user_site_json, headers),
                User.class
        );

        site = restTemplate.exchange(
                createURLWithPort("/site/" + site_id),
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                Site.class
        ).getBody();

        user = restTemplate.exchange(
                createURLWithPort("/user/" + user_id),
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                User.class
        ).getBody();

        assertThat(site.getUsers().iterator().next().getId()).isEqualTo(user_id);
        assertThat(user.getSites().iterator().next().getId()).isEqualTo(site_id);

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
