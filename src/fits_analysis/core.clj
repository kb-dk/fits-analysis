(ns fits-analysis.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:use [clojure.tools.cli]
        [clj-time.core :only [in-secs interval]]
        [clj-time.coerce :only [from-date]])

  (:import (java.io File
             InputStreamReader
             BufferedReader
             FileInputStream
             ByteArrayInputStream)
           (java.util.zip GZIPInputStream)
           (com.ice.tar TarArchive TarInputStream))
  (:gen-class))

(defn list-tgz-entries
  [tarfile]
  (let [tarstream (->> tarfile FileInputStream. GZIPInputStream. TarInputStream.)]
    (doall
      (flatten
        (for [entry (repeatedly #(.getNextEntry tarstream))
              :while entry
              :when (and (not (.isDirectory entry)) (not= (.getName entry) "././@LongLink"))]
          {:name (.getName entry)
           :size (.getSize entry)
           :modification (.getModTime entry)})))))


(defn analyse-tarfile
  "This function reads a FITS tarball, ie. a gzipped tar file, and extracts data on the oldest and newest file therein.
  It then builds a key/value structure with data aobut the tarball."
  [tarfile]
  (let [entries (sort-by :modification (list-tgz-entries tarfile))]
    {:name (.getName tarfile)
     :time (try (in-secs
                  (interval
                    (from-date (:modification (first entries)))
                    (from-date (:modification (last entries)))))
             (catch Exception e 0))
     :length (.length tarfile)
     :count (count entries)
     }))

(defn process-directory
  "Usage:
    (print-table
      (process-directory \"/Users/perdalum/Statsbiblioteket/Projekter/FITS-Analyse/fits-analysis/data\"))"

  [directory]
  (map #(analyse-tarfile %)
    (filter #(.endsWith (.getName %) ".tgz") (file-seq (io/file directory)))))

(defn -main
  [& args-in]
  (let [[options args banner]
        (cli args-in
          ["-h" "--help"
           "Show usage information" :default false :flag true]
          ["-d" "--directory"
           "Path to the directory containing the FITS tarballs to analyse." :default false :flag false])]

    ;; display the help message
    (if (:help options)
      (do (println banner) 0)

      ;; set up and run the analysis
      (if (not (-> (io/file (:directory options)) .isDirectory))
        (println "Parameter %s is not a directory." (:directory options))

        (doseq [entry (process-directory (:directory options))]
          (println (format "%s, %s, %s, %s" (:name entry) (:count entry) (:length entry) (:time entry))))))))
