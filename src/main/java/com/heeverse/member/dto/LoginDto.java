package com.heeverse.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NonNull;


/**
 * @author gutenlee
 * @since 2023/07/23
 */

@JsonSerialize
public record LoginDto(
        @JsonProperty("id") @NonNull String id,
        @JsonProperty("password") @NonNull String password
) {

}
