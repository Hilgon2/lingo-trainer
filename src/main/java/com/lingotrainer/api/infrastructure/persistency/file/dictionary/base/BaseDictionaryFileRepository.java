package com.lingotrainer.api.infrastructure.persistency.file.dictionary.base;

import com.google.gson.Gson;
import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import com.lingotrainer.api.domain.repository.DictionaryRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDictionaryFileRepository implements DictionaryRepository {

    @Override
    public void save(Dictionary dictionary) {
        Gson gson = new Gson();
        Map<String, List<String>> wordsJson = new HashMap<>();
        wordsJson.put("words", dictionary.getWords());

        try {
            FileWriter targetFileWriter = new FileWriter(String.format("src/main/resources/dictionary/%s.json", dictionary.getLanguage()));
            targetFileWriter.write(gson.toJson(wordsJson));
            targetFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
