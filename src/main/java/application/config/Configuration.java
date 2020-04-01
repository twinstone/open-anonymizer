package application.config;

import application.datasource.DataSource;
import application.model.describer.EntityDescriber;
import application.model.describer.RelationEntityDescriber;

import java.util.List;
import java.util.Locale;

public class Configuration {
    private Locale locale;
    private String secret;
    private String dictionaryPath;
    private DataSource inputSource;
    private DataSource outputSource;
    private List<EntityDescriber> entities;
    private List<RelationEntityDescriber> relationEntities;
    private ValidationLevel level;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

    public List<RelationEntityDescriber> getRelationEntities() {
        return relationEntities;
    }

    public void setRelationEntities(List<RelationEntityDescriber> relationEntities) {
        this.relationEntities = relationEntities;
    }

    public ValidationLevel getLevel() {
        return level;
    }

    public void setLevel(ValidationLevel level) {
        this.level = level;
    }

    public static enum ValidationLevel {
        INFO, ERROR
    }
}
