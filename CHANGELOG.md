# Changelog

## [2.0.0](https://github.com/allisonjoycarter/BongoAPI/compare/v1.1.3...v2.0.0) (2024-03-18)


### ⚠ BREAKING CHANGES

* PoE Trade Search returns one item instead of a list to cut down on rate limits

### Features

* add origin to http error messages for easier debugging ([e7666f1](https://github.com/allisonjoycarter/BongoAPI/commit/e7666f1ff60fe6bebfd36bbdac7f3e84ec687bca))
* Cache PoE trade data using ehcache for faster responses. Cache stored in file. ([db1d564](https://github.com/allisonjoycarter/BongoAPI/commit/db1d564a4c660fd4f365ea21f2211de1c8c8ed6e))


### Code Refactoring

* PoE Trade Search returns one item instead of a list to cut down on rate limits ([14aaa88](https://github.com/allisonjoycarter/BongoAPI/commit/14aaa88128adb2973069df49369d991f4eb4397e))
