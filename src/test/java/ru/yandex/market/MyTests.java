package ru.yandex.market;

import com.google.common.net.MediaType;
import core.BaseTest;
import core.Helper;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.devtools.v113.network.Network;
import org.openqa.selenium.devtools.v113.network.model.Response;
import org.openqa.selenium.remote.http.HttpHandler;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;
import pages.YandexMainPage;
import pages.YandexMarketMainPage;
import pages.YandexMarketSERPFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.openqa.selenium.remote.http.Contents.utf8String;

public class MyTests extends BaseTest {

    private List<Response> responses = new ArrayList<>();
    @BeforeEach
    public void before() {


    }
    @Feature("Yandex Market")
    @DisplayName("Проверка выдачи товаров")
    @Test
    public void MyTest() {
        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        yandexMainPage.open();
        yandexMainPage.popupMarketButton.click();

        Helper.switchToLastTab(driver);

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.of(100000000)));
        devTools.addListener(Network.responseReceived(), resp -> {
            responses.add(resp.getResponse());
        });

        YandexMarketMainPage yandexMarketMainPage = new YandexMarketMainPage(driver);
        yandexMarketMainPage.createCategoryLocatorString("Ноутбуки и компьютеры");
        yandexMarketMainPage.createSubcategoryLocatorString("Ноутбуки");
        yandexMarketMainPage.getAllElements();
        yandexMarketMainPage.subcategory.click();

        // driver.get("https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013&allowCollapsing=1&local-offers-first=0");
        // driver.get("https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013&allowCollapsing=1&local-offers-first=0&pricefrom=10000&priceto=900000&glfilter=7893318%3A459710%2C152981");
        YandexMarketSERPFactory yandexMarketSERPFactory = new YandexMarketSERPFactory(driver);
        //yandexMarketSERPFactory.getAllElements();
        yandexMarketSERPFactory.manufacturerFilter2.clickShowAllButton();
        yandexMarketSERPFactory.manufacturerFilter2.chooseManufacturer("Lenovo");
        yandexMarketSERPFactory.manufacturerFilter2.chooseManufacturer("HUAWEI");

        //Thread.sleep(5000);
        //Har har = proxy.getHar();
    }

    @AfterEach
    public void cleanUp() {
        responses.clear();
    }
}
