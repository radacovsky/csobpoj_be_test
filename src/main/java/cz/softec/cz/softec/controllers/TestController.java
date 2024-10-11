package cz.softec.cz.softec.controllers;

import cz.softec.cz.softec.integration.models.GetUsersResponse;
import cz.softec.cz.softec.interfaces.User;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/interests-of-users")
    public Map<String, List<User>> handleRequest(@RequestParam @NotEmpty List<String> interests) {
        var inputInterests = new HashSet<>(interests);
        var response = restTemplate.getForEntity("https://raw.githubusercontent.com/sustacek/java_spring_interview_assignment/refs/heads/main/server/get-users.json", GetUsersResponse.class);
        var result = new HashMap<String, List<User>>();
        for (var extUser : response.getBody().getUsers()) {
            if (extUser.getInterests() != null && !extUser.getInterests().isEmpty()) {
                extUser.getInterests().forEach(interest -> {
                    if (inputInterests.contains(interest)) {
                        if (!result.containsKey(interest)) {
                            result.put(interest, new ArrayList<>());
                        }
                        // We could cache users and not create each time a new one...
                        var user = new User();
                        user.setUserId(extUser.getUserId());
                        user.setName(extUser.getFirstName() + " " + extUser.getLastName());
                        result.get(interest).add(user);
                    }
                });
            }
        }
        inputInterests.forEach(interest -> result.putIfAbsent(interest, List.of()));
        return result;
    }
}
