# playframework-core

[![Build Status](https://travis-ci.com/justapagamentos/playframework-core.svg?branch=master)](https://travis-ci.com/justapagamentos/playframework-core)
[![Maintainability](https://api.codeclimate.com/v1/badges/6d599db3c285d692df8a/maintainability)](https://codeclimate.com/github/justapagamentos/playframework-core/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/6d599db3c285d692df8a/test_coverage)](https://codeclimate.com/github/justapagamentos/playframework-core/test_coverage)

Core classes &amp; models to re-use on your [Play! Framework](https://playframework.com/) project.

### Dependencies

- [PlayFramework Core](https://github.com/justapagamentos/playframework-core)
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/)

### Installation

This project requires [Play! Framework](https://playframework.com/) v2.8.X and [Scala](https://www.scala-lang.org)
2.13.X to run.

Install the dependencies and devDependencies and start the server at default port (9000).

```sh
$ cd playframework-core
$ sbt compile run
```

#### Building for source

Generating pre-built zip archives for distribution:

```sh
$ cd playframework-core
$ sbt dist
```

### Authors

- [Felipe Bonezi](mailto:fb@justa.com.vc)
