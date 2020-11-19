package com.lingotrainer.api.application.dictionary.base;

import com.google.gson.Gson;
import com.lingotrainer.api.application.dictionary.DictionaryService;
import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseDictionaryService implements DictionaryService {

    @Override
    public void save(Dictionary dictionary, MultipartFile file) {
        Gson gson = new Gson();
        Map<String, List<String>> wordsJson = new HashMap<>();

        try {
            FileWriter targetFileWriter = new FileWriter(String.format("src/main/resources/dictionary/%s.json", dictionary.getLanguage()));
            List<String> additionalWords = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() >= 5 && line.length() <= 7 && line.chars().allMatch(Character::isLetter) && !dictionary.getWords().contains(line)) {
                        additionalWords.add(line);
                    }
                }
            }
            dictionary.getWords().addAll(additionalWords);
            wordsJson.put("words", dictionary.getWords());
            targetFileWriter.write(gson.toJson(wordsJson));
            targetFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
