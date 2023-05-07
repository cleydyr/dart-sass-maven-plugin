package com.github.cleydyr.dart.net;

public class DummyGithubLatestVersionProvider implements GithubLatestVersionProvider {

    @Override
    public String get() {
        return "1.62.1";
    }
}
