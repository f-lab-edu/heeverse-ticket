package com.heeverse.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gutenlee
 * @since 2023/10/07
 */
@RestController
public class IndexController {

    public static final String INDEX_URI = "/index";
    @GetMapping(INDEX_URI)
    public ResponseEntity<Void> index() {
        return ResponseEntity.ok().build();
    }

}
