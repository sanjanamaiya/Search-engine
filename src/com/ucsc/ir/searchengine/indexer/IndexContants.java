package com.ucsc.ir.searchengine.indexer;

import java.util.Arrays;
import java.util.List;

public class IndexContants 
{
	public static final String DOC_SEQUENTIAL_IDENTIFIER = ".I";
	public static final String DOC_MEDLINE_IDENTIFIER = ".U";
	public static final String DOC_MESH_TERMS = ".M";
	public static final String DOC_TITLE = ".T";
	public static final String DOC_PUBLICATION_TYPE = ".P";
	public static final String DOC_ABSTRACT = ".W";
	public static final String DOC_AUTHOR = ".A";
	public static final String DOC_SOURCE = ".S";
	
	// to save in the Lucene index
	public static final String CONTENTS = "contents";
	public static final String DOCID = "docid";
	public static final int MAX_SEARCH = 50;
	
	// Ohsumed query constants
	public static final String QUERY_TOP = "<top>";
	public static final String QUERY_TOP_END = "</top>";
	public static final String QUERY_NUM = "<num> Number:";
	public static final String QUERY_TITLE = "<title>";
	public static final String QUERY_DESCR = "<desc> Description:";
	
	public final static List<String> stopWords = Arrays.asList(
			"and", "of", "management", "diagnosis", "therapy",
			"for", "in", "treatment", "differential", "review", 
			"with", "to", "on", "article", "associated", "is",
			"the", "use", "when", "which", "how", "treating", 
			"are", "about", "where", "while", "does", "new",
			"for", "given", "there", "up", "can", "effect",
			"a", "effects", "treated", "advanced", "cause"
			);
	
	/*public final static List<String> stopWords = Arrays.asList(
			"and", "of", "management",
			"for", "in", "with", "to", "on", "associated", 
			"is", "the", "use", "when", "which", "how", "treating", 
			"are", "about", "where", "while", "does", "new",
			"for", "given", "there", "up", "can", "effect",
			"a", "effects", "treated"
			);
	
	public final static List<String> lowScoreWords = Arrays.asList(
			"review", "therapy", "advanced", "article",
			"best", "cause", "diagnosis", "treatment"
			);*/
	
	public enum OHSUMED 
	{
		DOC_SEQUENTIAL_IDENTIFIER (1),
		DOC_MEDLINE_IDENTIFIER (2),
		DOC_MESH_TERMS (3),
		DOC_TITLE (4),
		DOC_PUBLICATION_TYPE (5),
		DOC_ABSTRACT (6),
		DOC_AUTHOR (7),
		DOC_SOURCE (8);
		
		private int value;
		private OHSUMED(int value)
		{
			this.value = value;
		}
		
		public static OHSUMED valueOf(int value)
		{
			for (OHSUMED element : OHSUMED.values()) 
			{
				if (value == element.value)
				{
					return element;
				}
			}
			return DOC_SEQUENTIAL_IDENTIFIER;
		}
		
	};
}


