package com.group11.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group11.pojo.dto.Envelope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/3 15:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetWalletListResponse {
    private Long amount;

    @JsonProperty("envelope_list")
    private List<Envelope> envelopeList;
}
