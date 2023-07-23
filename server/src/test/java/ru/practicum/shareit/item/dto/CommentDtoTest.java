package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.mapper.CommentMapper;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtils.COMMENT;
import static ru.practicum.shareit.TestUtils.asString;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTest {
    private static Validator validator;
    private final JacksonTester<CommentDto> tester;

    @BeforeAll
    static void initializeValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @SneakyThrows
    void testSerialize() {
        var dto = CommentMapper.mapToDto(COMMENT);
        var result = tester.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).hasJsonPathStringValue("$.text");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(dto.getText());
        assertThat(result).hasJsonPathStringValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(asString(dto.getCreated()));
        assertThat(result).hasJsonPathStringValue("$.authorName");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(dto.getAuthorName());
    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        var json = "{\"id\":1,\"text\":\"ok\",\"authorName\":\"name1\",\"created\":\"2023-07-18T16:30:56.6484891\"}";
        var dto = tester.parse(json).getObject();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("ok");
        assertThat(dto.getAuthorName()).isEqualTo("name1");
        assertThat(asString(dto.getCreated())).isEqualTo("2023-07-18T16:30:56.6484891");
    }

    @Test
    void commentValidationShouldBeSuccessful() {
        var dto = CommentMapper.mapToDto(COMMENT);
        var set = validator.validate(dto);
        assertEquals(0, set.size());
    }

    @Test
    void commentValidationWithEmptyTextShouldContainsErrors() {
        var dto = CommentMapper.mapToDto(COMMENT.toBuilder().text("").build());
        var set = validator.validate(dto);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("text"));
    }

    @Test
    void commentValidationWithoutTextShouldContainsErrors() {
        var dto = CommentMapper.mapToDto(COMMENT.toBuilder().text(null).build());
        var set = validator.validate(dto);
        assertEquals(2, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("text"));
    }
}