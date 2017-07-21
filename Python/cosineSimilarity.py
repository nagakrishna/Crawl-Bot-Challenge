from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

documents = open("data\\finalDoc.txt", encoding="utf8")

tfidf_vectorizer = TfidfVectorizer()
tfidf_matrix = tfidf_vectorizer.fit_transform(documents)

file = open('data\\cosineSimilarityValues.txt', 'w')
arr = cosine_similarity(tfidf_matrix[1:], tfidf_matrix[0])

i=0
for x in arr:
    file.write(str(i) + ":"+str(x)+"\n")
    i=i+1
