(defproject fits-analysis "0.1.0"
  :description "Program to analyse performance of FITS characterisations."
  :url "http://statsbiblioteket.dk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ;; the javatar dependency is not on Clojars and I need to include it as
  ;; a stand-alone JAR. To make the JAR available to the code, issues this Maven command:
  ;; mvn install:install-file -Dfile=/Users/perdalum/Downloads/javatar-2.5/jars/tar.jar  -DartifactId=javatar -Dversion=2.5 -DgroupId=com.ice.tar -Dpackaging=jar -DlocalRepositoryPath=maven_repository
  ;;
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-time "0.4.4"] ;; wrapping Joda Time
                 [org.clojure/tools.cli "0.2.2"] ;; for option parsing
                 [javatar "2.5"]] ;; for reading TAR archives
  :aot [fits-analysis.core]
  :main fits-analysis.core)

