package com.wikia.webdriver.testcases.hubstests;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.annotations.RelatedIssue;
import com.wikia.webdriver.common.core.annotations.User;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.dataprovider.HubsDataProvider;
import com.wikia.webdriver.common.properties.Credentials;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.HomePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.HubBasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject.HubName;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialManageWikiaHome;

/**
 * @author Karol 'kkarolk' Kujawiak
 * @author Michal 'justptT' Nowierski
 * @author Robert 'rochan' Chan
 * @ownership Content X-Wing
 */
public class HubsTests extends NewTestTemplate {

  @DataProvider
  private final Object[][] provideHubName() {
    return new Object[][] { {HubName.VIDEO_GAMES}, {HubName.ENTERTAINMENT}, {HubName.LIFESTYLE}};
  }

  @Test(enabled = false, groups = {"HubsTest_001", "Hubs", "Smoke4"},
      dataProviderClass = HubsDataProvider.class, dataProvider = "provideHubDBName")
  public void HubsTest_001_verifyMosaicSliderShowsImagesOnHover(String hubDBName) {
    HomePageObject home = new HomePageObject(driver);
    HubBasePageObject hub = home.openHubByUrl(urlBuilder.getUrlForWiki(hubDBName));
    hub.verifyMosaicSliderImages();

    hub.mosaicSliderHoverOverImage(4);
    String currentLargeImageDescription = hub.mosaicSliderGetCurrentLargeImageDescription();

    hub.mosaicSliderHoverOverImage(3);
    hub.mosaicSliderVerifyLargeImageDescriptionDifferent(currentLargeImageDescription);
    currentLargeImageDescription = hub.mosaicSliderGetCurrentLargeImageDescription();

    hub.mosaicSliderHoverOverImage(2);
    hub.mosaicSliderVerifyLargeImageDescriptionDifferent(currentLargeImageDescription);
    currentLargeImageDescription = hub.mosaicSliderGetCurrentLargeImageDescription();

    hub.mosaicSliderHoverOverImage(1);
    hub.mosaicSliderVerifyLargeImageDescriptionDifferent(currentLargeImageDescription);
    currentLargeImageDescription = hub.mosaicSliderGetCurrentLargeImageDescription();

    hub.mosaicSliderHoverOverImage(0);
    hub.mosaicSliderVerifyLargeImageDescriptionDifferent(currentLargeImageDescription);
  }


  @Test(groups = {"HubsTest_002", "Hubs"}, dataProviderClass = HubsDataProvider.class,
      dataProvider = "provideHubDBName")
  /**
   *  verify that from community module has its elements
   */
  public void HubsTest_002_verifyFromCommunityModuleHasItsElements(String hubDBName) {
    HomePageObject home = new HomePageObject(driver);
    HubBasePageObject hub = home.openHubByUrl(urlBuilder.getUrlForWiki(hubDBName));
    hub.verifyFromModuleHasImages();
    hub.verifyFromModuleHasHeadline();
    hub.verifyFromModuleHasUserAndWikiField();
    hub.verifyFromModuleHasQuatation();
  }

  @Test(groups = {"HubsTest_003", "Hubs"}, dataProviderClass = HubsDataProvider.class,
      dataProvider = "provideHubDBName")
  /**
   * click on 'Get Promoted' button and verify if modal appears and if its fields/buttons are working properly
   */
  @Execute(asUser = User.USER_2)
  public void HubsTest_003_VerifyArticleSuggestionWorksProperly(String hubDBName) {
    HubBasePageObject hub =
        new HubBasePageObject(driver).openHubByUrl(urlBuilder.getUrlForWiki(hubDBName));
    hub.clickGetPromoted();
    hub.verifySuggestAVideoOrArticleModalAppeared();
    hub.verifySuggestAVideoOrArticleModalTopic();
    hub.verifySuggestVideoOrArticleButtonNotClickable();
    hub.suggestArticleTypeIntoWhatVideoField(hub.getTimeStamp());
    hub.suggestArticleTypeIntoWhyCoolField(hub.getTimeStamp());
    hub.verifySuggestVideoOrArticleButtonClickable();
    hub.closeSuggestAVideoOrArticleByXButton();
    hub.verifySuggestAVideoOrArticleModalDisappeared();
    hub.clickGetPromoted();
    hub.verifySuggestAVideoOrArticleModalAppeared();
    hub.verifySuggestAVideoOrArticleModalTopic();
    hub.verifySuggestVideoOrArticleButtonNotClickable();
    hub.closeSuggestAVideoOrArticleCancelButton();
    hub.verifySuggestAVideoOrArticleModalDisappeared();
  }

  /**
   * skipped due "promoted wikis" feature
   */
  @Test(enabled = false, groups = {"HubsTest_004", "Hubs"})
  @Execute(asUser = User.STAFF)
  public void HubsTests_004_VerifyCorporateSlotCollection() {
    SpecialManageWikiaHome manageWikia =
        new HubBasePageObject(driver).openSpecialManageWikiaHomePage(wikiCorporateURL);
    Map<String, Integer> slotDesiredSetup = manageWikia.getSlotSetup();
    HomePageObject home = new HomePageObject(driver).openCorporateHomePage(wikiCorporateURL);
    Map<String, Integer> slotCurrentSetup = home.getVisualizationWikisSetup();
    home.verifyVisualizationURLs(slotDesiredSetup, slotCurrentSetup);
  }

  @Test(groups = {"HubsTest_005", "Hubs", "new"})
  /**
   * Verify that each language drop down  goes to the correct page
   */
  public void HubsTest_005_VerifyLanguagesSelection() {
    HomePageObject home = new HomePageObject(driver);
    home.openCorporateHomePage(wikiCorporateURL);
    home.verifyLanguageDropdownURLs();
  }

  /**
   * Verify that links in WikiaBar are working
   */
  @Test(dataProvider = "provideHubName", groups = {"HubsTest_007", "Hubs"})
  public void HubsTest_006_VerifyLinkInWikiaBar(HubName hubName) {
    HomePageObject home = new HomePageObject(driver);
    home.logOut(wikiURL);
    home.openCorporateHomePage(wikiCorporateURL);
    HubBasePageObject hub = new HubBasePageObject(driver);
    hub.clickWikiaBarLink(hubName);
    hub.verifyHubTitle(hubName);
  }
}
