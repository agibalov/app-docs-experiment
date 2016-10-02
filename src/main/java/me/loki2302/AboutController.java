package me.loki2302;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/about")
public class AboutController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getAbout() {
        AboutApiDto aboutApiDto = new AboutApiDto();
        aboutApiDto.description = "Some very useful description";
        aboutApiDto.version = "1.0";
        return ResponseEntity.ok(aboutApiDto);
    }
}
