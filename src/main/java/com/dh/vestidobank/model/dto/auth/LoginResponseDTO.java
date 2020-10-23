package com.dh.vestidobank.model.dto.auth;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponseDTO {

    String token;

}
