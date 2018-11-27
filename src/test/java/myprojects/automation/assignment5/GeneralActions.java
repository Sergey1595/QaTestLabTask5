package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.model.UserDate;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.sql.Driver;
import java.time.Duration;
import java.util.List;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
        js = (JavascriptExecutor)driver;
    }

    public void openShopPage(){
        driver.get(Properties.getBaseUrl());
    }

    public boolean checkMobileMode(){
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("contact-link")));
        WebElement contactLink = driver.findElement(By.id("contact-link"));
        if(contactLink.isDisplayed()){
            CustomReporter.logAction("Open site in standard mode");
            return false;
        }else{
            CustomReporter.logAction("Open site in mobile mode");
            return true;
        }

    }

    public void openRandomProduct() {
        //find and click button allProducts
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']")));
        WebElement btnAllProducts =  driver.findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']"));
        wait.until(ExpectedConditions.elementToBeClickable(btnAllProducts));
        js.executeScript("arguments[0].click();", btnAllProducts);

        //get numbers of products on page
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='products row']")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@itemprop='name']/a")));
        List<WebElement> FoundedProducts = driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
        Assert.assertTrue(FoundedProducts.size() > 0, "Page without products");
        CustomReporter.logAction("Found " + FoundedProducts.size() + " products.");

        //select and open product
        int numberOfproduct =  (int) ( Math.random() * FoundedProducts.size() );
        FoundedProducts = driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
        wait.until(ExpectedConditions.elementToBeClickable(FoundedProducts.get(numberOfproduct)));
        js.executeScript("arguments[0].scrollIntoView();", FoundedProducts.get(numberOfproduct));
        js.executeScript("arguments[0].click();", FoundedProducts.get(numberOfproduct));

    }

    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");

        ProductData product = null;

        //save name of product
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='product-availability']")));
        CustomReporter.logAction("Products page was open");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@itemprop='name']")));
        WebElement foundName = driver.findElement(By.xpath("//h1[@itemprop='name']"));
        wait.until(ExpectedConditions.visibilityOf(foundName));
        String stFoundName = foundName.getText();
        CustomReporter.logAction("Name of found product is " + stFoundName.toLowerCase());

        //save price of product
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@itemprop='price']")));
        WebElement priceOfProduct = driver.findElement(By.xpath("//span[@itemprop='price']"));
        String foundPrice = priceOfProduct.getText().replace("₴", "").replace(" ", "").replace(",", ".");
        CustomReporter.log("Found price: " + foundPrice);

        //check is quantity not 0
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='product-availability']")));
        WebElement quantity = driver.findElement(By.xpath("//span[@id='product-availability']"));
        if(!(quantity.getText().equalsIgnoreCase("")))
            Assert.assertNotEquals("Нет в наличии",quantity.getText().replace("\uE14B", "").substring(1), "Quantity is 0");

        //press to button Products detail
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[((@class='nav-link') or (@class='nav-link active')) and (@href='#product-details')]")));
        WebElement btnDeteilProducts = driver.findElement(By.xpath("//a[((@class='nav-link') or (@class='nav-link active')) and (@href='#product-details')]"));
        wait.until(ExpectedConditions.elementToBeClickable(btnDeteilProducts));
        btnDeteilProducts.click();

        //save quantity of product
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='product-quantities']/span")));
        WebElement fieldQuantityOfProduct = driver.findElement(By.xpath("//div[@class='product-quantities']/span"));
        wait.until(ExpectedConditions.visibilityOf(fieldQuantityOfProduct));
        String QuantityOfProduct = fieldQuantityOfProduct.getText().replaceAll("[^0-9,]", "");
        CustomReporter.log("Found quantity is " + QuantityOfProduct);
        product = new ProductData(stFoundName, Integer.valueOf(QuantityOfProduct), Float.valueOf(foundPrice));

        return product;
    }

    public void addProductToBasket(ProductData product){
        //press to btn "Add in Basket"
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn btn-primary add-to-cart']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-primary add-to-cart']")));
        WebElement btnAddToBasket = driver.findElement(By.xpath("//button[@class='btn btn-primary add-to-cart']"));
        btnAddToBasket.click();

        //Go to registration page
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='btn btn-primary']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn btn-primary']")));
        WebElement btnGoToRegistration = driver.findElement(By.xpath("//a[@class='btn btn-primary']"));
        js.executeScript("arguments[0].click();", btnGoToRegistration);

        //Check name of added product
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='product-line-info']/a")));
        WebElement nameOfAddedProduct = driver.findElement(By.xpath("//div[@class='product-line-info']/a"));
        CustomReporter.logAction("Found name is " + nameOfAddedProduct.getText().toLowerCase());
        Assert.assertEquals(nameOfAddedProduct.getText().toLowerCase(), product.getName().toLowerCase(), "Name didnt natch");

        //Check number of added products
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='js-cart-line-product-quantity form-control']")));
        WebElement fieldNumberOfAddedProducts = driver.findElement(By.xpath("//input[@class='js-cart-line-product-quantity form-control']"));
        wait.until(ExpectedConditions.visibilityOf(fieldNumberOfAddedProducts));
        String NumberOfaddedProducts = fieldNumberOfAddedProducts.getAttribute("value");
        CustomReporter.logAction("Number of added products is " + NumberOfaddedProducts);
        Assert.assertEquals("1", NumberOfaddedProducts, "Count of added product didnt match");

        //Check price of added product
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='product-price']")));
        WebElement fieldPriceOfAddedProducts = driver.findElement(By.xpath("//span[@class='product-price']"));
        wait.until(ExpectedConditions.visibilityOf(fieldPriceOfAddedProducts));
        String priceOfAddedProduct = fieldPriceOfAddedProducts.getText().replace("₴", "").replace(" ", "").replace(",", ".");
        CustomReporter.log("Found price: " + priceOfAddedProduct);
        Assert.assertEquals(priceOfAddedProduct, product.getPrice(), "Price didnt match.");

        //press to registration order button
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='btn btn-primary']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn btn-primary']")));
        WebElement btnGoToRegistration2 = driver.findElement(By.xpath("//a[@class='btn btn-primary']"));
        js.executeScript("arguments[0].click();", btnGoToRegistration2);
    }

    public void fillPageOfBuyProduct(UserDate user){
        //use male gender
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='custom-radio']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='custom-radio']")));
        WebElement maleSwitch =  driver.findElement(By.xpath("//span[@class='custom-radio']"));
        maleSwitch.click();

        //fill first name
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='firstname']")));
        WebElement fieldFirstName =  driver.findElement(By.xpath("//input[@name='firstname']"));
        fieldFirstName.sendKeys(user.getFirstName());

        //fill last name
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='lastname']")));
        WebElement fieldLastName =  driver.findElement(By.xpath("//input[@name='lastname']"));
        fieldLastName.sendKeys(user.getLastName());

        //fill email
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='email']")));
        WebElement fieldEmail =  driver.findElement(By.xpath("//input[@name='email']"));
        fieldEmail.sendKeys(user.getEmail());

        //press continue
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-link-action='register-new-customer']")));
        WebElement btnContinue =  driver.findElement(By.xpath("//button[@data-link-action='register-new-customer']"));
        wait.until(ExpectedConditions.elementToBeClickable(btnContinue));
        js.executeScript("arguments[0].click();", btnContinue);

        //fill address
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='address1']")));
        WebElement fieldAddress =  driver.findElement(By.xpath("//input[@name='address1']"));
        fieldAddress.sendKeys(user.getAddress());

        //fill postcode
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='postcode']")));
        WebElement fieldPoscode =  driver.findElement(By.xpath("//input[@name='postcode']"));
        fieldPoscode.sendKeys(user.getPostcode());

        //fill city
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='city']")));
        WebElement fieldCity =  driver.findElement(By.xpath("//input[@name='city']"));
        fieldCity.sendKeys(user.getPostcode());

        //press to btn confirm address
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@name='confirm-addresses']")));
        WebElement btnConfirmAddress =  driver.findElement(By.xpath("//button[@name='confirm-addresses']"));
        wait.until(ExpectedConditions.elementToBeClickable(btnConfirmAddress));
        js.executeScript("arguments[0].click();", btnConfirmAddress);
    }

    public void confirmDeliveryType(){
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@name='confirmDeliveryOption']")));
        WebElement btnDeliveryType =  driver.findElement(By.xpath("//button[@name='confirmDeliveryOption']"));
        wait.until(ExpectedConditions.elementToBeClickable(btnDeliveryType));
        js.executeScript("arguments[0].click();", btnDeliveryType);
    }

    public void confirmPayType(){
        //select check type of pay
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='payment-option-1']")));
        WebElement chekTypeSwitch =  driver.findElement(By.xpath("//input[@id='payment-option-1']"));
        chekTypeSwitch.click();

        //press Consent btn
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("conditions_to_approve[terms-and-conditions]")));
        WebElement btnConsent = driver.findElement(By.name("conditions_to_approve[terms-and-conditions]"));
        btnConsent.click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn btn-primary center-block']")));
        WebElement btnConfirmAddress =  driver.findElement(By.xpath("//button[@class='btn btn-primary center-block']"));
        wait.until(ExpectedConditions.elementToBeClickable(btnConfirmAddress));
        js.executeScript("arguments[0].click();", btnConfirmAddress);
    }

    public void checkOrder(ProductData product){
        //check confirm order
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='h1 card-title']")));
        WebElement confirmOrder =  driver.findElement(By.xpath("//h3[@class='h1 card-title']"));
        Assert.assertTrue(confirmOrder.getText().toLowerCase().contains("ваш заказ подтверждён"), "Order is not confirm");

        //check number of results
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='order-confirmation-table']/div")));
        List <WebElement> rowResults = driver.findElements(By.xpath("//div[@class='order-confirmation-table']/div"));
        CustomReporter.logAction( "Found " + rowResults.size());
        Assert.assertTrue(rowResults.size() >= 1, "Found more than 1 type of buy product");

        //check name of product
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-sm-4 col-xs-9 details']/span")));
        WebElement foundName =  driver.findElement(By.xpath("//div[@class='col-sm-4 col-xs-9 details']/span"));
        Assert.assertTrue(foundName.getText().toLowerCase().contains(product.getName().toLowerCase()), "Name missmatch");

        //check qty
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-xs-2']")));
        WebElement foundQty =  driver.findElement(By.xpath("//div[@class='col-xs-2']"));
        Assert.assertEquals("1", foundQty.getText(), "Quantity didnt match");

        //check result price
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@class='font-weight-bold']/td[2]")));
        WebElement resultPrice =  driver.findElement(By.xpath("//tr[@class='font-weight-bold']/td[2]"));
        Assert.assertEquals(resultPrice.getText().replace("₴", "").replace(" ", "").replace(",", "."), product.getPrice(), "Quantity didnt match");
    }

    public void searchProduct(String Name){
        CustomReporter.logAction("Start search products by name.");

        //find button allProducts
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']")));
        WebElement btnAllProducts =  driver.findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']"));
        wait.until(ExpectedConditions.elementToBeClickable(btnAllProducts));
        String PressToBtnAllProducts  = "document.getElementsByClassName('all-product-link pull-xs-left pull-md-right h4')[0].click()";
        js.executeScript(PressToBtnAllProducts);

        //search added product on all pages
        exit: for(;true;){
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='products row']")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@itemprop='name']/a")));
            List<WebElement> FoundedProducts = driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")));
            Assert.assertTrue(FoundedProducts.size() > 0, "Page without products");
            CustomReporter.logAction("Found " + FoundedProducts.size() + " products.");

            //Search product by name on one page of product by name
            for(int j = 0; j < FoundedProducts.size(); j++){
                FoundedProducts = driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
                CustomReporter.logAction("Found name is " + FoundedProducts.get(j).getText());
                if(FoundedProducts.get(j).getText().equalsIgnoreCase(Name)){
                    FoundedProducts = driver.findElements(By.xpath("//h1[@itemprop='name']/a"));
                    wait.until(ExpectedConditions.elementToBeClickable(FoundedProducts.get(j)));
                    Actions moveAndClickOnNameOfProduct = new Actions(driver);
                    js.executeScript("arguments[0].scrollIntoView();", FoundedProducts.get(j));
                    moveAndClickOnNameOfProduct.moveToElement(FoundedProducts.get(j)).pause(Duration.ofSeconds(1)).click().build().perform();
                    break exit;
                }
            }

            //if on page was less than 12 product we check all products
            Assert.assertTrue(FoundedProducts.size() == 12, "Product didnt find by name.");

            //go to next page
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//nav[@class='pagination']/div[@class='col-md-4']")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@rel='next']")));
            String PressToBtngoNextPage  = "document.getElementsByClassName('next js-search-link')[0].click()";
            js.executeScript(PressToBtngoNextPage);

            //wait for load new products on page
            try{
                wait.until(ExpectedConditions.and(
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")),
                        ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")), "12"))
                ));
            }catch (StaleElementReferenceException ex){
                wait.until(ExpectedConditions.and(
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")),
                        ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='col-md-6 hidden-sm-down total-products']/p")), "12"))
                ));
            }
        }
    }

    public void checkChangeOfQuantity(ProductData product){
        //check is quantity not equals 0
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='product-availability']")));
        WebElement Quantity = driver.findElement(By.xpath("//span[@id='product-availability']"));
        if((String.valueOf( Integer.valueOf(product.getQty()) - 1).equalsIgnoreCase("0") || (Quantity.getText().replace("\uE14B", "").contains("Нет в наличии")))){
            CustomReporter.logAction("Expected 0 products. Found 0 products");
            return;
        }
        Assert.assertFalse(Quantity.getText().replace("\uE14B", "").contains("Нет в наличии"), "Quantity is 0");

        //press to button Products detail
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[(@class='nav-link') or (@class='nav-link active')]")));
        WebElement BtnDeteilProducts = driver.findElement(By.xpath("//a[(@class='nav-link') or (@class='nav-link active')]"));
        wait.until(ExpectedConditions.elementToBeClickable(BtnDeteilProducts));
        BtnDeteilProducts.click();

        //check quantity of product
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='product-quantities']/span")));
        WebElement productAvalible =  driver.findElement(By.xpath("//div[@class='product-quantities']/span"));
        String QuantityOfProductFromPage = (String) js.executeScript("return arguments[0].textContent;", productAvalible);
        QuantityOfProductFromPage = QuantityOfProductFromPage.replaceAll("[^0-9,]", "");
        String QuantityOfProductSaved = String.valueOf( Integer.valueOf(product.getQty()) - 1);
        CustomReporter.logAction(" QuantityOfProductFromPage is " +  QuantityOfProductFromPage);
        CustomReporter.logAction(" QuantityOfProductSaved is " +  QuantityOfProductSaved);
        Assert.assertEquals(QuantityOfProductFromPage, QuantityOfProductSaved, "Quantity didnt match" );
    }

}
