# Crawl-Bot-Challenge
Challgenge given by Clear Access IP that produces a list of relevant products and companies that relate to the invention described.

H2 Steps
* Parsed the [HTML content](https://github.com/nagakrishna/Crawl-Bot-Challenge/blob/master/data/input.txt) and [extracted](https://github.com/nagakrishna/Crawl-Bot-Challenge/blob/master/data/input/filteredInput.txt) required fields like Field of the Invention, Summary, and Detailed Description using JSOUP library
* Used Standfords NLP library to perform Tokenization, Lemmatization and extracted [significant words](https://github.com/nagakrishna/Crawl-Bot-Challenge/blob/master/data/significantWords.txt) using Spark's Count Vectorizer
* With [Patentsview](http://www.patentsview.org/api/query-language.html) APIs, fetched patent's title, abstract and organization for each significant
* To calculate the relavacny of fetched details with the give patent document, used cosine similarity - [Scikit Learn](http://scikit-learn.org/stable/)
* Sorted the [results](https://github.com/nagakrishna/Crawl-Bot-Challenge/blob/master/data/output.txt) (Organization and Products) based on cosine similarity score
