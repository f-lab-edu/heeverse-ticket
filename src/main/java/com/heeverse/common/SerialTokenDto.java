package com.heeverse.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author gutenlee
 * @since 2023/08/11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SerialTokenDto {

    @Getter
    Delimiter delimiter;

    protected SerialTokenDto(Delimiter delimiter) {
        this.delimiter = delimiter;
    }
}
