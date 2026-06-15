package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        // Проверка видимости уведомления (не более 15 секунд)
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));

        // ПРАВИЛЬНАЯ ПРОВЕРКА: ищем элемент по CSS селектору, проверяем видимость и точный текст
        $("[data-test-id='notification'] .notification__title")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Успешно!"));

        // Проверяем текст с датой встречи (сервис может ошибаться!)
        String expectedText = "Встреча успешно забронирована на " + date;
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText(expectedText));
    }
}