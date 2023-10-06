package com.github.cleydyr.dart.net;

public interface GithubLatestVersionProvider {
    String get(String os, String arch);
}
