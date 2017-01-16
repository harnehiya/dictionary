package com.dw.andro.dictionary.DataObject;

/**
 * Created by dvayweb on 01/04/16.
 */
public class WordObject {
    private String wordId;
    private String word;
    private String dateTime;
    private String WODId;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWODId() {
        return WODId;
    }

    public void setWODId(String WODId) {
        this.WODId = WODId;
    }
}
