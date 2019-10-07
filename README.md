# Document Indexer
* This repository provides APIs of retrieval from Shakespeare's collection of plays. 
  * Even though this API works on Shakespeare's dataset most of its components are modularized so that those can be changed easily to be ready for other datasets. This modularization also makes it easy to swap or replace components. Adding another compression method is easy, just add the compressor and update the “CompressorFactory” class. 
  * Given a word from one of the scenes it can calculate Dice’s coefficient. That is it can find a word whose co-occurrence with the given word is highest. 
  * Given some set of query terms, API can search through the corpus of scenes and find out which scenes are most likely to contain those terms. The facility to store the inverted index on a file system is also provided.
   It can be stored as-is (uncompressed) in binary format or it can be compressed and then stored.
  
Corpus statistics are as follows:

```
Total words in the collection: 897268
Unique words in the collection: 15635
Scenes in the collection: 748
Average length of scene: 1199.5561497326203
shortest scene: antony_and_cleopatra:2.8=47
shortest play: comedy_of_errors=16415
longest play: hamlet=32867

```
## Import Instructions

Import the project into eclipse as is.

If the jackson libraries are not imported automatically. 

`Right click project -> BuildPath -> Configure Build path -> Libraries tab -> Add external libraries -> add all libraries from the lib folder`

After that 

`Right click project -> BuildPath -> Configure Build path -> Order and Export tab -> check 3 libraries -> Apply`

## Execution Instructions


To build and write the inverted index just run `BuildAndWriteIndex`

To run `Random7TermsAndStatistics` edit run configuration and pass "compressedIndex true" or "uncompressedIndex false" as program arguments

To generate query files run `CalculateDice` It calculates dice's coefficient for 7 random terms and stores the retrieved nearest words. This process is repeated 100 times. Then it writes those terms as query terms in two files `7_tokens` and `14_tokens`

To compare two indexes use `CompareTwoIndexes`

Validate compression hypothesis by just running `CompressionHypothesisValidation`

It is important to run the classes in this order to avoid errors such as file not found etc. Or run these files in any order if you know what you are doing!

