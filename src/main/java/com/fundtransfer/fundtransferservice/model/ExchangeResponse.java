package com.fundtransfer.fundtransferservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponse {

    private String result;
    private Integer time_next_update_unix;
    private String time_next_update_utc;
    private String provider;
    private Integer time_eol_unix;
    private String documentation;
    private Map<String,Double > rates;
    private Integer time_last_update_unix;
    private String base_code;
    private String time_last_update_utc;
    private String terms_of_use;

}
