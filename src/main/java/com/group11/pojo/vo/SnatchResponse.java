package com.group11.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xu Haitong
 * @since 2021/11/3 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnatchResponse {
    @JsonProperty("envelope_id")
    private Long envelopeId;

    @JsonProperty("max_count")
    private Long maxCount;

    @JsonProperty("cur_count")
    private Long curCount;
}
