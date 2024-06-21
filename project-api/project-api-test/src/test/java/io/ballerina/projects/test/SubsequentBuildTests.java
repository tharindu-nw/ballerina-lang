/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package io.ballerina.projects.test;

import io.ballerina.projects.DiagnosticResult;
import io.ballerina.projects.PackageCompilation;
import io.ballerina.projects.directory.BuildProject;
import org.apache.commons.io.FileUtils;
import org.ballerinalang.test.BCompileUtil;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.ballerinalang.util.RepoUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.ballerina.projects.test.TestUtils.assertTomlFilesEquals;
import static io.ballerina.projects.test.TestUtils.replaceDistributionVersionOfDependenciesToml;
import static io.ballerina.projects.util.ProjectConstants.BALLERINA_TOML;
import static io.ballerina.projects.util.ProjectConstants.BUILD_FILE;
import static io.ballerina.projects.util.ProjectConstants.DEPENDENCIES_TOML;
import static io.ballerina.projects.util.ProjectConstants.RESOURCE_DIR_NAME;
import static io.ballerina.projects.util.ProjectConstants.TARGET_DIR_NAME;

/**
 * Contains cases to test Dependencies.toml for subsequent builds.
 *
 * @since 2.0.0
 */
public class SubsequentBuildTests {

    private static final Path RESOURCE_DIRECTORY =
            Paths.get("src/test/resources/projects_for_resolution_tests").toAbsolutePath();
    private static Path tempResourceDir;
    private static final PrintStream OUT = System.out;
    private Path packagePath;

    @BeforeClass
    public void setUp() throws IOException {
        // copy the resource directory to a temp directory
        tempResourceDir = Files.createTempDirectory("project-api-test");
        FileUtils.copyDirectory(RESOURCE_DIRECTORY.toFile(), tempResourceDir.toFile());

        packagePath = tempResourceDir.resolve("package_f");
        // Delete build file if exists
        Files.deleteIfExists(packagePath.resolve(TARGET_DIR_NAME).resolve(BUILD_FILE));
        // Delete Dependencies.toml file if exists
        Files.deleteIfExists(packagePath.resolve(DEPENDENCIES_TOML));

        // package_f --> package_d
        // package_d --> package_b --> package_c
        // package_d --> package_e
        BCompileUtil.compileAndCacheBala(tempResourceDir.resolve("package_c").toString());
        Path packageBPath = tempResourceDir.resolve("package_b");
        replaceDistributionVersionOfDependenciesToml(packageBPath, RepoUtils.getBallerinaShortVersion());
        BCompileUtil.compileAndCacheBala(packageBPath.toString());
        BCompileUtil.compileAndCacheBala(tempResourceDir.resolve("package_e").toString());
        BCompileUtil.compileAndCacheBala(tempResourceDir.resolve("package_d").toString());
    }

    @Test
    public void testBuildPackage() throws IOException {
        // Build the project
        BuildProject buildProject = TestUtils.loadBuildProject(packagePath);
        buildProject.save();
        PackageCompilation compilation = buildProject.currentPackage().getCompilation();

        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        diagnosticResult.diagnostics().forEach(OUT::println);
        Assert.assertEquals(diagnosticResult.diagnosticCount(), 0, "Unexpected compilation diagnostics");

        // Check Dependencies.toml content
        assertTomlFilesEquals(packagePath.resolve(DEPENDENCIES_TOML),
                packagePath.resolve(RESOURCE_DIR_NAME).resolve("UpdatedDependencies.toml"));
    }

    @Test(dependsOnMethods = "testBuildPackage")
    public void testBuildPackageAgainAfterPushingLocalDependency() throws IOException {
        // package_f --> package_d
        // package_d --> package_b --> package_c
        // package_d --> package_e

        // update package_c version and push to local repo
        String pkgDBallerinaTomlContent = """
                [package]
                org = "samjs"
                name = "package_c"
                version = "0.2.0"
                export = ["package_c", "package_c.mod_c1", "package_c.mod_c2"]
                """;
        Files.write(tempResourceDir.resolve("package_c").resolve(BALLERINA_TOML),
                    pkgDBallerinaTomlContent.getBytes(StandardCharsets.UTF_8));
        BCompileUtil.compileAndCacheBala("projects_for_resolution_tests/package_c");

        // Build the project
        BuildProject buildProject = BuildProject.load(packagePath);
        buildProject.save();
        PackageCompilation compilation = buildProject.currentPackage().getCompilation();

        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        diagnosticResult.diagnostics().forEach(OUT::println);
        Assert.assertEquals(diagnosticResult.diagnosticCount(), 0, "Unexpected compilation diagnostics");

        // Check updated Dependencies.toml content
        assertTomlFilesEquals(packagePath.resolve(DEPENDENCIES_TOML),
                packagePath.resolve(RESOURCE_DIR_NAME).resolve("UpdatedDependencies.toml"));
    }

    @AfterClass
    private void cleanUp() throws IOException {
        // Delete Dependencies.toml and build file
        Files.deleteIfExists(packagePath.resolve(DEPENDENCIES_TOML));
        Files.deleteIfExists(packagePath.resolve(TARGET_DIR_NAME).resolve(BUILD_FILE));
        // revert package_c version
        String pkgDBallerinaTomlContent = """
                [package]
                org = "samjs"
                name = "package_c"
                version = "0.1.0"
                export = ["package_c", "package_c.mod_c1", "package_c.mod_c2"]
                """;
        Files.write(tempResourceDir.resolve("package_c").resolve(BALLERINA_TOML),
                    pkgDBallerinaTomlContent.getBytes(StandardCharsets.UTF_8));
        // Delete package_c build file
        Files.deleteIfExists(tempResourceDir.resolve("package_c").resolve(TARGET_DIR_NAME).resolve(BUILD_FILE));
    }
}
