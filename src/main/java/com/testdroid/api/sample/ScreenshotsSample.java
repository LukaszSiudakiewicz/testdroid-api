package com.testdroid.api.sample;

import com.testdroid.api.APIClient;
import com.testdroid.api.APIException;
import com.testdroid.api.APIList;
import com.testdroid.api.model.APIProject;
import com.testdroid.api.model.APITestRun;
import com.testdroid.api.model.APIUser;
import com.testdroid.api.model.APIUserFile;
import com.testdroid.api.sample.util.Common;

import java.io.InputStream;

/**
 * @author Sławomir Pawluk <slawomir.pawluk@bitbar.com>
 */
public class ScreenshotsSample {

    private static final APIClient client = Common.createApiClient();

    public static void main(String[] args) {
        try {
            APIUser me = client.me();

            APITestRun testRun = getFinishedTestRun(me);

            if (testRun == null) {
                System.out.println("You have no finished test runs. Cannot generate screenshots.");
                return;
            }

            testRun.requestScreenshotsZip();
            APIUserFile file = testRun.getScreenshotsZip();

            while (file.getState() != APIUserFile.State.READY) {
                try {
                    System.out.println("Waiting for screenshots...");
                    Thread.sleep(5000);
                    file.refresh();
                } catch (InterruptedException ignore) {
                }
            }

            InputStream is = file.getFile();

        } catch (APIException apie) {
            System.out.println(apie.getMessage());
        }
    }

    private static APITestRun getFinishedTestRun(APIUser user) throws APIException {
        APIList<APIProject> projectsList = user.getProjectsResource().getEntity();

        for (APIProject project : projectsList.getData()) {
            APIList<APITestRun> testRunsList = project.getTestRunsResource().getEntity();
            for (APITestRun testRun : testRunsList.getData()) {
                if (testRun.getState() == APITestRun.State.FINISHED) {
                    return testRun;
                }
            }
        }

        return null;
    }
}
