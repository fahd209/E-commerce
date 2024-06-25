package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    ProfileDao profileDao;
    UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }


    @GetMapping("/profile")
    public Profile getProfile(Principal principal)
    {
        try {
            // getting user info
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return profileDao.getProfile(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops...our fault");
        }
    }

    @PutMapping("/profile")
    public void updateProfile(@RequestBody Profile profile, Principal principal)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            profileDao.updateProfile(userId, profile);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops...our fault");
        }
    }
}
