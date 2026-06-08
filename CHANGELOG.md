# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- v1.5.0 New feature to skip the compile goal execution by setting the `skip` flag to `true`
- v1.4.0 New feature to autodetect Linux Musl systems and use the Musl version of the
binary if detected

### Removed

- Cascade-remove support for Windows 7 and 8 as per https://github.com/dart-lang/sdk/issues/54509
