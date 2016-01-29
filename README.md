# search-engine

The purpose of this project is to read the Cranfield collection (The Cranfield collection was the pioneering test collection in allowing precise quantitative measures of information retrieval effectiveness.) including 1400 text documents and index all the words. 

We have used Porter Stemmer to get the stem of each word and also Google's list of stop words to remove the words that create ambiguity and causes server load problems. Stemming and removing stop words ultimately reduces the number of indices which means faster processing time.

Through this project we have used different methods and techniques that are highly used for building search engines such as:

- Creating Inverted files of the collection storing word frequency and IDs using Vector Space Model 
- Stemming 
- Ranking the collection using TF and IDF measures
- Sorting the collection based on the Cosine measure
- Evaluating queries, searching and returning the desirable results based on the weight of the documents
- Using Recall and Precision scores for each query in the query text  

This project had been implemented in both Java and Python programming languages. 
