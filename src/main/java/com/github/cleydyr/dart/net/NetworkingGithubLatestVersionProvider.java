package com.github.cleydyr.dart.net;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.logging.Logger;
import org.kohsuke.github.*;
import org.kohsuke.github.connector.GitHubConnectorResponse;
import org.kohsuke.github.extras.ImpatientHttpConnector;
import org.kohsuke.github.internal.GitHubConnectorHttpConnectorAdapter;

@Singleton
@Named
public class NetworkingGithubLatestVersionProvider implements GithubLatestVersionProvider {

    @Inject
    private Logger logger;

    @Inject
    private MavenSession mavenSession;

    private final GithubLatestVersionProvider fallbackVersionProvider = new DummyGithubLatestVersionProvider();

    @Override
    public String get(String os, String arch) {
        try {
            GitHub github = new GitHubBuilder()
                    .withConnector(GitHubConnectorHttpConnectorAdapter.adapt(new ImpatientHttpConnector(this::setupConnection)))
                    .withRateLimitHandler(new GitHubRateLimitHandler() {
                        @Override
                        public void onError(GitHubConnectorResponse ghcr) throws IOException {
                            throw new IOException("GitHub rate limit exceeded.");
                        }
                    })
                    .withAbuseLimitHandler(new GitHubAbuseLimitHandler() {
                        @Override
                        public void onError(GitHubConnectorResponse ghcr) throws IOException {
                            throw new IOException("GitHub abuse limit hit.");
                        }
                    })
                    .build();

            GHRepository repository = github.getRepository("sass/dart-sass");

            PagedIterable<GHRelease> ghReleases = repository.listReleases();

            for (GHRelease ghRelease : ghReleases) {
                logger.debug("Checking release " + ghRelease.getName());

                String version = ghRelease.getTagName();

                DartSassReleaseParameter dartSassReleaseParameter = new DartSassReleaseParameter(os, arch, version);

                for (GHAsset ghAsset : ghRelease.listAssets()) {
                    logger.debug("Checking asset " + ghAsset.getName());

                    if (ghAsset.getName().equals(dartSassReleaseParameter.getArtifactName())) {
                        return version;
                    }
                }

                logger.info("Skipping version " + version + " because it doesn't have a matching asset");
            }
        } catch (IOException e) {
            logger.warn("Error while getting latest version from GitHub API", e);
        }

        logger.warn("Falling back to latest known release (" + fallbackVersionProvider.get(os, arch) + ")");

        return fallbackVersionProvider.get(os, arch);
    }

    public HttpURLConnection setupConnection(URL url) throws IOException {
        if (mavenSession == null
                || mavenSession.getSettings() == null
                || mavenSession.getSettings().getActiveProxy() == null) {
            return (HttpURLConnection) url.openConnection();
        }

        return setupWithMavenSettings(url);
    }

    public HttpURLConnection setupWithMavenSettings(URL url) throws IOException {
        try {
            Settings settings = mavenSession.getSettings();

            org.apache.maven.settings.Proxy activeProxy = settings.getActiveProxy();

            if (activeProxy == null) {
                return (HttpURLConnection) url.openConnection();
            }

            Proxy proxy = getProxy(activeProxy);

            return (HttpURLConnection) url.openConnection(proxy);
        } catch (IOException e) {
            logger.warn("Error while parsing maven settings. Settings like proxy will be ignored.");

            return (HttpURLConnection) url.openConnection();
        }
    }

    private static Proxy getProxy(org.apache.maven.settings.Proxy activeProxy) {
        String hostname = activeProxy.getHost();

        int port = activeProxy.getPort();

        return new Proxy(Type.HTTP, new InetSocketAddress(hostname, port));
    }
}
