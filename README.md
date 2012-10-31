# fits-analysis

A small tool to extract profiling information from FITS data.

The tar and gzip archive contains FITS results from running the FITS tool on a
single ARC file. An ARC file contains crawled characterisation data for several
hundreds web files. The FITS characterisation result for such a file is a set
of XML files, one for each web file. The profile information of a
characterisation job is in this case just defined as the difference in creation
time of the oldest and newest of the result files.

The tool outputs a line for each tar/gzip file in the given directory. Each
line has the following format:

    filename, count, length, time

where 

*filename*: The file name of the tar/gzip file

*count*: The number of web files contained in the ARC file

*length*: The file size of the tar/gzip file

*time*: The time difference in seconds between the oldest and the newest file in
the tar/gzip file.

## Usage

Build the tool using

    lein uberjar

Usage:

    Switches               Default  Desc                                                           
    --------               -------  ----                                                           
    -h, --no-help, --help  false    Show usage information                                         
    -d, --directory        false    Path to the directory containing the FITS tarballs to analyse. 
 
The tool can be run like this:
    
    java -jar fits-analysis-0.1.0-SNAPSHOT-standalone.jar -d <directory 

## License

Copyright © 2012 State and University Library, Denmark

Distributed under the Eclipse Public License, the same as Clojure.
