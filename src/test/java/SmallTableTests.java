import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class SmallTableTests extends BaseTest{

    private SeleniumTableUtility stu;

    private List<String> expectedHeaders = Arrays.asList("Site", "Commodities_main", "Quad_250", "Quad_63360", "Latitude",
            "Longitude", "Location", "Commodities_other", "Ore_minerals", "Gangue_minerals", "Site_type",
            "Site_status", "Production", "Generic_model", "Deposit_model", "Geologic_description",
            "Workings_exploration", "Additional_comments", "Expanded_References", "ARDF_no", "Reporter",
            "Last_report_date", "MRDS_no", "Age", "Deposit_model_number", "Alteration", "Production_notes",
            "Reserves", "Primary_reference", "State", "District", "Host_rock", "Host_rock_age", "Assoc_ign_rock",
            "Ign_rock_age", "References", "Reporter_affiliation", "Quadrangle");

    @BeforeMethod
    public void bm(){
        driver.get("file:///" + smallTableHtmlFileAbsPath);
        stu = new SeleniumTableUtility(driver, testTable);
    }

    @Test
    public void getHeaders(){
        assertEquals(stu.getHeaders(), expectedHeaders);
    }

    @Test
    public void getHeaderElements(){
        assertTrue(stu.getHeaderElements().get(0).getText().equals(expectedHeaders.get(0)));
    }

    @Test
    public void isHeaderPresentPositive(){
        assertTrue(stu.isHeaderPresent(expectedHeaders.get(0)));
    }

    @Test
    public void isHeaderPresentNegative(){
        assertFalse(stu.isHeaderPresent("Tux"));
    }

    @Test
    public void getHeaderIndexPositive(){
        assertEquals(stu.getHeaderIndex(expectedHeaders.get(0)), 0);
    }

    @Test
    public void getHeaderIndexNegative(){
        assertEquals(stu.getHeaderIndex("Banana"), -1);
    }

    @Test
    public void getHeaderByIndexPositive(){
        assertEquals(stu.getHeaderByIndex(1), expectedHeaders.get(1));
    }

    @Test
    public void getHeaderByIndexNegative(){
        assertEquals(stu.getHeaderByIndex(99999), "");
    }

    @Test
    public void isHeaderPresentByIndexPositive(){
        assertTrue(stu.isHeaderPresent(1));
    }

    @Test
    public void isHeaderPresentByIndexNegative(){
        assertFalse(stu.isHeaderPresent(99999));
    }

}
