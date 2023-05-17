package com.github.cleydyr.dart.system.io;

import com.github.cleydyr.dart.release.DartSassReleaseParameter;
import com.github.cleydyr.dart.system.OSDetector;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.HttpHost;

public class ApacheFluidHttpClientReleaseDownloader implements ReleaseDownloader {
    private static final String RELEASE_URI_PATTERN =
            "https://github.com/sass/dart-sass/releases/download/%s/dart-sass-%s-%s-%s.%s";

    private HttpHost httpProxy;

    public ApacheFluidHttpClientReleaseDownloader(URL proxyHost) throws URISyntaxException {
        if (proxyHost == null) {
            return;
        }

        this.httpProxy = HttpHost.create(proxyHost.toURI());
    }

    @Override
    public void download(DartSassReleaseParameter release, File destination) throws ReleaseDownloadException {
        String releaseURI = getReleaseURI(release);

        try {
            Request request = Request.get(new URI(releaseURI));

            if (httpProxy != null) {
                request = request.viaProxy(httpProxy);
            }

            request.execute().saveContent(destination);
        } catch (IOException e) {
            throw new ReleaseDownloadException("Error while downloading Dart Sass release from uri " + releaseURI, e);
        } catch (URISyntaxException e) {
            throw new ReleaseDownloadException("Invalid uri: " + releaseURI, e);
        }
    }

    private String getReleaseURI(DartSassReleaseParameter release) {
        return String.format(
                RELEASE_URI_PATTERN,
                release.getVersion(),
                release.getVersion(),
                release.getOS(),
                release.getArch(),
                getExtension(release.getOS()));
    }

    private String getExtension(String os) {
        return OSDetector.OS_WINDOWS.equals(os) ? "zip" : "tar.gz";
    }
}
