package core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ConvertSToS2DTest {

	@BeforeClass
	public static void BeforeClass01() throws Exception {
	}

	// Test data generator
	@Parameters(name = "Iteration # {index} : exp: {1}; act: {2}")
	public static Collection<String[]> data() throws IOException {
		core.ConvertS propertiesLoad = new core.ConvertS();
		return Arrays.asList(propertiesLoad.ConvertSToS2D());
	}

	// @Parameter for Field injection instead of Constructor
	@Parameter(value = 0)
	public String testID;

	@Parameter(value = 1)
	public String expectedResult;

	@Parameter(value = 2)
	public String actualResult;

	@Test
	public void testCheckTableSValidation() throws ComparisonFailure {
		assertEquals("FAILED", expectedResult, actualResult);
	}
}