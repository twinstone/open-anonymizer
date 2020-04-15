package openanonymizer.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.mapper.MappingException;
import openanonymizer.model.mapper.RowMapper;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RowMapperTest {

    private RowMapper mapper;
    private EntityDescriber describer;
    private EntityWrapper wrapper;

    @Before
    public void before() {
        describer = new EntityDescriber();
        FieldDescriber f1 = new FieldDescriber();
        f1.setName("name");
        FieldDescriber f2 = new FieldDescriber();
        f2.setName("age");
        describer.setFields(Arrays.asList(f1, f2));
        Map<String, Object> map = new HashMap<>();
        map.put("name", "TestName");
        map.put("age", 10);
        wrapper = new EntityWrapperImpl(map, describer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyFieldsTest() {
        mapper = new RowMapper(new String[0], null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyFieldsTest2() {
        mapper = new RowMapper(Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void nullDescriberTest() {
        mapper = new RowMapper(new String[1], null);
    }

    @Test(expected = MappingException.class)
    public void fromEntityTestWithEx() {
        mapper = new RowMapper(new String[]{"name", "age"}, describer);
        mapper.getFromEntity(new String[]{"TestName"}, describer);
    }

    @Test
    public void fromEntityTest() {
        mapper = new RowMapper(new String[]{"name", "age"}, describer);
        EntityWrapper wrapper = mapper.getFromEntity(new String[]{"TestName", "10"}, describer);
        Assert.assertEquals("TestName", wrapper.getValue("name"));
        Assert.assertEquals("10", wrapper.getValue("age"));
    }

    @Test(expected = MappingException.class)
    public void fromWrapperWithEx() {
        mapper = new RowMapper(Collections.singletonList("first_name"));
        mapper.getFromWrapper(wrapper);
    }

    @Test
    public void fromWrapperTest() {
        mapper = new RowMapper(new String[]{"name", "age"}, describer);
        String[] columns = mapper.getFromWrapper(wrapper);
        Assert.assertNotNull(columns);
        Assert.assertNotEquals(0, columns.length);
        Assert.assertEquals("TestName", columns[0]);
        Assert.assertEquals("10", columns[1]);
    }
}
