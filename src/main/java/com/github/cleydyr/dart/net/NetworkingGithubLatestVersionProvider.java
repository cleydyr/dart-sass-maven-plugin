package com.github.cleydyr.dart.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.maven.settings.MavenSettingsBuilder;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

@Singleton
@Named
@SuppressWarnings("deprecation")
@Component(role = GithubLatestVersionProvider.class)
public class NetworkingGithubLatestVersionProvider implements GithubLatestVersionProvider {

    public static final String GITHUB_DART_SASS_RELEASES_TAG_PREFIX = "https://github.com/sass/dart-sass/releases/tag/";
    public static final String GITHUB_SASS_DART_SASS_LATEST_RELEASE_URI =
            "https://github.com/sass/dart-sass/releases/latest";

    @Requirement
    private Logger logger;

    @Requirement
    private MavenSettingsBuilder mavenSettingsBuilder;

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

        HttpURLConnection connection = null;

        if (mavenSettingsBuilder == null) {
            connection = (HttpURLConnection) url.openConnection();
        } else {
            try {
                Settings settings = mavenSettingsBuilder.buildSettings();

                org.apache.maven.settings.Proxy activeProxy = settings.getActiveProxy();

                if (activeProxy != null) {
                    String hostname = activeProxy.getHost();

                    int port = activeProxy.getPort();

                    Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(hostname, port));

                    connection = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    connection = (HttpURLConnection) url.openConnection();
                }

            } catch (IOException | XmlPullParserException e) {
                logger.warn("Error while parsing maven settings. Settings like proxy will be ignored.");

                connection = (HttpURLConnection) url.openConnection();
            }
        }

        connection.setRequestMethod("GET");

        connection.setInstanceFollowRedirects(false);

        return connection;
    }
}
