package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    private String generateDate(int daysToAdd) {
        LocalDate date = LocalDate.now().plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullySubmitForm() {
        String city = "Казань";
        String name = "Иван Петров";
        String phone = "+79000000000";
        String date = generateDate(4);

        // Заполнение города
        $("[data-test-id='city'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='city'] input").setValue(city);

        // Заполнение даты
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);

        // Заполнение ФИО
        $("[data-test-id='name'] input").setValue(name);

        // Заполнение телефона
        $("[data-test-id='phone'] input").setValue(phone);

        // Чекбокс согласия
        $("[data-test-id='agreement']").click();

        // Отправка формы
        $$("button").findBy(Condition.text("Забронировать")).click();

        // Проверка успешного уведомления
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно забронирована")).shouldBe(Condition.visible);
    }
}