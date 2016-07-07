(defproject log4j-edn-config "0.1.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.6.3"]
                 [com.fasterxml.jackson.core/jackson-databind "2.7.5"]
                 [org.apache.logging.log4j/log4j-core "2.6.1"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.7" "-source" "1.7"]
  :prep-tasks [["compile" "log4j-edn-config.core"]
               "javac" "compile"]
  :aot [log4j-edn-config.core])
