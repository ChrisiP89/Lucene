package com.demo.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

//This class is used to index the raw data so that we can make it searchable using lucene library
public class Indexer {

	// Lucene IndexWriter Object
	private IndexWriter indexWriter = null;

	public Indexer(String indexDirectoryPath) throws IOException {
		getIndexWriter(indexDirectoryPath, true);
	}

	// If writer object exists - use it / else create one
	public IndexWriter getIndexWriter(String indexDirectoryPath, boolean recreate)
			throws IOException {
		if (indexWriter == null) {
			// this directory will contain the indexes
			Directory indexDirectory = FSDirectory.open(new File(
					indexDirectoryPath));
		
			// create the indexer
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,
					analyzer);
			indexWriter = new IndexWriter(indexDirectory, config);
			
			if(recreate) {
				//recreate Index
				indexWriter.deleteAll();
			}

		}
		return indexWriter;
	}

	// If writer object exists - close it
	public void close() throws CorruptIndexException, IOException {
		if (indexWriter != null) {
			indexWriter.close();
			System.out.println("-- IndexWriter closed --");
		}
	}

	// Method to get a lucene document from a text file
	private Document getDocument(File file) throws IOException {
		Document document = new Document();

		// index file contents
		// only contents is to be analyzed as it can contain data (FileReader - also for big files - Content is not stored in Index)
		Field contentField = new Field(Constants.CONTENTS, new FileReader(file));
		// index file name
		Field fileNameField = new Field(Constants.FILE_NAME, file.getName(),
				Field.Store.YES, Field.Index.NOT_ANALYZED);
		// index file path
		Field filePathField = new Field(Constants.FILE_PATH,
				file.getCanonicalPath(), Field.Store.YES,
				Field.Index.NOT_ANALYZED);

		// Add the newly created fields to the document object and return it to
		// the caller method
		document.add(contentField);
		document.add(fileNameField);
		document.add(filePathField);

		return document;
	}

	// add Document to IndexWriter
	private void indexFile(File file) throws IOException {
		System.out.println("Indexing " + file.getCanonicalPath());
		Document document = getDocument(file);
		indexWriter.addDocument(document);
	}

	// Build the Index from the Files by calling indexFile() Method
	// Uses FileFilter (TextFileFilter) as Argument
	public int createIndex(String dataDirPath, FileFilter filter)
			throws IOException {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists()
					&& file.canRead() && filter.accept(file)) {
				indexFile(file);
			}
		}

		// return number of indexed Documents
		return indexWriter.numDocs();
	}
}