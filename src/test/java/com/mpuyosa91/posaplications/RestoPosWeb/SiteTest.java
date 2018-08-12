package com.mpuyosa91.posaplications.RestoPosWeb;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SiteTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders      headers      = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Test
    public void createSiteReadSiteDeleteSite() {

        Site site = new Site();
        site.setName("ThePanera");

        HttpEntity<Site> entity = new HttpEntity<>(site, headers);

        site = restTemplate.exchange(createURLWithPort("/site/"), HttpMethod.POST, entity, Site.class).getBody();

        System.out.println(site.toString());

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
