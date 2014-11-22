(defproject todo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [enfocus "2.1.1"]
                 [jayq "2.5.2"]]
  :source-paths ["src/clj"]
  :main ^:skip-aot todo.core
  :target-path "target/%s"
  :plugins [[lein-node-webkit-build "0.1.6"]
            [lein-cljsbuild "0.2.7"]]
  :cljsbuild {:builds [{:source-path "src/cljs"
                        :compiler {:output-to "resources/public/client.js"
                                   :optimizations :whitespace
                                   :pretty-print true } }] }
  :node-webkit-build {:root "resources/public"
                      :name "My NodeWebkit ClojureScript App"}
  :profiles {:uberjar {:aot :all}})
