package io.rammila.api.service;

import io.rammila.api.model.Profile;
import io.rammila.api.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;


    public Profile create(Profile profile) {
        log.info("create profile: {} ", profile);
        return profileRepository.save(profile);
    }

    public List<Profile> all(){
        log.info("fetch all profile : {} ");
        return profileRepository.findAll();
    }


}
