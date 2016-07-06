# log4j-edn-config

A Clojure library designed to provide EDN configuration support for log4j2

## Usage


[![Clojars Project](https://img.shields.io/clojars/v/log4j-edn-config.svg)](https://clojars.org/log4j-edn-config)


Include the dependency in the project, log4j will pick it and allow using `.edn` files for configuraiton.
The logging configruation must be present under the `:logger` key in the EDN file, e.g:

```clojure
{:logger
 {:configuration
  {:name "RoutingTest"
   :properties {:property
                {:name "filename"
                 :value "./rolling/rollingtest-$${sd:type}.log"}}
   :ThresholdFilter {:level "debug"}
   :appenders {:Console {:name "STDOUT"
                         :PatternLayout {:pattern "[%d][%p][%c] %m%n"}}}
   :loggers {:root {:level "info"
                    :AppenderRef {:ref "STDOUT"}}}}}}
```

The EDN under the `:logger` key is converted to JSON and follows log4j JSON configuration format.
See the official log4j documentation on [configuring JSON logging](https://logging.apache.org/log4j/2.x/manual/configuration.html#JSON) for details.

## License

Copyright © 2016 Dmitri Sotnikov

Distributed under the Apache License 2.0
