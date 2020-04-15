package openanonymizer.datasource.neo4j;

import openanonymizer.core.storage.TransformationStorage;
import openanonymizer.datasource.DataSource;
import openanonymizer.datasource.PagedDataSource;
import openanonymizer.model.dataset.*;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.RelationFieldDescriber;
import openanonymizer.model.mapper.EntityWrapperMapper;
import openanonymizer.model.mapper.Neo4jEntityMapper;
import openanonymizer.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.Neo4jException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Data source for executing CRUD operations on Neo4j database.
 *
 * @version 0.1
 * @since Open Anonymizer 1.0.0
 */
public class Neo4jDataSource implements DataSource, PagedDataSource {

    private static final EntityWrapperMapper<Record> mapper = new Neo4jEntityMapper();
    private static final Logger logger = Logger.getLogger(Neo4jDataSource.class);

    private final Driver driver;

    public Neo4jDataSource(Driver driver) {
        Validate.notNull(driver, "Driver must be not null.");
        this.driver = driver;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        try (Session session = driver.session()) {
            Result result = session.run(Neo4jUtils.entityMatchQuery(describer));
            List<EntityWrapper> list = result.list(r -> mapper.getFromEntity(r, describer));
            logger.info(String.format("Reading from Neo4j database. %s nodes with label [%s] found.", list.size(), describer.getSource()));
            return new DataSetImpl(list, describer);
        } catch (Neo4jException e) {
            logger.error("Exception reading from Neo4j.", e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public void saveEntities(EntityDescriber describer, DataSet dataSet) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notNull(dataSet, "Data set must be nit null.");
        Session session = driver.session();
        Transaction transaction = session.beginTransaction();
        try {
            for (final EntityWrapper wrapper : dataSet) {
                String query = Neo4jUtils.entityCreateQuery(wrapper);
                Result result = transaction.run(query, wrapper.getEntityAsMap());
                long id = result.single().get(0).asLong();
                TransformationStorage.insertValue(describer.getSource(), wrapper.getId(), id);
                logger.info(String.format("Created new node with id [%s].", id));
                if (describer.getRelationFields() != null) createRelations(wrapper, id, transaction);
            }
            transaction.commit();
        } catch (Neo4jException e) {
            logger.error(String.format("Exception creating new nodes with label [%s].", describer.getName()), e);
            transaction.rollback();
        } finally {
            transaction.close();
            session.close();
        }
    }

    @Override
    public void updateEntities(EntityDescriber describer, DataSet dataSet) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notNull(dataSet, "Data set must be nit null.");
        Session session = driver.session();
        Transaction transaction = session.beginTransaction();
        try {
            for (final EntityWrapper wrapper : dataSet) {
                String query = Neo4jUtils.entityUpdateQuery(wrapper);
                transaction.run(query, wrapper.getEntityAsMap());
                logger.info(String.format("Updating node with id [%s].", wrapper.getId()));
            }
            transaction.commit();
        } catch (Neo4jException e) {
            logger.error(String.format("Exception updating nodes with label [%s].", describer.getSource()), e);
        }
    }

    @Override
    public PagedDataSet readPage(EntityDescriber describer, long offset, int limit) {
        try (Session session = driver.session()) {
            Result result = session.run(Neo4jUtils.entityPagedMatchQuery(describer, offset, limit));
            List<EntityWrapper> list = result.list(r -> mapper.getFromEntity(r, describer));
            logger.info(String.format("Reading from Neo4j database. %s nodes with label [%s] found.", list.size(), describer.getSource()));
            return new PagedDataSetImpl(list, describer, offset, 0, limit, getTotalItemsCount(describer));
        } catch (Neo4jException e) {
            logger.error("Exception reading from Neo4j.", e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public long getTotalItemsCount(EntityDescriber describer) {
        try (Session session = driver.session()) {
            Result result = session.run(Neo4jUtils.countEntitiesQuery(describer));
            return result.single().get(0).asLong();
        } catch (Neo4jException e) {
            return 0;
        }
    }

    @Override
    public void close() throws IOException {
        driver.close();
    }

    private void createRelations(EntityWrapper wrapper, Object leftId, Transaction current) {
        Validate.notNull(wrapper, "Wrapper must be not null.");
        Validate.notNull(leftId, "Id must be not null.");
        EntityDescriber describer = wrapper.describeEntity();
        for (final RelationFieldDescriber relation : describer.getRelationFields()) {
            if (RelationFieldDescriber.RelationType.MANY_TO_ONE.equals(relation.getRelationType())) {
                EntityDescriber r = new EntityDescriber();
                r.setSource(relation.getTargetSource());
                r.setName(relation.getName());
                Optional<Pair> pair = TransformationStorage.findByLeft(relation.getTargetSource(), wrapper.getValue(relation.getName()));
                if (pair.isPresent()) {
                    current.run(Neo4jUtils.relationCreateQuery(describer, r, leftId, pair.get().getRight(), relation.getName()));
                    logger.info(String.format("Created new relation [%s]-(%s)->[%s].", describer.getSource(), relation.getName(), r.getSource()));
                } else {
                    logger.warn(String.format("Could not create relation [%s]-(%s)->[%s]. Transformation for node with label [%s] and previous id [%s] not found.",
                            describer.getSource(), relation.getName(), r.getSource(), r.getSource(), wrapper.getValue(relation.getName())));
                }
            }
        }
    }
}
