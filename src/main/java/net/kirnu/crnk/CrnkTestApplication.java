package net.kirnu.crnk;

import net.kirnu.crnk.repositories.cyclic.CyclicResourceARepository;
import net.kirnu.crnk.repositories.cyclic.CyclicResourceBRespository;
import net.kirnu.crnk.repositories.cyclic.CyclicResourceCRepository;
import net.kirnu.crnk.repositories.related.RelatedResourceARepository;
import net.kirnu.crnk.repositories.related.RelatedResourceBRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import io.crnk.core.module.SimpleModule;
import io.crnk.rs.CrnkFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/*
 * @author syri.
 */
public class CrnkTestApplication extends Application<CrnkTestApplicationConfig> {

    @Override
    public void run(CrnkTestApplicationConfig configuration, Environment environment) {
        environment.jersey().register(getCrnkModule());
    }

    public static void main(String[] args) throws Exception {
        new CrnkTestApplication().run(args);
    }


    public static CrnkFeature getCrnkModule() {
        SimpleModule crnkModule = new SimpleModule("crnk-test");
        crnkModule.addRepository(new CyclicResourceARepository());
        crnkModule.addRepository(new CyclicResourceBRespository());
        crnkModule.addRepository(new CyclicResourceCRepository());
        crnkModule.addRepository(new RelatedResourceARepository());
        crnkModule.addRepository(new RelatedResourceBRepository());

        CrnkFeature crnkFeature = new CrnkFeature();
        crnkFeature.addModule(crnkModule);
        configureObjectMapper(crnkFeature.getObjectMapper());
        return crnkFeature;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
    }
}
