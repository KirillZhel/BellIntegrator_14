package ru.yandex.market;

import core.BaseTest;
import core.Helper;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.YandexMainPage;
import pages.YandexMarketMainPage;

public class MyTests extends BaseTest {
    @Feature("Проверка тайтла")
    @DisplayName("Проверка тайтла сайта")
    @Test
    public void MyTest() {
        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        yandexMainPage.open();
        yandexMainPage.popupMarketButton.click();

        System.out.println(driver.getCurrentUrl());
        Helper.switchToLastTab(driver);
        System.out.println(driver.getCurrentUrl());

        YandexMarketMainPage yandexMarketMainPage = new YandexMarketMainPage(driver);
    }
}
