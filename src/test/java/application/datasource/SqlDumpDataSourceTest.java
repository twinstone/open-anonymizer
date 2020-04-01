package application.datasource;

import application.datasource.sqldump.SqlDumpDataSource;
import application.model.dataset.DataSet;
import application.model.describer.EntityDescriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SqlDumpDataSourceTest {

    private final static String DIR = "src/test/resources";
    EntityDescriber describer = new ObjectMapper().readValue(Objects.requireNonNull(this.getClass().getClassLoader().getResource("user_describer.json")), EntityDescriber.class);

    public SqlDumpDataSourceTest() throws IOException {
    }

    @Test(expected = NullPointerException.class)
    public void nullTest() {
        new SqlDumpDataSource(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperation() {
        DataSource dataSource = new SqlDumpDataSource(new File(DIR));
        dataSource.updateEntities(null, null);
    }

    @Test
    public void testRead() {
        SqlDumpDataSource dataSource = new SqlDumpDataSource(new File(DIR));
        DataSet dataSet = dataSource.readDataSet(describer);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(10L, dataSet.size());
    }

}
