package org.apache.maven.plugins.resources;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;

import org.apache.maven.model.Resource;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugins.resources.stub.MavenProjectResourcesStub;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.maven.api.plugin.testing.MojoExtension.setVariableValueToObject;
import static org.codehaus.plexus.testing.PlexusExtension.getBasedir;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * @author Olivier Lamy
 * @version $Id$
 */
@MojoTest
public class CopyResourcesMojoTest
{

    public static final String PLUGIN_CONFIG_XML = "classpath:/unit/resources-test/plugin-config.xml";

    File outputDirectory = new File( getBasedir(), "/target/copyResourcesTests" );

    @BeforeEach
    protected void setUp()
        throws Exception
    {
        if ( !outputDirectory.exists() )
        {
            Files.createDirectories( outputDirectory.toPath() );
        }
        else
        {
            FileUtils.cleanDirectory( outputDirectory );
        }
    }

    @Test
    @InjectMojo( goal = "resources", pom = PLUGIN_CONFIG_XML )
    public void testCopyWithoutFiltering( ResourcesMojo mojo )
        throws Exception
    {
        mojo.setOutputDirectory( outputDirectory );

        Resource resource = new Resource();
        resource.setDirectory( getBasedir() + "/src/test/unit-files/copy-resources-test/no-filter" );
        resource.setFiltering( false );

        mojo.setResources( Collections.singletonList( resource ) );

        MavenProjectResourcesStub project = new MavenProjectResourcesStub( "CopyResourcesMojoTest" );
        File targetFile =  new File( getBasedir(), "/target/copyResourcesTests" );
        project.setBaseDir( targetFile );
        setVariableValueToObject( mojo, "project", project );
        mojo.execute();

        assertTrue( new File( targetFile, "config.properties" ).exists() );
    }

}
