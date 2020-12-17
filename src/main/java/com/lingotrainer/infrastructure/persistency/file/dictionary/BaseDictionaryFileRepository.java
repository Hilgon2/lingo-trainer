package com.lingotrainer.infrastructure.persistency.file.dictionary;

import com.google.gson.Gson;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.infrastructure.persistency.exception.FileIOException;
import com.lingotrainer.infrastructure.persistency.exception.LanguageNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseDictionaryFileRepository implements DictionaryRepository {

    private Gson gson = new Gson();
    private final ClassLoader classLoader = getClass().getClassLoader();
    private static final String DICTIONARY_STRING = "dictionary/%s.json";

    @Override
    public Dictionary save(Dictionary dictionary) {

        try (FileWriter targetFileWriter = new FileWriter(
                this.classLoader.getResource(".").getFile() + String.format(DICTIONARY_STRING,
                        dictionary.getLanguage()))) {
            targetFileWriter.write(this.gson.toJson(dictionary.getWords()));
        } catch (IOException e) {
            throw new FileIOException(String.format("Onbekende fout bij het openen van de woordenlijst %s",
                    dictionary.getLanguage()));
        }

        return dictionary;
    }

    @Override
    public Optional<Dictionary> findByLanguage(String language) {
        try {
            String targetFileReader = new String(Files.readAllBytes(Paths.get(
                    this.classLoader.getResource(".").getFile() + String.format(DICTIONARY_STRING, language)
            )));
            List<String> words = gson.fromJson(targetFileReader, List.class);

            return Optional.ofNullable(Dictionary.builder()
                    .language(language)
                    .words(words)
                    .build());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByWord(String languageCode, String guessedWord) {
        return this.findByLanguage(languageCode).orElseThrow(() ->
                new LanguageNotFoundException(
                        String.format("Dictionary by language %s could not be found", languageCode)))
                .getWords().contains(guessedWord);
    }

    @Override
    public String retrieveRandomWord(String languageCode, WordLength wordLength) {
        Dictionary dictionary = this.findByLanguage(languageCode).orElseThrow(() ->
                new LanguageNotFoundException(
                        String.format("Dictionary language %s could not be found", languageCode)));
        return dictionary.getRandomWord(wordLength);
    }

    @Override
    public List<String> findAvailableLanguages() {
        List<String> languages = new ArrayList<>();
        File file = new File(this.classLoader.getResource("dictionary").getFile());
        if (file.exists() && file.list() != null) {
            for (String dictionaryName : file.list()) {
                if (dictionaryName.length() > 5
                        && dictionaryName.substring(dictionaryName.length() - 5).equals(".json")) {
                    languages.add(dictionaryName.substring(0, dictionaryName.length() - 5));
                }
            }
        }

        return languages;
    }

    @Override
    public boolean delete(String language) {
        File file = new File(this.classLoader.getResource(".").getFile() + String.format(DICTIONARY_STRING, language));
        return file.delete();
    }

    @Override
    public List<String> findWordsByLanguage(String language) throws IOException {
        List<String> words = new ArrayList<>();

        // if file exists, get words list
        if (this.findByLanguage(language).isPresent()) {
            String targetFileReader = new String(Files.readAllBytes(Paths.get(
                    this.classLoader.getResource(".").getFile() + String.format(DICTIONARY_STRING, language))));
            words = gson.fromJson(targetFileReader, List.class);
        }

        return words;
    }
}
