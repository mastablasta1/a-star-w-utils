package pl.edu.agh.idziak.asw.utils.test.grid2d.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tomasz on 27.08.2016.
 */
public class TestsJsonMapper {
    private ObjectMapper objectMapper = new ObjectMapper();

    public TestsJsonMapper() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void writeTests(OutputStream outputStream, List<TestCaseDTO> list) throws IOException {
        objectMapper.writeValue(outputStream, list);
    }

    public List<TestCaseDTO> readTests(InputStream inputStream) throws IOException {
        return Arrays.asList(objectMapper.readValue(inputStream, TestCaseDTO[].class));
    }
}
