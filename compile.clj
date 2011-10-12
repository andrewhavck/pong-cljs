(require '[cljs.closure :as closure])

(if (= 1 (count *command-line-args*))
	(closure/build (first *command-line-args*) {:optimizations :advanced
								   :output-dir "out"
								   :output-to "pong.js"})
    (println "compile.clj requires one argument: path to cljs source to compile"))