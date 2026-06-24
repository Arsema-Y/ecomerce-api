package org.yearup.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;


@RestController
@RequestMapping("profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public Profile getProfile(Principal principal) {
        int userId = userService.getByUserName(principal.getName()).getId();
        return profileService.getById(userId);
    }

    @PutMapping
    public Profile updateProfile(Principal principal, @RequestBody Profile profile) {
        int userId = userService.getByUserName(principal.getName()).getId();
        return profileService.update(userId, profile);
    }
}
