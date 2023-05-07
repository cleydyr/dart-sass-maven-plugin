package com.github.cleydyr.dart.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import junit.framework.TestCase;
import org.apache.hc.core5.http.HttpHeaders;

public class NetworkingGithubLatestVersionProviderTest extends TestCase {
    public void testRedirectIsWorking() {
        NetworkingGithubLatestVersionProvider networkingGithubLatestVersionProvider =
                new NetworkingGithubLatestVersionProvider();

        try {
            HttpURLConnection connection = networkingGithubLatestVersionProvider.setupConnection();

            int expected = HttpURLConnection.HTTP_MOVED_TEMP;

            assertEquals(
                    "Request to " + NetworkingGithubLatestVersionProvider.GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI
                            + " doesn't return response code " + expected,
                    expected,
                    connection.getResponseCode());
        } catch (IOException e) {
            fail();
        }
    }

    public void testLocationStartsWithPrefix() {
        NetworkingGithubLatestVersionProvider networkingGithubLatestVersionProvider =
                new NetworkingGithubLatestVersionProvider();

        try {
            HttpURLConnection connection = networkingGithubLatestVersionProvider.setupConnection();

            String location = connection.getHeaderField(HttpHeaders.LOCATION);

            String prefix = NetworkingGithubLatestVersionProvider.GITHUB_DART_SASS_RELEASES_TAG_PREFIX;

            String url = NetworkingGithubLatestVersionProvider.GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI;

            assertTrue(
                    "Request to " + url + " doesn't redirect to another url with prefix " + prefix,
                    location.startsWith(prefix));
        } catch (IOException e) {
            fail();
        }
    }
}
