package com.demo.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

// This class is used to search the indexes created by Indexer to search the requested contents
public class SearchEngine {

	// Search Lucene Objects
	IndexSearcher indexSearcher = null;
	IndexReader reader = null;
	QueryParser queryParser = null;
	Query query = null;

	// Initialize Lucene Search
	public SearchEngine(String indexDirectoryPath) throws IOException {
		Directory indexDirectory = FSDirectory
				.open(new File(indexDirectoryPath));

		reader = DirectoryReader.open(indexDirectory);
		indexSearcher = new IndexSearcher(reader);
		queryParser = new QueryParser(Version.LUCENE_40, Constants.CONTENTS,
				new StandardAnalyzer(Version.LUCENE_40));
	}

	// Search Method that accepts a Query String (gets parsed)
	public TopDocs search(String queryString) throws IOException,
			ParseException {
		query = queryParser.parse(queryString);
		return indexSearcher.search(query, Constants.MAX_SEARCH);
	}

	// Search Method that accepts a Query
	public TopDocs search(Query query) throws IOException, ParseException {
		return indexSearcher.search(query, Constants.MAX_SEARCH);
	}

	// Search Method that accepts a Query and a Sort
	public TopDocs search(Query query, Sort sort) throws IOException,
			ParseException {
		return indexSearcher.search(query, Constants.MAX_SEARCH, sort);
	}

	// get Document from scored Doc
	public Document getDocument(ScoreDoc scoreDoc)
			throws CorruptIndexException, IOException {
		return indexSearcher.doc(scoreDoc.doc);
	}

	// Close IndexReader
	public void close() throws IOException {
		reader.close();
	}
}
