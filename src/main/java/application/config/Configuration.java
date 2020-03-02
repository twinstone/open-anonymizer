package application.config;

import application.datasource.DataSource;
import application.model.describer.EntityDescriber;

import java.util.List;
import java.util.Locale;

public class Configuration {
    private Locale locale;
    private String dictionaryPath;
    private DataSource inputSource;
    private DataSource outputSource;
    private List<EntityDescriber> entities;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDictionaryPath() {
        return dictionaryPath;
    }

    public void setDictionaryPath(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
    }

    public DataSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(DataSource inputSource) {
        this.inputSource = inputSource;
    }

    public DataSource getOutputSource() {
        return outputSource;
    }

    public void setOutputSource(DataSource outputSource) {
        this.outputSource = outputSource;
    }

    public List<EntityDescriber> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityDescriber> entities) {
        this.entities = entities;
    }
}
