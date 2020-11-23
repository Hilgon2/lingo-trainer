package com.lingotrainer.api.infrastructure.security.json;

import com.lingotrainer.api.domain.model.user.Role;

import java.util.HashMap;
import java.util.Map;

/**
 * Jackson only accepts *.class in the annotation @JsonView.
 * This class holds an empty class to represents all the roles.
 */
public class MyJsonView {

    public static final Map<Role, Class> MAPPING = new HashMap<>();

    static {
        MAPPING.put(Role.TRAINEE, Trainee.class);
        MAPPING.put(Role.ADMIN, Admin.class);
    }

    public interface Anonymous {}

    public interface Trainee extends Anonymous {}

    public interface Admin extends Trainee {}

}