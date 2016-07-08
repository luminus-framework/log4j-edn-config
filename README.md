# log4j-edn-config

A Clojure library designed to provide EDN configuration support for log4j2

## Usage


[![Clojars Project](https://img.shields.io/clojars/v/log4j-edn-config.svg)](https://clojars.org/log4j-edn-config)

** warning ** Log4j plugins are not compatible with uberjars, log4j generates a plugin dat file, and only a single file can be present on the classpath, when multiple ones are present, then you'll end up with unexpected behaviors. Maven provides a [plugin](https://stackoverflow.com/questions/25065580/log4j2-custom-plugins-annotation-processing-with-maven-assembly-plugin) for handling the issue, however there's no way to do that via Leiningen at the moment.

Include the dependency in the project, log4j will pick it and allow using `.edn` files for configuraiton.
The logging configruation must be present under the `:logger` key in the EDN file, e.g:

```clojure
{:logger
 {:configuration
  {:name "DefaultConfig"
   :ThresholdFilter {:level "debug"}
   :appenders
   {:appender
    [{:type "Console"
      :name "Console"
      :target "SYSTEM_OUT"
      :PatternLayout {:pattern "[%d][%p][%c] %m%n"}}
     {:type "RollingFile"
      :name "File"
      :fileName "./log/myapp.log"
      :filePattern "./log/myapp-%d{MM-dd-yyyy}-%i.log.gz"
      :PatternLayout {:pattern "[%d][%p][%c] %m%n"}
      :DefaultRolloverStrategy {:max "10"}
      :Policies
      {:SizeBasedTriggeringPolicy {:size "10 MB"}}}]}
   :loggers {:logger
             [{:name "org.xnio.nio"
               :level "warn"}]
             :root {:level "info"
                    :AppenderRef
                    [{:ref "Console"}
                     {:ref "File"}]}}}}}

```

The EDN under the `:logger` key is converted to JSON and follows log4j JSON configuration format.
See the official log4j documentation on [configuring JSON logging](https://logging.apache.org/log4j/2.x/manual/configuration.html#JSON) for details.

## License

Copyright © 2016 Dmitri Sotnikov

Distributed under the Apache License 2.0
