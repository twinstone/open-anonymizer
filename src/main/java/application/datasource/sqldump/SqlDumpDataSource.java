package application.datasource.sqldump;

import application.datasource.DataSource;
import application.datasource.SqlUtils;
import application.model.dataset.DataSet;
import application.model.dataset.DataSetImpl;
import application.model.dataset.EmptyDataSet;
import application.model.describer.EntityDescriber;
import application.model.mapper.EntityWrapperMapper;
import application.model.mapper.RowMapper;
import application.model.wrapper.EntityWrapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlDumpDataSource implements DataSource {

    private static final String FILE_PATTERN = "%s/%s.sql";
    private static final Logger logger = Logger.getLogger(SqlDumpDataSource.class);
    private static final Pattern LINE_PATTERN = Pattern.compile("^INSERT\\sINTO\\s`(\\w+)`\\s\\((.*)\\)\\sVALUES\\s\\((.*)\\);$", Pattern.CASE_INSENSITIVE);

    private final File directory;

    public SqlDumpDataSource(File directory) {
        Validate.notNull(directory, "Directory must be not null.");
        this.directory = directory;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        File source = new File(String.format(FILE_PATTERN, directory.getAbsolutePath(), describer.getSource()));
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            Validate.isTrue(source.exists(), "Source file does not exist.");
            Validate.isTrue(!source.isDirectory(), "Source file is directory.");
            Validate.isTrue(source.canRead(), "Could not read from source file.");
            String line;
            EntityWrapperMapper<String[]> mapper = null;
            List<EntityWrapper> wrappers = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                Matcher lineMatcher = LINE_PATTERN.matcher(line);
                if (lineMatcher.matches()) {
                    if (mapper == null) {
                        mapper = new RowMapper(lineMatcher.group(2).split(SqlUtils.FIELD_VALUE_DELIMITER), describer);
                    }
                    wrappers.add(mapper.getFromEntity(lineMatcher.group(3).split(SqlUtils.FIELD_VALUE_DELIMITER), describer));
                }
            }
            logger.info(String.format("Reading from file [%s]. %d rows read.", source.getAbsolutePath(), wrappers.size()));
            return new DataSetImpl(wrappers, describer);
        } catch (Exception e) {
            logger.error(String.format("Exception reading from file [%s].", source.getAbsolutePath()), e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public void saveEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notEmpty(wrappers, "Wrappers must contains at least one element.");
        File file = new File(String.format(FILE_PATTERN, directory.getAbsolutePath(), describer.getSource()));
        try (FileWriter writer = new FileWriter(file, true)) {
            if (!file.exists()) file.createNewFile();
            for (final EntityWrapper wrapper : wrappers) {
                writer.append(SqlUtils.sqlInsertQuery(wrapper, SqlUtils.FIELD_VALUE_DELIMITER));
            }
            logger.info(String.format("Wrote %d lines to file [%s].", wrappers.size(), file.getAbsolutePath()));
        } catch (IOException e) {
            logger.error(String.format("Exception writing new entities to file [%s].", file.getAbsolutePath()), e);
        }
    }

    @Override
    public void updateEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveManyToManyRelation(DataSource input, EntityDescriber leftDescriber, EntityDescriber rightDescriber) {

    }
}
