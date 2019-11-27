/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package com.redhat.quarkus.jdt.core;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.quarkus.commons.QuarkusJavaHoverInfo;
import com.redhat.quarkus.commons.QuarkusJavaHoverParams;
import com.redhat.quarkus.jdt.internal.core.ls.JDTUtilsLSImpl;

/**
 * JDT Quarkus manager test for hover in Java file.
 * 
 *
 */
public class JDTQuarkusManagerForJavaHoverTest extends BaseJDTQuarkusManagerTest {
	
	private static IJDTUtils utils;
	private static String javaFileUri;
	
	
	@BeforeClass
	public static void setup() throws Exception {	
		IJavaProject javaProject = loadMavenProject(MavenProjectName.config_hover);
		IFile javaFile = javaProject.getProject()
				.getFile(new Path("src/main/java/org/acme/config/GreetingResource.java"));
		javaFileUri = javaFile.getLocation().toFile().toURI().toString();
		
		utils = JDTUtilsLSImpl.getInstance();
	}
	
	
	@Test
	public void configPropertyNameHover() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(14, 40);
		// Position(14, 40) is the character after the | symbol:
		//    @ConfigProperty(name = "greeting.mes|sage")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNotNull(info);
		
		Position expectedStart = new Position(14, 28);
		Position expectedEnd = new Position(14, 44);
		Range expectedRange = new Range(expectedStart, expectedEnd);
		
		Assert.assertEquals(info.getPropertyKey(), "greeting.message");
		Assert.assertEquals(info.getPropertyValue(), "hello");
		Assert.assertEquals(info.getRange(), expectedRange);
	}
	
	@Test
	public void configPropertyNameLeftEdgeHover() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(14, 28);
		// Position(14, 28) is the character after the | symbol:
		//    @ConfigProperty(name = "|greeting.message")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNotNull(info);
		
		Position expectedStart = new Position(14, 28);
		Position expectedEnd = new Position(14, 44);
		Range expectedRange = new Range(expectedStart, expectedEnd);
		
		Assert.assertEquals(info.getPropertyKey(), "greeting.message");
		Assert.assertEquals(info.getPropertyValue(), "hello");
		Assert.assertEquals(info.getRange(), expectedRange);
	}
	
	@Test
	public void configPropertyNameRightEdgeHover() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(14, 43);
		// Position(14, 43) is the character after the | symbol:
		//    @ConfigProperty(name = "greeting.messag|e")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNotNull(info);
		
		Position expectedStart = new Position(14, 28);
		Position expectedEnd = new Position(14, 44);
		Range expectedRange = new Range(expectedStart, expectedEnd);
		
		Assert.assertEquals(info.getPropertyKey(), "greeting.message");
		Assert.assertEquals(info.getPropertyValue(), "hello");
		Assert.assertEquals(info.getRange(), expectedRange);
	}
	
	@Test
	public void configPropertyNameNoHover1() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(14, 27);
		// Position(14, 27) is the character after the | symbol:
		//    @ConfigProperty(name = |"greeting.message")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNull(info);
	}
	
	@Test
	public void configPropertyNameNoHover2() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(14, 44);
		// Position(14, 44) is the character after the | symbol:
		//    @ConfigProperty(name = "greeting.message|")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNull(info);
	}
	
	@Test
	public void configPropertyNameHoverDefaultValue() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(17, 33);
		// Position(17, 33) is the character after the | symbol:
		//    @ConfigProperty(name = "greet|ing.suffix", defaultValue="!")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNotNull(info);
		
		Position expectedStart = new Position(17, 28);
		Position expectedEnd = new Position(17, 43);
		Range expectedRange = new Range(expectedStart, expectedEnd);
		
		Assert.assertEquals(info.getPropertyKey(), "greeting.suffix");
		Assert.assertEquals(info.getPropertyValue(), "!");
		Assert.assertEquals(info.getRange(), expectedRange);
	}
	
	@Test
	public void configPropertyNameHoverOverrideDefaultValue() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(26, 33);
		// Position(26, 33) is the character after the | symbol:
		//    @ConfigProperty(name = "greeting.number", defaultValue="0")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNotNull(info);
		
		Position expectedStart = new Position(26, 28);
		Position expectedEnd = new Position(26, 43);
		Range expectedRange = new Range(expectedStart, expectedEnd);
		
		Assert.assertEquals(info.getPropertyKey(), "greeting.number");
		Assert.assertEquals(info.getPropertyValue(), "100");
		Assert.assertEquals(info.getRange(), expectedRange);
	}
	
	@Test
	public void configPropertyNameHoverNoValue() throws Exception {

		QuarkusJavaHoverParams params = new QuarkusJavaHoverParams();
		Position hoverPosition = new Position(23, 33);
		// Position(23, 33) is the character after the | symbol:
	    //    @ConfigProperty(name = "greet|ing.missing")
		
		params.setPosition(hoverPosition);
		params.setUri(javaFileUri);
		
		QuarkusJavaHoverInfo info = JDTQuarkusManagerForJava.getInstance().hover(params, utils,
				new NullProgressMonitor());
		
		Assert.assertNotNull(info);
		
		Position expectedStart = new Position(23, 28);
		Position expectedEnd = new Position(23, 44);
		Range expectedRange = new Range(expectedStart, expectedEnd);
		
		Assert.assertEquals(info.getPropertyKey(), "greeting.missing");
		Assert.assertNull(info.getPropertyValue());
		Assert.assertEquals(info.getRange(), expectedRange);
	}	
}