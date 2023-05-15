package com.compression.Project.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.compression.Project.lzw.LZW.*;

@RestController
@RequestMapping
public class LZWController {

    @PostMapping("/lzw/encode")
    public List<Object> encodeLZW(@RequestBody String input) {
        List<Object> encodedList = encode(input);
        return encodedList;
    }

    @PostMapping("/lzw/decode")
    public String decodeLZW(@RequestBody String input) {
        return decode(input);
    }
}
