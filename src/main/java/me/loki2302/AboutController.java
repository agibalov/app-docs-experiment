package me.loki2302;

import me.loki2302.spring.TransactionEntryPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes an API "about" endpoint
 *
 * @stereotype controller
 */
@RestController
@RequestMapping("/api/about")
public class AboutController {
    /**
     * Provides API description and version
     *
     * @return Response entity with API details
     */
    @TransactionEntryPoint("Get API description and version")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getAbout() {
        AboutApiDto aboutApiDto = new AboutApiDto();
        aboutApiDto.description = "Some very useful description";
        aboutApiDto.version = "1.0";
        return ResponseEntity.ok(aboutApiDto);
    }
}
