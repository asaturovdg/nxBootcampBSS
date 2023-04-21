package ru.wostarnn.nxbootcampbss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.wostarnn.nxbootcampbss.services.BRT;

@SpringBootApplication
public class NxBootcampBssApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NxBootcampBssApplication.class, args);
        BRT brt = context.getBean(BRT.class);
        brt.performBilling();
    }

}
