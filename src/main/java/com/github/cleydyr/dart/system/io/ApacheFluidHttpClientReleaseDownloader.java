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

    private final URI baseURL;

    private final HttpHost httpProxy;

    public ApacheFluidHttpClientReleaseDownloader(URI baseURL, Proxy proxy) throws URISyntaxException, MalformedURLException {
        this.baseURL = baseURL;
        // TODO: respect non-proxy host configuration
        this.httpProxy = proxy == null
                ? null
                : HttpHost.create(
                        new URL(proxy.getProtocol() + "://" + proxy.getHost() + ":" + proxy.getPort()).toURI());
    }

    @Override
    public void download(DartSassReleaseParameter release, File destination) throws ReleaseDownloadException {
        URI releaseURI = getReleaseURI(release);

        try {
            Request request = Request.get(releaseURI).viaProxy(httpProxy);

            request.execute().saveContent(destination);
        } catch (IOException e) {
            throw new ReleaseDownloadException("Error while downloading Dart Sass release from uri " + releaseURI, e);
        }
    }

    private URI getReleaseURI(DartSassReleaseParameter release) {
        return baseURL.resolve(String.format("%s/%s", release.getVersion(), release.getArtifactName()));
    }
}
