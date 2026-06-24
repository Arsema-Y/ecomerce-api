package org.yearup.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;


@RestController
@RequestMapping("profile")
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Profile> getProfile(Principal principal) {
        int userId = userService.getByUserName(principal.getName()).getId();
        return ResponseEntity.ok(profileService.getById(userId));
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(Principal principal, @RequestBody Profile profile) {
        int userId = userService.getByUserName(principal.getName()).getId();
        return ResponseEntity.ok(profileService.update(userId, profile));
    }
}
