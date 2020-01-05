package application.config;

import application.datasource.DataSource;
import application.model.describer.EntityDescriber;

import java.util.List;

public class Configuration {
    private DataSource inputSource;
    private DataSource outputSource;
    private List<EntityDescriber> entities;

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
