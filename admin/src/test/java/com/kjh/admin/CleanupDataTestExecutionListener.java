package com.kjh.admin;

import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class CleanupDataTestExecutionListener implements TestExecutionListener, Ordered {

    @Override
    public void afterTestMethod(TestContext testContext) {
        TestDataService testDataService = testContext.getApplicationContext().getBean(TestDataService.class);
        testDataService.deleteAll();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
