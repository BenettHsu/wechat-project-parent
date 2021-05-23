package cn.xuben99.schedule;

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public interface InterfaceSchedule {

    long SECOND = 1_000L;

    default void handle(String taskName){
        notImplementHandleMethod();
    }

    void notImplementHandleMethod();

    void execute(String taskName);
}
