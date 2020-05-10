package openanonymizer.config;

import openanonymizer.datasource.DataSource;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.RelationEntityDescriber;

import java.util.List;
import java.util.Locale;

/**
 * Application configuration class.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class ApplicationConfiguration {
    private Integer threads;
    private Integer pageSize;
    private Locale locale;
    private String secret;
    private String dictionaryPath;
    private DataSource inputSource;
    private DataSource outputSource;
    private List<EntityDescriber> entities;
    private List<RelationEntityDescriber> relationEntities;
    private ValidationLevel level;
    private List<String> stages;

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    public List<String> getStages() {
        return stages;
    }

    public void setStages(List<String> stages) {
        this.stages = stages;
    }

    public enum ValidationLevel {
        INFO, WARN, ERROR
    }
}
