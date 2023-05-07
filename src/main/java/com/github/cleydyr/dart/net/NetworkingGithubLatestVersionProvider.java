package com.github.cleydyr.dart.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

@Singleton
@Named
public class NetworkingGithubLatestVersionProvider implements GithubLatestVersionProvider {

    public static final String GITHUB_DART_SASS_RELEASES_TAG_PREFIX = "https://github.com/sass/dart-sass/releases/tag/";
    public static final String GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI =
            "https://github.com/sass/dart-sass/releases/latest";

    private Log logger = new SystemStreamLog();

    private GithubLatestVersionProvider fallbackVersionProvider = new DummyGithubLatestVersionProvider();

    @Override
    public String get() {
        try {
            HttpURLConnection connection = setupConnection();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String location = connection.getHeaderField(HttpHeaders.LOCATION);

                if (location.startsWith(GITHUB_DART_SASS_RELEASES_TAG_PREFIX)) {
                    return location.substring(GITHUB_DART_SASS_RELEASES_TAG_PREFIX.length());
                }

                logger.warn("Couldn't parse latest release location. Redirected from "
                        + GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI + " to " + location);
            } else {
                logger.warn("Didn't get redirection from url " + GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI);
            }

        } catch (IOException e) {
            logger.warn("Error while getting latest version from " + GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI, e);
        }

        logger.warn("Falling back to latest known release (" + fallbackVersionProvider.get() + ")");

        return fallbackVersionProvider.get();
    }

    public HttpURLConnection setupConnection() throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        connection.setInstanceFollowRedirects(false);
        return connection;
    }
}
