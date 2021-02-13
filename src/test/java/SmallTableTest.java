import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sen4ik.stu.TableUtility;
import org.sen4ik.utils.FileUtil;
import org.sen4ik.utils.selenium.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.*;

@Slf4j
public class SmallTableTest {

    public static String smallTableHtmlFile = "./test_data/small_table.htm";
    public static final By testTable = By.id("mainTable");
    public static final By secondaryTable = By.id("secondaryTable");

    private static final String firstCellText = "Chandalar River Headwaters";
    private List<String> expectedHeaders = Arrays.asList("Site", "Commodities_main", "Quad_250", "Quad_63360",
            "Latitude", "Longitude", "Location", "Commodities_other", "Ore_minerals", "Gangue_minerals", "Site_type",
            "Site_status", "Production", "Generic_model", "Deposit_model", "Geologic_description",
            "Workings_exploration", "Additional_comments", "Expanded_References", "ARDF_no", "Reporter",
            "Last_report_date", "MRDS_no", "Age", "Deposit_model_number", "Alteration", "Production_notes",
            "Reserves", "Primary_reference", "State", "District", "Host_rock", "Host_rock_age", "Assoc_ign_rock",
            "Ign_rock_age", "References", "Reporter_affiliation", "Quadrangle");

    private static int initialRowCount = 99;
    private static int initialColumnCount = 38;

    @BeforeClass
    public void bc(){
        try {
            this.smallTableHtmlFile = FileUtil.getFilePath(smallTableHtmlFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void bm(){
        DriverManager.createInstance();
        TableUtility.init(DriverManager::getDriver, testTable);
        DriverManager.getDriver().get("file:///" + smallTableHtmlFile);
    }

    @AfterMethod
    public void am(){
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void isTablePresent(){
        assertTrue(TableUtility.isTablePresent());
    }

    @Test
    public void isTablePresent_negative(){
        TableUtility.setTableLocator(By.xpath("//table[@id='nonexisting']"));
        assertFalse(TableUtility.isTablePresent());
    }

    @Test
    public void isTableVisible(){
        assertTrue(TableUtility.isTableVisible());
    }

    @Test
    public void isTableVisible_negative(){
        TableUtility.setTableLocator(secondaryTable);
        assertFalse(TableUtility.isTableVisible());
    }

    @Test
    public void isHeaderRowPresent(){
        assertTrue(TableUtility.isHeaderRowPresent());
    }

    @Test
    public void isHeaderRowPresent_negative(){
        DriverManager.getDriver().findElement(By.id("removeHeader")).click();
        assertFalse(TableUtility.isHeaderRowPresent());
    }

    @Test
    public void getHeaders(){
        assertEquals(TableUtility.getHeaders(), expectedHeaders);
    }

    @Test
    public void getHeaders_negative(){
        DriverManager.getDriver().findElement(By.id("removeHeader")).click();
        assertTrue(TableUtility.getHeaders().size() == 0);
    }

    @Test
    public void getHeaderElements(){
        assertTrue(TableUtility.getHeaderElements()
                .get(0)
                .getText()
                .equals(expectedHeaders.get(0)));
    }

    @Test
    public void isHeaderPresent_positive(){
        assertTrue(TableUtility.isHeaderPresent(expectedHeaders.get(0)));
    }

    @Test
    public void isHeaderPresent_negative(){
        assertFalse(TableUtility.isHeaderPresent("Tux"));
    }

    @Test
    public void getHeaderIndex_positive(){
        assertEquals(TableUtility.getHeaderIndex(expectedHeaders.get(0)), 0);
    }

    @Test
    public void getHeaderIndex_negative(){
        assertEquals(TableUtility.getHeaderIndex("Banana"), -1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getHeaderIndex_negativeTwo(){
        TableUtility.getHeaderIndex("");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getHeaderIndex_negativeThree(){
        TableUtility.getHeaderIndex(null);
    }

    @Test
    public void getHeaderByIndex_positive(){
        assertEquals(TableUtility.getHeaderByIndex(0), expectedHeaders.get(0));
        assertEquals(TableUtility.getHeaderByIndex(1), expectedHeaders.get(1));
    }

    @Test
    public void getHeaderByIndex_negative(){
        assertEquals(TableUtility.getHeaderByIndex(99999), "");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getHeaderByIndex_negativeTwo(){
        TableUtility.getHeaderByIndex(-1);
    }

    @Test
    public void isHeaderPresentByIndex_positive(){
        assertTrue(TableUtility.isHeaderPresent(1));
    }

    @Test
    public void isHeaderPresentByIndex_negative(){
        assertFalse(TableUtility.isHeaderPresent(99999));
    }

    @Test
    public void getHeaderElementByName(){
        WebElement headerElement = TableUtility.getHeaderElementByName(expectedHeaders.get(1));
        assertTrue(headerElement.getText().equals(expectedHeaders.get(1)));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getHeaderElementByName_negative(){
        TableUtility.getHeaderElementByName("");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getHeaderElementByName_negativeTwo(){
        TableUtility.getHeaderElementByName(null);
    }

    @Test
    public void getHeaderElementByIndex(){
        WebElement headerEl = TableUtility.getHeaderElementByIndex(1);
        assertTrue(headerEl.getText().equals(expectedHeaders.get(1)));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getHeaderElementByIndex_negative(){
        TableUtility.getHeaderElementByIndex(-1);
    }

    @Test
    public void getCell(){
        assertTrue(TableUtility
                .getCell(0, 0)
                .getText()
                .equals(firstCellText));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getCell_negative(){
        TableUtility.getCell(99999, 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getCell_negativeTwo(){
        TableUtility.getCell(0, 99999);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getCell_negativeThree(){
        TableUtility.getCell(-1, 0);
    }

    @Test
    public void getCellUsingColumnName(){
        assertTrue(TableUtility
                .getCell(0, expectedHeaders.get(0))
                .getText()
                .equals(firstCellText));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void getCellUsingColumnName_negative(){
        TableUtility.getCell(0, "non existing");
    }

    @Test
    public void getCellText(){
        assertTrue(TableUtility
                .getCellText(0, 0)
                .equals(firstCellText));
    }

    @Test
    public void getCellTextUsingColumnName(){
        assertTrue(TableUtility
                .getCellText(0, expectedHeaders.get(0))
                .equals(firstCellText));
    }

    @Test
    public void getRows(){
        assertTrue(TableUtility.getRows().size() == initialRowCount);
    }

    @Test
    public void getRow(){
        assertTrue(TableUtility.getRow(0)
                .getText()
                .contains(firstCellText));
    }

    @Test
    public void getRowText(){
        assertTrue(TableUtility.getRowText(0)
                .contains(firstCellText));
    }

    @Test
    public void getRowCount(){
        assertTrue(TableUtility.getRowCount() == initialRowCount);
    }

    @Test
    public void getColumn(){
        assertTrue(TableUtility.getColumn(0).get(0).getText().equals(firstCellText));
        assertTrue(TableUtility.getColumn(0).get(1).getText().equals("Koness River Headwaters"));
    }

    @Test
    public void getColumn_two(){
        assertTrue(TableUtility.getColumn("Site").get(0).getText().equals(firstCellText));
        assertTrue(TableUtility.getColumn("Site").get(1).getText().equals("Koness River Headwaters"));
    }

    @Test
    public void getColumnText(){
        assertTrue(TableUtility.getColumnText(0).contains(firstCellText));
        assertTrue(TableUtility.getColumnText(0).contains("Koness River Headwaters"));
    }

    @Test
    public void getColumnText_two(){
        assertTrue(TableUtility.getColumnText("Site").contains(firstCellText));
        assertTrue(TableUtility.getColumnText("Site").contains("Koness River Headwaters"));
    }

    @Test
    public void getUniqueColumnText(){
        assertTrue(TableUtility.getUniqueColumnText(10).equals(
                Arrays.asList("", "Prospects", "Mine", "Prospect", "Occurrences", "Occurrence")
        ));
    }

    @Test
    public void getUniqueColumnText_byHeaderName(){
        assertTrue(TableUtility.getUniqueColumnText("Site_type").equals(
                Arrays.asList("", "Prospects", "Mine", "Prospect", "Occurrences", "Occurrence")
        ));
    }

    @Test
    public void isValuePresentInColumn(){
        assertTrue(TableUtility.isValuePresentInColumn("Site_type", "Prospect"));
    }

    @Test
    public void isValuePresentInColumn_two(){
        assertTrue(TableUtility.isValuePresentInColumn(10, "Prospect"));
    }

    @Test
    public void isValuePresentInColumn_negative(){
        assertFalse(TableUtility.isValuePresentInColumn(initialRowCount, "Prospect"));
    }

    @Test
    public void isValuePresentInColumn_negative_two(){
        assertFalse(TableUtility.isValuePresentInColumn(10, "non existing"));
    }

    @Test
    public void isTbodyPresent(){
        assertTrue(TableUtility.isTbodyPresent());
    }

    @Test
    public void waitForNumberOfRowsToEqual(){
        assertTrue(TableUtility.getRowCount() == initialRowCount);
        clickRemoveRowButton();
        assertTrue(TableUtility.waitForNumberOfRowsToEqual(initialRowCount-1, 7));
    }

    @Test
    public void waitForNumberOfRowsToEqual_negative(){
        assertFalse(TableUtility.waitForNumberOfRowsToEqual(999, 2));
    }

    @Test
    public void waitForNumberOfRowsToChange(){
        assertTrue(TableUtility.getRowCount() == initialRowCount);
        clickRemoveRowButton();
        assertTrue(TableUtility.waitForNumberOfRowsToChange(7));
    }

    @Test
    public void waitForNumberOfRowsToChange_negative(){
        assertFalse(TableUtility.waitForNumberOfRowsToChange(2));
    }

    @Test
    public void waitForNumberOfColumnsToEqual(){
        assertTrue(TableUtility.getColumnCount() == initialColumnCount);
        clickRemoveFirstColumnButton();
        assertTrue(TableUtility.waitForNumberOfColumnsToEqual(initialColumnCount-1, 7));
    }

    @Test
    public void waitForNumberOfColumnsToEqual_negative(){
        assertFalse(TableUtility.waitForNumberOfColumnsToEqual(9999, 2));
    }

    @Test
    public void waitForNumberOfColumnsToChange(){
        assertTrue(TableUtility.getColumnCount() == initialColumnCount);
        clickRemoveFirstColumnButton();
        assertTrue(TableUtility.waitForNumberOfColumnsToChange(7));
    }

    @Test
    public void waitForNumberOfColumnsToChange_negative(){
        assertFalse(TableUtility.waitForNumberOfColumnsToChange(2));
    }





    private void clickRemoveRowButton(){
        DriverManager.getDriver().findElement(By.id("removeRow")).click();
    }

    private void clickRemoveFirstColumnButton(){
        DriverManager.getDriver().findElement(By.id("removeFirstColumn")).click();
    }

}
