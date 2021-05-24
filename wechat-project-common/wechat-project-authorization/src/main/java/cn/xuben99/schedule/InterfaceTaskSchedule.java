package cn.xuben99.schedule;

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public interface InterfaceTaskSchedule {

    long SECOND = 1_000L;

    default void handle() throws Exception {
        notImplementHandleMethod();
    }

    void notImplementHandleMethod();

    void execute(String taskName);
}
