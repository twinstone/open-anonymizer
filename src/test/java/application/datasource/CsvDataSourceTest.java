package application.datasource;

import application.datasource.csv.CsvDataSource;
import application.model.dataset.DataSet;
import application.model.describer.EntityDescriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CsvDataSourceTest {

    private final static String DIR = "src/test/resources";
    EntityDescriber describer = new ObjectMapper().readValue(Objects.requireNonNull(this.getClass().getClassLoader().getResource("user_describer.json")), EntityDescriber.class);

    public CsvDataSourceTest() throws IOException {
    }

    @Test(expected = NullPointerException.class)
    public void nullTest() {
        new CsvDataSource(null, ',', 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperation() {
        DataSource dataSource = new CsvDataSource(new File(DIR), ',', 0);
        dataSource.updateEntities(null, null);
    }

    @Test
    public void readDataSet() {
        CsvDataSource dataSource = new CsvDataSource(new File(DIR), ',', 1);
        DataSet dataSet = dataSource.readDataSet(describer);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(10L, dataSet.size());
    }

}
