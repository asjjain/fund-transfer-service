package com.fundtransfer.fundtransferservice.com.fundtransfer.fundtransferservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundtransfer.fundtransferservice.client.ExchangeRateApiClient;
import com.fundtransfer.fundtransferservice.controller.ExchangeRateApiController;
import com.fundtransfer.fundtransferservice.model.ExchangeResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashMap;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateApiControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;
    private ExchangeResponse exchangeResponse;

    @InjectMocks
    private ExchangeRateApiController exchangeRateApiController;


    @BeforeEach
    public void setup(){
        this.exchangeResponse = new ExchangeResponse("success", 1669422061, LocalDate.now().toString(),  "https://www.exchangerate-api.com", 0,
                "https://www.exchangerate-api.com/docs/free", new HashMap<String,Double >(),
                1669334551, "USD",LocalDate.now().toString(),"https://www.exchangerate-api.com/terms" );

        mockMvc = MockMvcBuilders.standaloneSetup(exchangeRateApiController).build();

    }

    @AfterEach
    void tearDown(){

        exchangeResponse=null;
    }

    @Test
    public void getExchangeRateFromCurrencyCode() throws Exception{

        when(exchangeRateApiClient.getExchangeRateFromCurrencyCode("USD")).thenReturn(exchangeResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/exchangeRate/USD").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exchangeResponse))).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());

        verify(exchangeRateApiClient).getExchangeRateFromCurrencyCode("USD");
        verify(exchangeRateApiClient, times(1)).getExchangeRateFromCurrencyCode("USD");

    }

    @Test
    public void shouldReturn404WhenCurrencyCodeMissingInRequest() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/exchangeRate/").contentType(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print()).andExpect(status().is4xxClientError());


    }

    @Test
    public void shouldReturn400WhenInvalidCurrencyCodeInRequest() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/exchangeRate/ABC").contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
        .andExpect(content().string("valid Currency Code is requried"));
               //  .andExpect(status().reason(containsString("valid Currency Code is requried")));


    }

    public static String  asJsonString(final Object obj) {
        try{
            return  new ObjectMapper().writeValueAsString(obj);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


}
