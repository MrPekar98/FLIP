package javacc.visitors.codegeneration.templates.js;

import javacc.visitors.codegeneration.CodeFile;

public interface JsModuleFile extends CodeFile {
  String getExportedNames();
}
