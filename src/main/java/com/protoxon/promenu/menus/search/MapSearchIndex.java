package com.protoxon.promenu.menus.search;

import com.protoxon.promenu.map.MapType;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Directory.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapSearchIndex {

    private static final HashMap<MapType, Directory> indexes = new HashMap<>();
    private static final Analyzer analyzer = new StandardAnalyzer();

    public static void rebuildIndex(List<com.protoxon.promenu.map.Map> maps, MapType mapType) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        Directory index = new ByteBuffersDirectory();
        try (IndexWriter writer = new IndexWriter(index, config)) {
            for (com.protoxon.promenu.map.Map map : maps) {
                Document doc = new Document();

                doc.add(new TextField("name", map.getName(), Field.Store.YES));
                doc.add(new TextField("authors", map.getAuthors(), Field.Store.YES));
                doc.add(new TextField("keywords", String.join(" ", map.getKeywords()), Field.Store.YES));
                doc.add(new TextField("tags", String.join(" ", map.getTagKeys()), Field.Store.YES));

                writer.addDocument(doc);
            }
        }
        indexes.put(mapType, index);
    }

    public static Directory getIndex(MapType mapType) {
        return indexes.get(mapType);
    }

    public static List<com.protoxon.promenu.map.Map> search(String input, MapType mapType, int maxResults) throws Exception {
        IndexReader reader = DirectoryReader.open(getIndex(mapType));
        IndexSearcher searcher = new IndexSearcher(reader);

        // Define boosts for each field
        Map<String, Float> fieldBoosts = Map.of(
                "name", 2.0f,
                "keywords", 1.2f,
                "authors", 1.5f,
                "tags", 0.5f
        );

        float minScore = 0.5f;

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        String[] terms = input.toLowerCase().split("\\s+");

        for (String field : fieldBoosts.keySet()) {
            float boost = fieldBoosts.get(field);
            for (String term : terms) {
                Query fuzzyQuery = new FuzzyQuery(new Term(field, term), 2);
                Query prefixQuery = new PrefixQuery(new Term(field, term));

                BooleanQuery.Builder subBuilder = new BooleanQuery.Builder();
                subBuilder.add(fuzzyQuery, BooleanClause.Occur.SHOULD);
                subBuilder.add(prefixQuery, BooleanClause.Occur.SHOULD);

                Query combined = subBuilder.build();
                Query boosted = new BoostQuery(combined, boost);

                queryBuilder.add(boosted, BooleanClause.Occur.SHOULD);
            }
        }

        Query query = queryBuilder.build();
        TopDocs topDocs = searcher.search(query, maxResults);

        List<com.protoxon.promenu.map.Map> results = new ArrayList<>();
        for (ScoreDoc sd : topDocs.scoreDocs) {
            if (sd.score >= minScore) {
                Document doc = searcher.doc(sd.doc);
                String name = doc.get("name");

                com.protoxon.promenu.map.Map map = com.protoxon.promenu.map.MapRegistry.getMap(mapType, name);
                if (map != null) {
                    results.add(map);
                }
            }
        }

        reader.close();
        return results;
    }


    public static List<com.protoxon.promenu.map.Map> search(String input, MapType mapType) throws Exception {
        IndexReader reader = DirectoryReader.open(getIndex(mapType));
        IndexSearcher searcher = new IndexSearcher(reader);

        // Boost values per field
        Map<String, Float> fieldBoosts = Map.of(
                "name", 2.0f,
                "keywords", 1.2f,
                "authors", 1.5f,
                "tags", 0.5f
        );

        float  minScore = 0.5f;

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        String[] terms = input.toLowerCase().split("\\s+");

        for (String field : fieldBoosts.keySet()) {
            float boost = fieldBoosts.get(field);
            for (String term : terms) {
                BooleanQuery.Builder subBuilder = new BooleanQuery.Builder();

                // Only apply FuzzyQuery if term length > 3
                if (term.length() > 3) {
                    Query fuzzyQuery = new FuzzyQuery(new Term(field, term), 1);
                    subBuilder.add(fuzzyQuery, BooleanClause.Occur.SHOULD);
                }

                // Always apply PrefixQuery
                Query prefixQuery = new PrefixQuery(new Term(field, term));
                subBuilder.add(prefixQuery, BooleanClause.Occur.SHOULD);

                Query combined = subBuilder.build();
                Query boosted = new BoostQuery(combined, boost);

                queryBuilder.add(boosted, BooleanClause.Occur.SHOULD);
            }
        }

        Query query = queryBuilder.build();
        List<com.protoxon.promenu.map.Map> results = new ArrayList<>();

        int totalHits = searcher.count(query);
        if (totalHits > 0) {
            TopDocs topDocs = searcher.search(query, totalHits);

            for (ScoreDoc sd : topDocs.scoreDocs) {
                if (sd.score >= minScore) {
                    Document doc = searcher.doc(sd.doc);
                    String name = doc.get("name");

                    com.protoxon.promenu.map.Map map = com.protoxon.promenu.map.MapRegistry.getMap(mapType, name);
                    if (map != null) {
                        map.setSearchScore(sd.score);
                        results.add(map);
                    }
                }
            }
        }

        reader.close();
        return results;
    }

}

