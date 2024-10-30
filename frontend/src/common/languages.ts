export type Language = {
  name: string;
  image: string;
  example: string;
};

export const languages = new Map<string, Language>();
languages.set("python", { name: "Python", image: "python", example: `print("Hello, World!")` });
languages.set("java", {
  name: "Java",
  image: "java",
  example: `public class Main {

  public static void main(String[] args) {
    System.out.println("Hello, World!");
  }
}`,
});
languages.set("kotlin", {
  name: "Kotlin",
  image: "kotlin",
  example: `fun main() {
  println("Hello, World!")
}`,
});
