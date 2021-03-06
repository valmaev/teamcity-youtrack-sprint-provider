package org.valmaev.teamcity.server.dataaccess;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientFactory {

    public Client create() {
        return ClientBuilder.newClient()
                .register(new JacksonJsonProvider(ObjectMapperFactory.create()))
                .register(HttpAuthenticationFeature.basicBuilder().build());
    }
}
