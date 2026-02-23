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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Strips comments from a single JSON file.
 */
class JsonCommentStripper {

  private static final ObjectMapper STRICT_MAPPER = new ObjectMapper();
  private static final ObjectMapper LENIENT_MAPPER = new ObjectMapper()
      .enable(JsonParser.Feature.ALLOW_COMMENTS);

  private final File file;

  JsonCommentStripper(File file) {
    this.file = file;
  }

  /**
   * Returns {@code true} if the file cannot be parsed as strict JSON (i.e. it contains comments or other
   * non-standard constructs that require the lenient parser).
   */
  boolean hasComments() {
    try {
      STRICT_MAPPER.readTree(file);
      return false;
    }
    catch (IOException ex) {
      return true;
    }
  }

  /**
   * Reads the file with the lenient Jackson parser (comments allowed), serialises the tree
   * back to standard JSON and writes it to the same file.
   */
  void stripComments() throws IOException {
    JsonNode tree = LENIENT_MAPPER.readTree(file);
    STRICT_MAPPER.writeValue(file, tree);
  }

}
