package net.lapidist.colony.tests;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public final class TestLauncher {

    private TestLauncher() {
    }

    public static void main(final String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        resultReport(junit.run(TestSuite.class));
    }

    public static void resultReport(final Result result) {
        System.out.println("Finished all tests.\nFailures: "
                + result.getFailureCount() + "\nIgnored: "
                + result.getIgnoreCount() + "\nTests run: "
                + result.getRunCount() + "\nTime: "
                + result.getRunTime() + "ms");
        System.exit(result.getFailureCount() > 0 ? 1 : 0);
    }
}
