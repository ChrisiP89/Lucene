package com.demo.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

// WebSearch Class Wrapper for index.jsp Frontend
public class WebSearch {

	// Set directories for Index and Data Files !!!
	String indexDir = "/Users/Christoph/FH-Dateien/Semantic Text Analysis/Lucene/LuceneServer/files/Index";
	String dataDir = "/Users/Christoph/FH-Dateien/Semantic Text Analysis/Lucene/LuceneServer/files/Data";

	Indexer indexer = null;
	SearchEngine searchEngine = null;

	public WebSearch() throws IOException {
		super();
		createIndex();
	}

	// build Index based on Text Documents in specified Directory
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

	// search query String in Index
	public String search(String searchQuery) throws IOException, ParseException {
		try {
			// create SearchEngine Object
			searchEngine = new SearchEngine(indexDir);
			long startTime = System.currentTimeMillis();
			// retrieve Search Results (Hits)
			TopDocs hits = searchEngine.search(searchQuery);
			long endTime = System.currentTimeMillis();

			// Build String for Result
			String s = "<h5>" + hits.totalHits + " document(s) found. Time: "
					+ (endTime - startTime) + "</h5>";
			
			// Iterate through documents and print information
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = searchEngine.getDocument(scoreDoc);
				
				// get Content of Text File
				String content = readFile(doc.get(Constants.FILE_PATH));
				
				// Add information to result String
				s += "<p><b>Filename: </b>" + doc.get(Constants.FILE_NAME) 
				  + "<br><b>Filepath: </b>" + doc.get(Constants.FILE_PATH) 
				  + "<br><b>Content: </b>" + content +"</p><hr>";
			}
			searchEngine.close();
			return s;
		} catch (Exception e) {
			return "<p class='alert alert-danger' style='width: 335px'>Nothing found!</p>";
		}
	}
	
	// File Content is not stored in Index - so we have to read it directly from the file (result)
	public String readFile(String filename)
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}

}