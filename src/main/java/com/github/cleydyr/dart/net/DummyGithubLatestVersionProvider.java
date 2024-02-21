package com.github.cleydyr.dart.net;

public class DummyGithubLatestVersionProvider implements GithubLatestVersionProvider {

    @Override
    public String get(String os, String arch) {
        return "1.71.1";
    }
}
