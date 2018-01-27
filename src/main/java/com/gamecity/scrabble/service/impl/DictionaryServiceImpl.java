package com.gamecity.scrabble.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gamecity.scrabble.service.DictionaryService;

@Service(value = "dictionaryService")
public class DictionaryServiceImpl implements DictionaryService
{
    private static final Map<String, List<String>> wordMap = new HashMap<String, List<String>>();
    private static List<String> languages;

    @Value("${dictionary.path}")
    private String dictionaryPath;

    @Value("${dictionary.languages}")
    private String dictionaryLanguages;

    @PostConstruct
    public void init()
    {
        languages = Arrays.asList(dictionaryLanguages.split(","));
        languages.forEach(language -> loadWords(language));
    }

    private void loadWords(String language)
    {
        try
        {
            List<String> words = new ArrayList<String>();
            try (BufferedReader br = new BufferedReader(new FileReader(String.format(dictionaryPath, language))))
            {
                String line = br.readLine();

                while (line != null)
                {
                    words.add(line.toLowerCase(new Locale(language)));
                    line = br.readLine();
                }
            }
            wordMap.put(language, words);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isWordValid(String word, String language)
    {
        List<String> words = wordMap.get(language);
        return words.contains(word.toLowerCase(new Locale(language)));
    }
}
