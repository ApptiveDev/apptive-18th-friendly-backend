package apptive.team1.friendly.domain.curation.controller;

import apptive.team1.friendly.domain.curation.service.HeartService;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;
    private final UserService userService;

    @PostMapping("/curation/{contentId}/heart")
    public ResponseEntity<Long> addHeart(@PathVariable Long contentId) {
        Account currentUser = userService.getCurrentUser();
        Long heartId = heartService.addHeart(currentUser, contentId);

        return new ResponseEntity<>(heartId, HttpStatus.OK);
    }

    @DeleteMapping("/curation/{contentId}/heart")
    public ResponseEntity<Long> deleteHeart(@PathVariable Long contentId) {
        Account currentUser = userService.getCurrentUser();
        Long heartId = heartService.deleteHeart(currentUser, contentId);

        return new ResponseEntity<>(heartId, HttpStatus.OK);
    }
}
