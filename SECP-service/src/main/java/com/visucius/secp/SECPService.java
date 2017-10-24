package com.visucius.secp;

import com.visucius.secp.Chat.ChatServerServlet;
import com.visucius.secp.Resource.*;
import com.visucius.secp.UseCase.*;
import com.visucius.secp.config.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRegistration;


public class SECPService extends Application<SECPConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(SECPService.class);

    public static void main(String[] args) throws Exception {
        new SECPService().run(args);
    }

    private final HibernateBundle<SECPConfiguration> hibernateBundle = new HibernateBundle<SECPConfiguration>(

            Void.class
        ) {
        @Override
        public DataSourceFactory getDataSourceFactory(SECPConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public String getName() {
        return "SECP";
    }
    @Override
    public void initialize(Bootstrap<SECPConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/app/", "/", "index.html"));
        bootstrap.addBundle(hibernateBundle);
        ObjectMapper mapper = bootstrap.getObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void run(SECPConfiguration configuration,
                    Environment environment) throws Exception {

        final UserRegistrationController userRegistrationController = new UserRegistrationController(null,null);
        final RegisterResource registerResource = new RegisterResource(userRegistrationController);
        final UserLoginController userLoginController = new UserLoginController(null,null);
        final LoginResource loginResource = new LoginResource(userLoginController);

        environment.jersey().register(registerResource);
        environment.jersey().register(loginResource);
        environment.jersey().setUrlPattern("/SECP/*");
        //environment.jersey().disable();
        ServletRegistration.Dynamic webSocket = environment.servlets().addServlet("ws", new ChatServerServlet());
        webSocket.setAsyncSupported(true);
        webSocket.addMapping("/chat/*");

    }
}
