package ru.battlefield.my;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.battlefield.my.domain.PlayerProfile;
import ru.battlefield.my.repository.PlayerProfilesRepository;

//@Slf4j
@SpringBootApplication
//@ComponentScan("ru.battlefield.my.domain")
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }


//    @Bean
//    public CommandLineRunner demo(PlayerProfilesRepository repository){
//        return (args)-> {
//
//            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:");
//            log.info("--------------------------------------------");
////            for (PlayerProfile cam : repository.findAllByLvl(1)){
////                log.info(cam.toString());
////            }
//            log.info(repository.findByNickName("pop").toString());
//            /*for (Specialization se : repository.findByNecessaryLvlIsLessThanEqual(41)){
//                log.info(se.toString());
//            }*/
//            /*for (Gadget ge : repository.findAllByNecessaryPointIsLessThanEqual(29000)){
//                log.info(ge.toString());
//            }*/
//            /*for (Weapon we : repository.findAllByMinDamageIsGreaterThanEqual(20.0)){
//                log.info(we.toString());
//            }*/
//            /*for (CommonWeapon cw : repository.findAllByLvlForUnlockIsLessThanEqual(15)){
//                log.info(cw.toString());
//            }*/
//            /*for (ClassWeapon clw : repository.findAllByPointsForUnlockIsLessThanEqual(110000)){
//                log.info(clw.toString());
//            }*/
//            /*for (Upgrade u : repository.findAllByKillsForUnlockIsLessThanEqual(15)){
//                log.info(u.toString());
//            }*/
//            log.info("");
//        };
//    }
}
