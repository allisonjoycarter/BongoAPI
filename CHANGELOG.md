# Changelog

## [2.2.0](https://github.com/allisonjoycarter/BongoAPI/compare/v2.1.0...v2.2.0) (2024-03-19)


### Features

* include PoE trade URL with searches and use most common pricing ([fd71635](https://github.com/allisonjoycarter/BongoAPI/commit/fd71635ecf6f87dc0a1966279a3b1d694fd8acee))


### Bug Fixes

* Attempted to fix cheap poe currency with /chaos ([2e27a4d](https://github.com/allisonjoycarter/BongoAPI/commit/2e27a4d0dafcab0a48ed6f35b2d8f2e8e0b4a048))
* Name generator always returning one name ([1b58505](https://github.com/allisonjoycarter/BongoAPI/commit/1b58505ec3a71f46da38492a990da73d001b6b6c))

## [2.1.0](https://github.com/allisonjoycarter/BongoAPI/compare/v2.0.0...v2.1.0) (2024-03-18)


### Features

* include item name with poe item price info ([27b101e](https://github.com/allisonjoycarter/BongoAPI/commit/27b101e5af31c627874e2d52848a759e2c7d49d5))

## [2.0.0](https://github.com/allisonjoycarter/BongoAPI/compare/v1.1.3...v2.0.0) (2024-03-18)


### ⚠ BREAKING CHANGES

* PoE Trade Search returns one item instead of a list to cut down on rate limits

### Features

* add origin to http error messages for easier debugging ([e7666f1](https://github.com/allisonjoycarter/BongoAPI/commit/e7666f1ff60fe6bebfd36bbdac7f3e84ec687bca))
* Cache PoE trade data using ehcache for faster responses. Cache stored in file. ([db1d564](https://github.com/allisonjoycarter/BongoAPI/commit/db1d564a4c660fd4f365ea21f2211de1c8c8ed6e))


### Code Refactoring

* PoE Trade Search returns one item instead of a list to cut down on rate limits ([14aaa88](https://github.com/allisonjoycarter/BongoAPI/commit/14aaa88128adb2973069df49369d991f4eb4397e))
