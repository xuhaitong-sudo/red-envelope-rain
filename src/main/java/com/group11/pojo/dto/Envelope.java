package com.group11.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xu Haitong
 * @since 2021/11/3 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envelope {
    @JsonProperty("envelope_id")
    Long envelopeId;

    // 如果 opened 是 false 则 value 不传给前端
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long value;

    Boolean opened;

    @JsonProperty("snatch_time")
    Long snatchTime;
}
