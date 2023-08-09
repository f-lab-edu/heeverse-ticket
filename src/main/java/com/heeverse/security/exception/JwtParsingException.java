package com.heeverse.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jeongheekim
 * @date 2023/08/02
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class JwtParsingException extends RuntimeException {

}
