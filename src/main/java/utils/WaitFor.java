package utils;

import java.time.Duration;
import java.util.concurrent.Callable;
import org.awaitility.Awaitility;

public class WaitFor {

    final Test test;

    public WaitFor(Test test) {
        this.test = test;
    }


    public void expectedCondition(Callable<Boolean> conditionEvaluator) {
        Awaitility.await()
                .pollInterval(Duration.ofSeconds(Long.parseLong(test.envDataConfig()
                .getInterval())))
                .atMost(Duration.ofSeconds(Long.parseLong(test.envDataConfig()
                .getTimeOut()))).until(conditionEvaluator);
    }
}
