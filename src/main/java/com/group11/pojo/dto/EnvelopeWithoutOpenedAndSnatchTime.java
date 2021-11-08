package com.group11.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xu Haitong
 * @since 2021/11/8 21:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvelopeWithoutOpenedAndSnatchTime {
    Long envelopeId;
    Long uid;
    Long value;
}
