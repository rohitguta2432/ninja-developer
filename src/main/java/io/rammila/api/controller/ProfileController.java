package io.rammila.api.controller;

import io.rammila.api.model.Profile;
import io.rammila.api.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
@Api(description = "API for Profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @ApiOperation(value = "create profile")
    @ApiResponses({@ApiResponse(code = 200, message = "user profile create"),
            @ApiResponse(code = 401, message = "Access token is not valid"),
            @ApiResponse(code = 404, message = "Profile Not Found")})
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PROFILE')")
    public ResponseEntity<?> create(@RequestBody @Valid Profile profile) {
        return new ResponseEntity<>(profileService.create(profile), HttpStatus.OK);
    }

    @ApiOperation(value = "get profile")
    @ApiResponses({@ApiResponse(code=200,message = "Fetching all profiles"),
    @ApiResponse(code=404,message = "Profile is Not Found"),
    @ApiResponse(code=401,message = "Access Token is Not Found")})
    @GetMapping
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(profileService.all(), HttpStatus.OK);
    }


}
