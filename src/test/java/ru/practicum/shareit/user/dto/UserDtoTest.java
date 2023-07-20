package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtils.USER_1;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDtoTest {
    private static Validator validator;
    private final JacksonTester<UserDto> tester;

    @BeforeAll
    static void initializeValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @SneakyThrows
    void testSerialize() {
        var dto = UserMapper.mapToDto(USER_1);
        var result = tester.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(USER_1.getId().intValue());
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(USER_1.getName());
        assertThat(result).hasJsonPathStringValue("$.email");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(USER_1.getEmail());

    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        var json = "{\"id\":1,\"name\":\"name1\",\"email\":\"name1@email.com\"}";
        var dto = tester.parse(json).getObject();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("name1");
        assertThat(dto.getEmail()).isEqualTo("name1@email.com");
    }

    @Test
    void userValidationWithoutGroupShouldBeSuccessful() {
        var dto = UserMapper.mapToDto(USER_1);
        var set = validator.validate(dto);
        assertEquals(0, set.size());
    }

    @Test
    void userValidationWithGroupShouldBeSuccessful() {
        var dto = UserMapper.mapToDto(USER_1);
        var set = validator.validate(dto, CreateUserGroup.class);
        assertEquals(0, set.size());
    }

    @Test
    void userValidationWithoutNameShouldContainsErrors() {
        var dto = UserMapper.mapToDto(USER_1.toBuilder().name(null).build());
        var set = validator.validate(dto, CreateUserGroup.class);
        assertEquals(2, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("name"));
    }

    @Test
    void userValidationWithEmptyNameShouldContainsErrors() {
        var dto = UserMapper.mapToDto(USER_1.toBuilder().name("").build());
        var set = validator.validate(dto, CreateUserGroup.class);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("name"));
    }

    @Test
    void userValidationWithoutEmailShouldContainsErrors() {
        var dto = UserMapper.mapToDto(USER_1.toBuilder().email(null).build());
        var set = validator.validate(dto, CreateUserGroup.class);
        assertEquals(2, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("email"));
    }

    @Test
    void userValidationWithEmptyEmailShouldContainsErrors() {
        var dto = UserMapper.mapToDto(USER_1.toBuilder().email("").build());
        var set = validator.validate(dto, CreateUserGroup.class);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("email"));
    }

    @Test
    void userValidationWithWrongEmailShouldContainsErrors() {
        var dto = UserMapper.mapToDto(USER_1.toBuilder().email("foo@").build());
        var set = validator.validate(dto, CreateUserGroup.class);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("email"));
    }
}