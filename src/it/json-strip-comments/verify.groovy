def baseFileName = "target/sling-initial-content-json-strip-comments-maven-plugin-test-bundle-1.0.0-SNAPSHOT"

def expectedJsonContent = """{
  "jcr:primaryType": "cq:Component",
  "jcr:title": "wcm.io Breadcrumb (v3)",
  "componentGroup": ".wcmio-core-wcm",
  "sling:resourceSuperType": "core/wcm/components/breadcrumb/v3/breadcrumb",
  "cq:dialog": {
    "jcr:primaryType": "nt:unstructured",
    "content": {
      "items": {
        "tabs": {
          "items": {
            "properties": {
              "items": {
                "columns": {
                  "items": {
                    "column": {
                      "items": {
                        "startLevel": {
                          "sling:hideResource": true
                        },
                        "disableShadowing": {
                          "sling:hideResource": true
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "cq:design_dialog": {
    "jcr:primaryType": "nt:unstructured",
    "content": {
      "items": {
        "tabs": {
          "items": {
            "properties": {
              "items": {
                "startLevel": {
                  "sling:hideResource": true
                },
                "disableShadowing": {
                  "sling:hideResource": true
                }
              }
            }
          }
        }
      }
    }
  }
}"""

// bundle
File jarFile = new File(basedir, "${baseFileName}.jar")
assert jarFile.exists()

// validate JSON output
try (zipFile = new java.util.zip.ZipFile(jarFile)) {
  zipFile.entries().each {
    if (it.name == "SLING-INF/app-root/json-with-comments.json") {
      def jsonContent = zipFile.getInputStream(it).text
      assert jsonContent == expectedJsonContent
    }
  }
}

return true
