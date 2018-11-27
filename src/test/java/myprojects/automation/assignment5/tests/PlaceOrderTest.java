package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.model.UserDate;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaceOrderTest extends BaseTest {

    ProductData product = null;
    UserDate user = UserDate.generateRandom();

    @Test
    public void checkSiteVersion() {
        actions.openShopPage();

        Assert.assertEquals(isMobileTesting, actions.checkMobileMode(), "Mismatch mode of open site and selected mode.");


    }

    @Test
    public void createNewOrder() {
        // open random product
        actions.openRandomProduct();

        // save product parameters
        product = actions.getOpenedProductInfo();

        // add product to Cart and validate product information in the Cart
        actions.addProductToBasket(product);

        // proceed to order creation, fill required information
        actions.fillPageOfBuyProduct(user);
        actions.confirmDeliveryType();
        actions.confirmPayType();

        // place new order and validate order summary
        actions.checkOrder(product);

        // check updated In Stock value
        actions.openShopPage();
        actions.searchProduct(product.getName());
        actions.checkChangeOfQuantity(product);
    }

}
