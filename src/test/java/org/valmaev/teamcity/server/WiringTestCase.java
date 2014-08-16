package org.valmaev.teamcity.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

public class WiringTestCase {

    @Test
    public void applicationContext_always_shouldProperlyInstantiateObjectRoot() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "META-INF/build-server-plugin-teamcity-youtrack-sprint-provider.xml");
        context.getBean(YoutrackSprintBuildParametersProvider.class);
    }
}
