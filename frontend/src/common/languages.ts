import { Extension } from "@uiw/react-codemirror";
import { pythonLanguage as pythonSyntax } from "@codemirror/lang-python";
import { javaLanguage as javaSyntax } from "@codemirror/lang-java";

export type Language = {
  name: string;
  image: string;
  syntax: Extension;
  example: string;
};

export const languages = new Map<string, Language>();
languages.set("python", {
  name: "Python",
  image: "python",
  syntax: pythonSyntax,
  example: `print("Hello, World!")`,
});
languages.set("java", {
  name: "Java",
  image: "java",
  syntax: javaSyntax,
  example: `public class Main {

  public static void main(String[] args) {
    System.out.println("Hello, World!");
  }
}`,
});
languages.set("c", {
  name: "C",
  image: "c",
  syntax: javaSyntax,
  example: `#include <stdio.h>

int main() {
  printf("Hello, World!");
  return 0;
}`,
});
