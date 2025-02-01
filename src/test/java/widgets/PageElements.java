package widgets;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class PageElements {
    public SelenideElement videoPlayer = $(".video-player");
    public SelenideElement popup = $("[class^='wdp-popup-module']");
    public SelenideElement popupSvg = popup.$("svg");
    public SelenideElement soundOn = $("[aria-label*='Включить звук']");
    public SelenideElement soundOff = $("[aria-label*='Выключить звук']");
}
