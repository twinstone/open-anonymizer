package application.datasource.csv;

import application.datasource.DataSource;
import application.model.dataset.DataSet;
import application.model.dataset.DataSetImpl;
import application.model.dataset.EmptyDataSet;
import application.model.describer.EntityDescriber;
import application.model.mapper.EntityWrapperMapper;
import application.model.mapper.RowMapper;
import application.model.wrapper.EntityWrapper;
import com.opencsv.*;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CsvDataSource implements DataSource {

    private static final String FILE_PATTERN = "%s/%s.csv";
    private static final Logger logger = Logger.getLogger(CsvDataSource.class);

    private final File directory;
    private final char delimiter;
    private final int skipLines;

    public CsvDataSource(File directory, char delimiter, int skipLines) {
        Validate.notNull(directory, "Directory must be not null.");
        this.directory = directory;
        this.delimiter = delimiter;
        this.skipLines = skipLines;
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        Validate.notNull(describer, "Describer must be not null.");
        File source = new File(String.format(FILE_PATTERN, directory.getAbsolutePath(), describer.getSource()));
        try {
            Validate.isTrue(source.exists(), "Source file does not exist.");
            Validate.isTrue(!source.isDirectory(), "Source file is directory.");
            Validate.isTrue(source.canRead(), "Could not read from source file.");
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(delimiter)
                    .build();
            CSVReader reader = new CSVReaderBuilder(new FileReader(source))
                    .withSkipLines(skipLines)
                    .withCSVParser(parser)
                    .build();
            EntityWrapperMapper<String[]> mapper = new RowMapper(getColumnNames(source), describer);
            List<String[]> allRows = reader.readAll();
            reader.close();
            logger.info(String.format("Reading from file [%s]. %d rows read.", source.getAbsolutePath(), allRows.size()));
            return new DataSetImpl(allRows.stream().map(r -> mapper.getFromEntity(r, describer)).collect(Collectors.toList()), describer);
        } catch (Exception e) {
            logger.error(String.format("Exception reading from file [%s].", source.getAbsolutePath()), e);
            return EmptyDataSet.build();
        }
    }

    @Override
    public void saveEntities(EntityDescriber describer, List<EntityWrapper> wrappers) {
        Validate.notNull(describer, "Describer must be not null.");
        Validate.notEmpty(wrappers, "Wrappers must contains at least one element.");
        EntityWrapperMapper<String[]> mapper = new RowMapper();
        File file = new File(String.format(FILE_PATTERN, directory.getAbsolutePath(), describer.getSource()));
        List<String> fields = describer.getFieldNames();
        if (!file.exists()) createEmptyDocument(describer.getSource(), fields);
        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
            for (final EntityWrapper wrapper : wrappers) {
                writer.writeNext(mapper.getFromWrapper(wrapper));
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

    private String[] getColumnNames(final File input) {
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String header = reader.readLine();
            return header.split(String.valueOf(delimiter));
        } catch (Exception e) {
            logger.error(String.format("Exception reading header of csv file [%s]", input.getAbsolutePath()), e);
            return null;
        }
    }

    private void createEmptyDocument(String fileName, List<String> fields) {
        String filename = String.format(FILE_PATTERN, directory.getAbsolutePath(), fileName);
        try (FileWriter writer = new FileWriter(filename)) {
            StringJoiner fieldJoiner = new StringJoiner(String.valueOf(delimiter));
            fields.forEach(fieldJoiner::add);
            writer.append(fieldJoiner.toString());
            logger.info(String.format("New csv file [%s] created with header [%s].", filename, fieldJoiner.toString()));
        } catch (IOException e) {
            logger.error(String.format("Exception creating new file [%s].", filename), e);
        }
    }
}
