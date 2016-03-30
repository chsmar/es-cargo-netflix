package com.example.myeventsourcing.event.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Administrador on 02/12/2015.
 */

@Component
public class JsonTool {

    @Autowired
    private HttpMessageConverter<?>[] converters;

    public String json(Object o) throws IOException {
        HttpMessageConverter mappingJackson2HttpMessageConverter = Arrays.asList(getConverters()).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    public HttpMessageConverter<?>[] getConverters() {
        return converters;
    }
}
