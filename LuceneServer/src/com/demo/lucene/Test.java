package com.demo.lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

public class Test {

	// Set directories for Index and Data Files
	String indexDir = "files/Index";
	String dataDir = "files/Data";

	Indexer indexer = null;
	SearchEngine searchEngine = null;

	// Test Lucene Application
	public static void main(String[] args) {
		Test tester;
		try {
			tester = new Test();
			tester.createIndex();
			tester.search("Audi");
			tester.sortUsingRelevance("cord1.txt");
			tester.sortUsingIndex("cord1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	// build Index based on .txt Documents in Directory
	private void createIndex() throws IOException {
		// create Index
		indexer = new Indexer(indexDir);
		int numIndexed;
		long startTime = System.currentTimeMillis();

		// return number of indexed Files
		numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
		long endTime = System.currentTimeMillis();

		// close Indexer
		indexer.close();
		System.out.println(numIndexed + " File indexed, time taken: "
				+ (endTime - startTime) + " ms");
	}

	// test search in Index
	private void search(String searchQuery) throws IOException, ParseException {
		// create SearchEngine Object
		searchEngine = new SearchEngine(indexDir);
		long startTime = System.currentTimeMillis();
		// retrieve Search Results (Hits)
		TopDocs hits = searchEngine.search(searchQuery);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " document(s) found. Time :"
				+ (endTime - startTime));
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searchEngine.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(Constants.FILE_PATH));
		}
		searchEngine.close();
	}

	private void sortUsingRelevance(String searchQuery) throws IOException,
			ParseException {
		searchEngine = new SearchEngine(indexDir);
		long startTime = System.currentTimeMillis();

		// create a term to search file name
		Term term = new Term(Constants.FILE_NAME, searchQuery);

		// create the term query object
		Query query = new FuzzyQuery(term);

		// do the search
		TopDocs docs = searchEngine.search(query, Sort.RELEVANCE);
		long endTime = System.currentTimeMillis();

		ScoreDoc[] filterScoreDosArray = docs.scoreDocs;

		System.out.println(docs.totalHits + " document(s) found. Time :"
				+ (endTime - startTime) + "ms");

		for (int i = 0; i < filterScoreDosArray.length; ++i) {
			Document doc = searchEngine.getDocument(filterScoreDosArray[i]);
			System.out.println("File: " + doc.get(Constants.FILE_PATH));
		}

		searchEngine.close();
	}

	private void sortUsingIndex(String searchQuery) throws IOException,
			ParseException {
		searchEngine = new SearchEngine(indexDir);
		long startTime = System.currentTimeMillis();

		// create a term to search file name
		Term term = new Term(Constants.FILE_NAME, searchQuery);

		// create the term query object
		Query query = new FuzzyQuery(term);

		// do the search
		TopDocs docs = searchEngine.search(query, Sort.INDEXORDER);
		long endTime = System.currentTimeMillis();
		ScoreDoc[] filterScoreDosArray = docs.scoreDocs;

		System.out.println(docs.totalHits + " document(s) found. Time :"
				+ (endTime - startTime) + "ms");

		for (int i = 0; i < filterScoreDosArray.length; ++i) {
			Document doc = searchEngine.getDocument(filterScoreDosArray[i]);
			System.out.println("File: " + doc.get(Constants.FILE_PATH));
		}

		searchEngine.close();
	}
}