package org.valmaev.teamcity.server.dataaccess;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public final class ClientFactory {

    private ClientFactory() {
        throw new UnsupportedOperationException();
    }

    public static Client create(String login, String password) {
        return ClientBuilder.newClient()
                .register(new JacksonJsonProvider(ObjectMapperFactory.create()))
                .register(HttpAuthenticationFeature.basic(login, password));
    }
}
