package polimi.aui.sentimentaigroup6b.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polimi.aui.sentimentaigroup6b.services.ProfileService;

@RestController
@RequestMapping("/api/hr")
@AllArgsConstructor
public class HRController {

    private ProfileService profileService;
}
