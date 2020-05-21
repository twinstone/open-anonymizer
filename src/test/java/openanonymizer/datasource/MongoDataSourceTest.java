package openanonymizer.datasource;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import openanonymizer.datasource.mongo.MongoDataSource;
import openanonymizer.model.dataset.DataSet;
import openanonymizer.model.dataset.DataSetImpl;
import openanonymizer.model.dataset.PagedDataSet;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.log4j.xml.DOMConfigurator;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;

/**
 * Tests mongodb database datasource.
 */
public class MongoDataSourceTest {

    private EntityWrapper wrapper;
    private EntityDescriber simple;
    private MongoDatabase database;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        DOMConfigurator.configure("log4j.xml");
        FieldDescriber field = new FieldDescriber();
        field.setName("name");
        FieldDescriber id = new FieldDescriber();
        id.setName("id");
        simple = new EntityDescriber();
        simple.setId("id");
        simple.setName("TestEntity");
        simple.setSource("test");
        simple.setFields(Arrays.asList(id, field));
        simple.setRelationFields(Collections.emptyList());
        Map<String, Object> map = new HashMap<>();
        map.put("name", "TestName");
        map.put("id", 1);
        wrapper = new EntityWrapperImpl(map, simple);

        Document document = new Document(map);
        database = PowerMockito.mock(MongoDatabase.class);
        MongoCollection<Document> collection = PowerMockito.mock(MongoCollection.class);
        FindIterable<Document> iterable = PowerMockito.mock(FindIterable.class);
        Spliterator<Document> spliterator = Collections.singletonList(document).spliterator();
        PowerMockito.when(iterable.spliterator()).thenReturn(spliterator);
        PowerMockito.when(collection.find()).thenReturn(iterable);
        PowerMockito.when(collection.countDocuments()).thenReturn(1L);
        PowerMockito.doAnswer((Answer<Void>) invocation -> {
            wrapper.insert("documentId", "test");
            return null;
        }).when(collection).insertMany(anyList());
        PowerMockito.doAnswer((Answer<Void>) invocation -> {
            wrapper.insert("updatedValue", "test");
            return null;
        }).when(collection).updateOne(isA(Bson.class), isA(Bson.class));
        PowerMockito.when(database.getCollection(anyString(), eq(Document.class))).thenReturn(collection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateRelations() {
        new MongoDataSource(database).saveRelationEntity(null, null);
    }

    @Test
    public void readDataSet() {
        MongoDataSource dataSource = new MongoDataSource(database);
        DataSet dataSet = dataSource.readDataSet(simple);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(1L, dataSet.size());
    }

    @Test
    public void readPagedDataSet() {
        MongoDataSource dataSource = new MongoDataSource(database);
        PagedDataSet dataSet = dataSource.readPage(simple, 0, 10);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(1L, dataSet.size());
        Assert.assertEquals(1L, dataSet.getTotalItems());
    }

    @Test
    public void saveEntitiesTest() {
        MongoDataSource dataSource = new MongoDataSource(database);
        dataSource.saveEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
        Assert.assertEquals("test", wrapper.getValue("documentId"));
    }

    @Test
    public void updateEntitiesTest() {
        MongoDataSource dataSource = new MongoDataSource(database);
        dataSource.updateEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
        Assert.assertEquals("test", wrapper.getValue("updatedValue"));
    }

}
