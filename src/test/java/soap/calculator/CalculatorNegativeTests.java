package soap.calculator;

import calculator.wsdl2java.Calculator;
import calculator.wsdl2java.CalculatorSoap;
import common.BaseTest;
import common.ReplaceCamelCase;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


@Execution(ExecutionMode.CONCURRENT)
@DisplayNameGeneration(ReplaceCamelCase.class)
public class CalculatorNegativeTests extends BaseTest {

    private CalculatorSoap calculator = new Calculator().getCalculatorSoap();

    @ParameterizedTest(name = "add negative test #{0}")
    @CsvSource({
            "1, 2147483647, 1, 'Arithmetic operation resulted in an overflow'",
            "2, -2147483648, -1, 'Arithmetic operation resulted in an overflow'",
    })
    @Step("add negative test started")
    @Description(value = "test checks the function to add through SOAP API with incorrect result data which out of range int")
    public void addNegativeTest(int id, int A, int B, String expectedMessage) {
        try {
            calculator.add(A,B);
        }catch (Exception e) {
            String actualError = e.getMessage();
            assertError(expectedMessage, actualError);
        }
    }

    @ParameterizedTest(name = "subtract negative test #{0}")
    @CsvSource({
            "1, 2147483647, -1, 'Arithmetic operation resulted in an overflow'",
            "2, -2147483648, 1, 'Arithmetic operation resulted in an overflow'"
    })
    @Step("subtract negative test started")
    @Description(value = "test checks the function to subtract through SOAP API with incorrect result data which out of range int")
    public void subtractNegativeTest(int id, int A, int B, String expectedMessage) {
        try {
            calculator.subtract(A, B);
        } catch (Exception e) {
            String actualError = e.getMessage();
            assertError(expectedMessage, actualError);
        }
    }

    @ParameterizedTest(name = "multiply negative test #{0}")
    @CsvSource({
            "1, 2147483647, 2, 'Arithmetic operation resulted in an overflow'",
            "2, -2147483648, 2, 'Arithmetic operation resulted in an overflow'"
    })
    @Step("multiply negative test started")
    @Description(value = "test checks the function to multiply through SOAP API with incorrect result data which out of range int")
    public void multiplyNegativeTest(int id, int A, int B, String expectedMessage) {
        try {
            calculator.multiply(A,B);
        } catch (Exception e) {
            String actualError = e.getMessage();
            assertError(expectedMessage, actualError);
        }
    }

   @ParameterizedTest(name = "divide negative test #{0}")
   @CsvSource({
          "1, 2147483647, 0, 'Arithmetic operation resulted in an overflow'"
    })
   @Step("divide negative test started")
   @Description(value = "test checks the function to divide through SOAP API with incorrect result data which out of range int")
   public void divideNegativeTest(int id, int A, int B, String expectedMessage) {
       try {
           calculator.divide(A,B);
       } catch (Exception e) {
           String actualError = e.getMessage();
           assertError(expectedMessage, actualError);
       }
    }

   @Step("checked result of error message")
   private void assertError(String expectedMessage, String actualError) {
        Assertions.assertTrue(actualError.contains(expectedMessage), "didn't get expected result");
   }
}
