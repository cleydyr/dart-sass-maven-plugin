package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.io.exception.ReleaseDownloadException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.HttpHost;
import org.apache.maven.settings.Proxy;

public class ApacheFluidHttpClientReleaseDownloader implements ReleaseDownloader {

    private static final String RELEASE_URI_PATTERN = "https://github.com/sass/dart-sass/releases/download/%s/%s";

    private final HttpHost httpProxy;

    public ApacheFluidHttpClientReleaseDownloader(Proxy proxy) throws URISyntaxException, MalformedURLException {
        //TODO: respect non-proxy host configuration
        this.httpProxy = proxy == null ? null : HttpHost.create(new URL(proxy.getProtocol() + "://" + proxy.getHost() + ":" + proxy.getPort()).toURI());
    }

    @Override
    public void download(DartSassReleaseParameter release, File destination) throws ReleaseDownloadException {
        String releaseURI = getReleaseURI(release);

        try {
            Request request = Request.get(new URI(releaseURI)).viaProxy(httpProxy);

            request.execute().saveContent(destination);
        } catch (IOException e) {
            throw new ReleaseDownloadException("Error while downloading Dart Sass release from uri " + releaseURI, e);
        } catch (URISyntaxException e) {
            throw new ReleaseDownloadException("Invalid uri: " + releaseURI, e);
        }
    }

    private String getReleaseURI(DartSassReleaseParameter release) {
        return String.format(RELEASE_URI_PATTERN, release.getVersion(), release.getArtifactName());
    }
}
