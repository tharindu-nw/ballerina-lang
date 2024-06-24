/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.test.functions;

import io.ballerina.runtime.api.types.ObjectType;
import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.BRunUtil;
import org.ballerinalang.test.CompileResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.ballerinalang.test.BAssertUtil.validateWarning;

/**
 * Test nil return from functions.
 */
public class FunctionNilReturnTest {

    private CompileResult compileResult;
    private PrintStream original;
    private static final String EXPECTED_OUTPUT = "Hello\n\nBallerina\n";
    private static final String WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE =
            "this function should explicitly return a value";

    @BeforeClass(alwaysRun = true)
    public void setup() {
        original = System.out;
        compileResult = BCompileUtil.compile("test-src/functions/function-nil-return.bal");
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        System.setOut(original);
    }

    @Test
    public void testPrint1() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testPrint1", new ObjectType[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), EXPECTED_OUTPUT);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testPrint2() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testPrint2", new ObjectType[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), EXPECTED_OUTPUT);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testPrint3() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testPrint3", new ObjectType[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), EXPECTED_OUTPUT);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testPrint4() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testPrint4", new ObjectType[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), EXPECTED_OUTPUT);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testPrintInMatchBlock() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testPrintInMatchBlock", new ObjectType[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), EXPECTED_OUTPUT);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testPrintInWorkers() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testPrintInWorkers", new ObjectType[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), EXPECTED_OUTPUT);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testNoReturnFuncInvocnInNilReturnFuncRetStmt() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testNoReturnFuncInvocnInNilReturnFuncRetStmt", new Object[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), "nil returns here\nno returns here\n\n");
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testNilReturnFuncInvocnInNilReturnFuncRetStmt() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(outputStream));
            BRunUtil.invoke(compileResult, "testNilReturnFuncInvocnInNilReturnFuncRetStmt", new Object[0]);
            Assert.assertEquals(outputStream.toString().replace("\r", ""), """
                    nil returns here
                    explicit nil returns here

                    """);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testReturnsDuringValidCheck() {
        Object returns = BRunUtil.invoke(compileResult, "testReturnsDuringValidCheck");
        Assert.assertNull(returns);
    }

    @Test
    public void testMissingReturnInIfElse() {
        Assert.assertNull(BRunUtil.invoke(compileResult, "testMissingReturnInIfElse"));
    }

    @Test
    public void testMissingReturnInNestedIfElse() {
        Assert.assertNull(BRunUtil.invoke(compileResult, "testMissingReturnInNestedIfElse"));
    }

    @Test
    public void testReturningInMatch() {
        Assert.assertNull(BRunUtil.invoke(compileResult, "testReturningInMatch"));
    }

    @Test
    public void testValidCheckWithExplicitReturn() {
        Assert.assertNull(BRunUtil.invoke(compileResult, "testValidCheckWithExplicitReturn"));
    }

    @Test
    public void testEmptyFunctionsWithNilableReturns() {
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableInt"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableFloat"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableDecimal"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableString"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableBoolean"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableByte"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableJSON"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableXML"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableMap"));

        // Arrays
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableIntArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableFloatArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableDecimalArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableStringArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableBooleanArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableByteArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableJSONArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableXMLArray"));
    }

    @Test
    public void testEmptyFunctionsWithComplexNilableReturns() {
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableOpenRecord"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableClosedRecord"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableObject"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableUnion"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableTuple"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableTypedesc"));

        // Arrays
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableOpenRecordArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableClosedRecordArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableObjectArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableUnionArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableTupleArray"));
        Assert.assertNull(BRunUtil.invoke(compileResult, "testNilableTypedescArray"));
    }

    @Test
    public void testWarningsForNotExplicitlyReturningAValue() {
        int i = 0;
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 97, 46);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 105, 52);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 120, 41);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 138, 35);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 141, 37);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 144, 39);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 147, 38);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 150, 39);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 153, 36);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 156, 36);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 162, 35);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 165, 35);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 171, 40);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 174, 42);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 177, 44);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 180, 43);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 183, 44);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 186, 41);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 189, 41);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 192, 40);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 203, 42);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 206, 47);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 214, 44);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 217, 49);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 224, 38);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 227, 43);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 230, 37);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 233, 42);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 236, 37);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 239, 42);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 242, 40);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 245, 45);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 253, 28);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 257, 59);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 265, 35);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 274, 35);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 291, 84);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 294, 84);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 300, 84);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 303, 84);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 306, 84);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 312, 84);
        validateWarning(compileResult, i++, WARN_SHOULD_EXPLICITLY_RETURN_A_VALUE, 315, 84);
        Assert.assertEquals(compileResult.getWarnCount(), i);

    }

    @AfterClass
    public void tearDown() {
        compileResult = null;
    }
}
