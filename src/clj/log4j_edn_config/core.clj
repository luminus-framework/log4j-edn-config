(ns log4j-edn-config.core
  (:require [cheshire.core :as cheshire]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:gen-class
   :name log4j.edn.config.EDNParser
   :methods [#^{:static true} [parse [java.io.InputStream] String]]))

(defn -parse [input]
  (-> input
      (io/reader)
      (java.io.PushbackReader.)
      (edn/read)
      (:logger)
      (cheshire/generate-string)))
