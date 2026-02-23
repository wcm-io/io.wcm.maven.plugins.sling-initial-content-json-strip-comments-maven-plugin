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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class JsonCommentStripperTest {

  private static final File JSON_WITH_COMMENTS = new File("src/test/resources/json-with-comments.json");
  private static final File JSON_WITHOUT_COMMENTS = new File("src/test/resources/json-without-comments.json");

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @TempDir
  File tempDir;

  @Test
  void testHasComments() {
    assertTrue(new JsonCommentStripper(JSON_WITH_COMMENTS).hasComments());
    assertFalse(new JsonCommentStripper(JSON_WITHOUT_COMMENTS).hasComments());
  }

  @Test
  void testStripComments() throws IOException {
    File tempFile = new File(tempDir, "json-with-comments.json");
    Files.copy(JSON_WITH_COMMENTS.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    JsonCommentStripper stripper = new JsonCommentStripper(tempFile);
    assertTrue(stripper.hasComments(), "file should have comments before stripping");

    stripper.stripComments();

    assertFalse(stripper.hasComments(), "file should have no comments after stripping");

    JsonNode actual = MAPPER.readTree(tempFile);
    JsonNode expected = MAPPER.readTree(JSON_WITHOUT_COMMENTS);
    assertEquals(expected, actual, "stripped JSON content should match the reference file without comments");
  }

}