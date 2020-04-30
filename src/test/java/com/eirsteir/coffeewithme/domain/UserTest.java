package com.eirsteir.coffeewithme.domain;


import com.eirsteir.coffeewithme.domain.user.Role;
import com.eirsteir.coffeewithme.domain.user.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private Role adminRole;

    @Before
    public void setUp() {
        user = User.builder()
                .username("Alex")
                .email("alex@email.com")
                .password("12345678")
                .roles(new ArrayList<>())
                .build();

        adminRole = Role.builder()
                .name("ADMIN")
                .build();
    }

    @Test
    public void testAddRole() {
        Role savedAdminRole = entityManager.persistAndFlush(adminRole);
        user.addRole(savedAdminRole);
        User updatedUser = entityManager.persistAndFlush(user);

        assertThat(updatedUser.getRoles()).hasSize(1);
    }

    @Test
    public void testRemoveRole() {
        Role savedAdminRole = entityManager.persistAndFlush(adminRole);
        user.addRole(savedAdminRole);
        User updatedUser = entityManager.persistAndFlush(user);

        updatedUser.removeRole(savedAdminRole);
        User userWithoutRoles = entityManager.persistFlushFind(user);

        assertThat(userWithoutRoles.getRoles()).isEmpty();
    }
}