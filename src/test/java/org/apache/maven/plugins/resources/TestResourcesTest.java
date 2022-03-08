package org.apache.maven.plugins.resources;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugins.resources.stub.MavenProjectResourcesStub;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.apache.maven.api.plugin.testing.MojoExtension.setVariableValueToObject;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MojoTest
public class TestResourcesTest
{

    public static final String PLUGIN_CONFIG_XML = "classpath:/unit/resources-test/plugin-config.xml";

    /**
     * test mojo lookup, test harness should be working fine
     */
    @Test
    @InjectMojo( goal = "testResources", pom = PLUGIN_CONFIG_XML )
    public void testHarnessEnvironment( TestResourcesMojo mojo )
    {
        assertNotNull( mojo );
    }

    @Test
    @InjectMojo( goal = "testResources", pom = PLUGIN_CONFIG_XML )
    public void testTestResourceDirectoryCreation( TestResourcesMojo mojo )
        throws Exception
    {
        MavenProjectResourcesStub project = new MavenProjectResourcesStub( "testResourceDirectoryStructure" );
        List<Resource> resources = project.getBuild().getResources();

        assertNotNull( mojo );

        project.addFile( "file4.txt" );
        project.addFile( "package/file3.nottest" );
        project.addFile( "notpackage/file1.include" );
        project.addFile( "package/test/file1.txt" );
        project.addFile( "notpackage/test/file2.txt" );
        project.setupBuildEnvironment();

        setVariableValueToObject( mojo, "project", project );
        setVariableValueToObject( mojo, "resources", resources );
        setVariableValueToObject( mojo, "outputDirectory", new File( project.getBuild().getTestOutputDirectory() ) );
        setVariableValueToObject( mojo, "buildFilters", Collections.emptyList() );
        setVariableValueToObject( mojo, "useBuildFilters", Boolean.TRUE );
        mojo.execute();

        String resorucesDir = project.getTestOutputDirectory();

        assertTrue( FileUtils.fileExists( resorucesDir + "/file4.txt" ) );
        assertTrue( FileUtils.fileExists( resorucesDir + "/package/file3.nottest" ) );
        assertTrue( FileUtils.fileExists( resorucesDir + "/notpackage/file1.include" ) );
        assertTrue( FileUtils.fileExists( resorucesDir + "/package/test" ) );
        assertTrue( FileUtils.fileExists( resorucesDir + "/notpackage/test" ) );
    }
}
