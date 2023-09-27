package com.heeverse.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class VaultTokenNotExistException extends RuntimeException{

}
