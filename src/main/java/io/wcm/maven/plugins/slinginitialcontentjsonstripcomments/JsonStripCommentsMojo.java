/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2026 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.maven.plugins.slinginitialcontentjsonstripcomments;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Removes all comments from JSON file resources.
 */
@Mojo(name = "json-strip-comments", requiresProject = true, threadSafe = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class JsonStripCommentsMojo extends AbstractMojo {

  private static final Logger log = LoggerFactory.getLogger(JsonStripCommentsMojo.class);

  /**
   * Directory containing the JSON files to process. By default, it is set to the "classes/SLING-INF" directory of the
   * build output, which is a common location for initial content in Sling projects. You can customize this path if your
   * JSON files are located elsewhere.
   */
  @Parameter(defaultValue = "${project.build.directory}/classes/SLING-INF", required = true)
  private File resources;

  /**
   * Allows to skip the plugin execution.
   */
  @Parameter(property = "slinginitialcontenttransform.skip", defaultValue = "false")
  private boolean skip;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    if (skip) {
      log.debug("Skipping execution.");
      return;
    }

    if (!resources.isDirectory()) {
      log.debug("Resources directory does not exist or is not a directory: {}", resources);
      return;
    }

    try (var stream = Files.walk(resources.toPath())) {
      stream
          .filter(Files::isRegularFile)
          .filter(path -> FilenameUtils.isExtension(path.getFileName().toString(), "json"))
          .forEach(this::processFile);
    }
    catch (UncheckedIOException | IOException ex) {
      throw new MojoExecutionException("Failed process JSON files in : " + resources, ex);
    }
  }

  private void processFile(Path path) {
    File file = path.toFile();
    JsonCommentStripper stripper = new JsonCommentStripper(file);
    if (stripper.hasComments()) {
      log.info("Stripping comments from: {}", resources.toPath().relativize(path));
      try {
        stripper.stripComments();
      }
      catch (IOException ex) {
        throw new UncheckedIOException("Failed to strip comments from: " + file.getPath(), ex);
      }
    }
  }

}