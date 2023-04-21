import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Суши-маркет Тесты")
public class SushiMarketTests extends TestBaseSf {

    Faker faker = new Faker(new Locale("ru"));
    String name = faker.name().firstName(),
            number = faker.phoneNumber().subscriberNumber(10);

    @ValueSource(strings = {
            "Москва", "Череповец", "Санкт-Петербург"
    })
    @ParameterizedTest(name = "Выбрать город {0}")
    @DisplayName("Выбор городов для доставки")
    void selectСity(String city) {
        step("Ищем город " + city, () -> {
            $(".citySelect-searchInput").setValue(city);
        });
        step("Выбераем город " + city, () -> {
            $(".citySelect-modal_link").click();
            $(".deliveryMenu_click").shouldHave(exactText(city));
        });
        step("Проверяем, что выбран город " + city, () -> {
            $(".deliveryMenu_click").shouldHave(exactText(city));
        });
    }

    @Test
    @DisplayName("Поиск несущестующего города")
    void citySearch() {
        step("Ищем город Луна", () -> {
            $(".citySelect-searchInput").setValue("Луна");
        });
        $(".citySelect-noSearch").shouldHave(exactText("Мы еще не открылись в этом городе"));
    }

    @Test
    @DisplayName("Переход на строницу \"О нас\"")
    void aboutUs() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем строницу \"О нас\"", () -> {
            $("a[href=\"/about\"]").click();
        });
        step("Проверяем, что открылась тарница \"О нас\"", () -> {
            $(".menuGoods_title").shouldHave(exactText("О нас"));
        });
    }

    @Test
    @DisplayName("Отправить форму \"Заказать звонок\"")
    void orderСall() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Нажимаем - Перезвоните мне", () -> {
            $(byText("Перезвоните мне")).click();
        });
        step("Заполняем поле - Имя", () -> {
            $("[name=name]").setValue(name);
        });
        step("Заполняем поле - Телефон", () -> {
            $(".tel").setValue("").setValue(number);
        });
        step("Отправляем форму \"Заказать звонок\"", () -> {
            $(".btn_form").click();
        });
    }

    @Test
    @DisplayName("Оформление заказа - Самовывоз")
    void orderPickup() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Выбераем способ доставки - Самовывоз", () -> {
            $(byText("Самовывоз")).click();
        });
        step("Заполняем адрес доставки", () -> {
            $(".shop-item ").click();
            $(".paymentOrder_btn").click();
        });
        step("Переходив во вкладку - Наборы", () -> {
            $(byText("Наборы")).click();
        });
        step("Добовляем набор роллов в карзину", () -> {
            $(byText("Выбрать")).click();
        });
        step("Открываем корзину", () -> {
            $(".yourOrder_slidingTrigger").hover();
            $(byText("Оформить")).click();
        });
        step("Нажимаем - Далее", () -> {
            $("#make-order-button").scrollTo().click();

        });
        step("Заполняем контактные данные", () -> {
            $("[name=name]").setValue(name);
            $(".tel").setValue("").setValue(number);
        });
        step("Выбераем способ оплаты - Наличные", () -> {
            $(byText("Наличные")).scrollTo().click();
        });
        step("Заполняем время получения заказа", () -> {
            $x("//input[@value='time']/..").click();
            $x("//input[@value='time']/..").click();
        });
        step("Нажимаем - Оформить", () -> {
            $(byText("Оформить")).click();
        });
        step("Проверяме, что появилось сообщение об оформлении заказа", () -> {
            $(".order-end-msg-content")
                    .shouldHave(text("Скоро вам поступит смс уведомление или звонок оператора о подтверждении заказа."));
        });
    }

    @Test
    @DisplayName("Удалить добавленный товар из корзины")
    void ydalit() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Выбераем способ доставки - Самовывоз", () -> {
            $(byText("Самовывоз")).click();
        });
        step("Заполняем адрес доставки", () -> {
            $(".shop-item ").click();
            $(".paymentOrder_btn").click();
        });
        step("Открываем страницу - Наборы", () -> {
            $(byText("Наборы")).click();
        });
        step("Добовляем набор роллов в карзину", () -> {
            $(byText("Выбрать")).click();
        });
        step("Открываем корзину", () -> {
            $(".yourOrder_slidingTrigger").hover();
            $(byText("Оформить")).click();
        });
        step("Удаляем набор из корзины", () -> {
            $("a[title=Удалить]").click();
        });
        step("Проверяем, что корзина пуста", () -> {
            $(".emptyBasket-container")
                    .shouldHave(text("Для того, чтобы сделать заказ, перейдите в меню и выберите товары"));
        });
    }

    @Test
    @DisplayName("Переход на строницу \"Партнерам\"")
    void partnersPage() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу \"Партнерам\"", () -> {
            $("a[href=\"/partners\"]").click();
        });
        step("Проверяем, что открылась страница \"Партнерам\"", () -> {
            $(".ws_title").shouldHave(exactText("Перспективное сотрудничество"));
        });
    }

    @Test
    @DisplayName("Открыть акцию  \"Вкусная регистрация\"")
    void deliciousRegistration() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу \"Акции\"", () -> {
            $("a[href=\"/actions\"]").click();
        });
        step("Открываем акцию \"Вкусная регистрация\"", () -> {
            $("a[href=\"/actions/vkusnaya-registratsiya\"]").click();
        });
        step("Проверяем, что открылась акция \"Вкусная регистрация\"", () -> {
            $(".whiteSheet").shouldHave(text("\"Вкусная регистрация\""));
        });
    }

    @Test
    @DisplayName("Вернутся на главную страницу через логотип")
    void refundViaLogo() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу \"Салаты\"", () -> {
            $(byText("Салаты")).click();
        });
        step("Нажимаем на логотип", () -> {
            $("[src=\"https://cherepovec.sushi-market.com/storage/img/logo/ru.webp\"").click();
        });
        step("Проверяем, что открылась главная страница", () -> {
            $(".hotStocks_title").shouldHave(exactText("Горячие акции"));
        });
    }

    @Test
    @DisplayName("Заказать ролы с доставкой")
    void deliveryRoll() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу с роллами", () -> {
            $(byText("Роллы")).click();
        });
        step("Выбрать адрес доставки", () -> {
            $(byText("Выбрать")).click();
            $x("//input[contains(@class, 'input-orange')][not(@disabled='disabled')]")
                    .setValue("улица Гоголя, 45");
            $(".paymentOrder_btn").click();
        });
        step("Выбираем ролл", () -> {
            $(byText("Выбрать")).click();
        });
        step("Переходим в корзину", () -> {
            $(".yourOrder_slidingTrigger").hover();
            $(byText("Оформить")).click();
        });
        step("Нажимаем \"Далее\"", () -> {
            $("a[href=\"/cart/checkout\"]").scrollTo().click();
        });
        step("Заполняем \"Адрес доставки\"", () -> {
            $("*[name='room']").setValue("5");
            $(byText("Подтвердить адрес")).click();
        });
        step("Заполняем контактные данные", () -> {
            $("[name=name]").setValue(name);
            $(".tel").setValue("").setValue(number);
        });
        step("Заполняем время получения заказа", () -> {
            $x("//input[@value='time']/..").click();
            $x("//input[@value='time']/..").click();
        });
        step("Нажимаем \"Оформить\"", () -> {
            $(byText("Оформить")).scrollTo().click();
        });
        $(byText("Онлайн оплата картой")).shouldHave(visible);

    }

    @Test
    @DisplayName("Посмотерть описания ролла ФИЛАДЕЛЬФИЯ")
    void descriptionRoll() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу с роллами", () -> {
            $(byText("Роллы")).click();
        });
        step("Выбираем ролл ФИЛАДЕЛЬФИЯ", () -> {
            $("a[href=\"/product/filadelfiya4\"]").click();
        });
        step("Проверяем, что открылось описания ролла ФИЛАДЕЛЬФИЯ", () -> {
            $(".cardsDetail_content")
                    .shouldHave(text("ФИЛАДЕЛЬФИЯ"))
                    .shouldHave(text("ПИЩЕВАЯ ЦЕННОСТЬ на 100 гр.:"));
        });
    }

    @Test
    @DisplayName("Открываем страницу \"Как оплатить\"")
    void howToPay() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу \"Как оплатить\"", () -> {
            $("a[href=\"/payment\"]").click();
        });
        step("Проверяем, что открылось описания страницы \"Как оплатить\"", () -> {
            $(".whiteSheet")
                    .shouldHave(text("При оформлении заказа Вы можете выбрать"))
                    .shouldHave(text("наиболее удобный способ оплаты:"))
                    .shouldHave(text("Оплата наличными"))
                    .shouldHave(text("Оплата банковской картой"));
        });
    }

    @Test
    @DisplayName("Посмотреть, что в напитках есть \"Дюшес\"")
    void lookingDuchessInDrinks() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Перейти на вкладку  \"Напитки\"", () -> {
            $("a[href=\"/menu/napitki\"]").click();
        });
        step("Находим \"Дюшес\"", () -> {
            $x("//p[contains(@class, 'goodsBlockName')]/a[@href='/product/dyuses']")
                    .shouldHave(text("ДЮШЕС"));
        });
    }

    @Test
    @DisplayName("Отображение номера телефона на главной странце")
    void displayingThePhoneOnTheMainPage() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Проверяем, что номер телефона в шапке сайта отображается", () -> {
            $("[itemprop=\"telephone\"]").shouldHave(visible);
        });
    }

    @Test
    @DisplayName("Стоимость заказа в карзине")
    void costOfTheOrder() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Выбрать способ доставки Самовывоз", () -> {
            $(byText("Самовывоз")).click();
        });
        step("Выбераем адрес магазина", () -> {
            $(".shop-item ").click();
            $(".paymentOrder_btn").click();
        });
        step("Открываем вкладку \"Наборы\"", () -> {
            $("a[href=\"/menu/nabory\"]").click();
        });
        step("Добовляем два набора \"Выгодный\"", () -> {
            $(".goodsBlock_btn").click();
            $(".goodsBlock_btn").click();
        });
        step("Открываем карзину", () -> {
            $(".yourOrder_slidingTrigger").hover();
            $(byText("Оформить")).click();
        });
        step("Проверяем сумму заказа", () -> {
            $("#order-sum").shouldHave(exactText("1401"));
        });
    }

    @Test
    @DisplayName("Открыть карточку супа \"Том-ям\"")
    void lookingForSoup() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Выбрать способ доставки Самовывоз", () -> {
            $(byText("Самовывоз")).click();
        });
        step("Выбераем адрес магазина", () -> {
            $(".shop-item ").click();
            $(".paymentOrder_btn").click();
        });
        step("Открываем вкладку \"Супы\"", () -> {
            $(byText("Супы")).click();
        });
        step("Открываем карточку супа \"Том-ям\"", () -> {
            $("A[href=\"/product/sup-tom-yam-set1\"]").click();
        });
        step("Проверяем, что открылась карточка супа Том-ям", () -> {
            $(".cardsDetail_content")
                    .shouldHave(text("СУП ТОМ-ЯМ"))
                    .shouldHave(text("ПИЩЕВАЯ ЦЕННОСТЬ на 100 гр.:"));
        });
    }

    @Test
    @DisplayName("Проям отображение вкладки \"ВОК\" при самовывозе")
    void goToTheWokPage() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Выбрать способ доставки Самовывоз", () -> {
            $(byText("Самовывоз")).click();
        });
        step("Выбераем адрес магазина", () -> {
            $(".shop-item ").click();
            $(".paymentOrder_btn").click();
        });
        step("Открываем вкладку \"ВОК\"", () -> {
            $(byText("Вок")).click();
        });
        step("Проверяем, что открылась страница вок", () -> {
            $("a[title='Вок']").shouldHave(text("Вок"));
        });
    }

    @Test
    @DisplayName("Оформить заказ без номера телефона")
    void placeAnOrderWithoutPhone() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу с роллами", () -> {
            $(byText("Роллы")).click();
        });
        step("Выбрать адрес доставки", () -> {
            $(byText("Выбрать")).click();
            $x("//input[contains(@class, 'input-orange')][not(@disabled='disabled')]")
                    .setValue("улица Гоголя, 45");
            $(".paymentOrder_btn").click();
        });
        step("Выбираем ролл", () -> {
            $(byText("Выбрать")).click();
        });
        step("Переходим в корзину", () -> {
            $(".yourOrder_slidingTrigger").hover();
            $(byText("Оформить")).click();
        });
        step("Нажимаем \"Далее\"", () -> {
            $("a[href=\"/cart/checkout\"]").scrollTo().click();
        });
        step("Заполняем \"Адрес доставки\"", () -> {
            $("*[name='room']").setValue("5");
            $(byText("Подтвердить адрес")).click();
        });
        step("Заполняем  поле - Имя", () -> {
            $("[name=name]").setValue(name);
        });
        step("Нажимаем \"Оформить\"", () -> {
            $(byText("Оформить")).scrollTo().click();
        });
        $(byText("Введите данные корректно!")).shouldHave(visible);
    }

    @Test
    @DisplayName("Изменение количества выбраных роллов")
    void changingTheNumberOfRolls() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Открываем страницу с роллами", () -> {
            $(byText("Роллы")).click();
        });
        step("Выбрать адрес доставки", () -> {
            $(byText("Выбрать")).click();
            $x("//input[contains(@class, 'input-orange')][not(@disabled='disabled')]")
                    .setValue("улица Гоголя, 45");
            $(".paymentOrder_btn").click();
        });
        step("Выбираем ролл", () -> {
            $(byText("Выбрать")).click();
        });
        step("Увеличиваем количества выбраных роллов до 8", () -> {
            $(".plus").click();
            $(".basketQuantity").shouldHave(exactText("8"));
        });
        step("Уменьшаем количества выбраных роллов до 4", () -> {
            $(".minus").click();
            $(".basketQuantity").shouldHave(exactText("4"));
        });
    }

    @Test
    @DisplayName("Открыть карточку - Палочек")
    void openDescriptionSticks() {
        step("Ищем город череповец", () -> {
            $(".citySelect-searchInput").setValue("Череповец");
        });
        step("Выбераем город Череповец", () -> {
            $(".citySelect-modal_link").click();
        });
        step("Выбрать способ доставки Самовывоз", () -> {
            $(byText("Самовывоз")).click();
        });
        step("Выбераем адрес магазина", () -> {
            $(".shop-item ").click();
            $(".paymentOrder_btn").click();
        });
        step("Открываем вкладку - Запеченные роллы", () -> {
            $(byText("Запеченные роллы")).click();
        });
        step("Открываем карточку супа \"Том-ям\"", () -> {
            $("A[href=\"/product/palocki\"]").click();
        });
        step("Проверяем, что открылась карточка - Палочки", () -> {
            $(".cardsDetail_content")
                    .shouldHave(text("ПАЛОЧКИ"))
                    .shouldHave(text("ПИЩЕВАЯ ЦЕННОСТЬ на 100 гр.:"));
        });
    }
}

