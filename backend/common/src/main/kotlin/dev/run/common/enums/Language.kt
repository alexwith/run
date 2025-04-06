package dev.run.common.enums

// This is not a very pretty solution, but considering how difficult it is to read
// files from the resources directory in a multi-module compiled jar I have gone with this solution
enum class Language(
    val processLimit: Int,
    val memoryLimit: String,
    val timeoutSeconds: Int,
    val image: String
) {

    PYTHON(
        1,
        "30m",
        5,
        """
            FROM python:3.13.0-alpine

            WORKDIR /run        

            RUN cat <<EOF > code.py
            {code}
            EOF

            CMD ["python", "code.py"]
        """.trimIndent()
    ),
    JAVA(
        1,
        "50m",
        5,
        """
            FROM amazoncorretto:8-alpine3.17-jdk

            WORKDIR /run           

            RUN cat <<EOF > Main.java
            {code}
            EOF

            CMD ["/bin/sh", "-c", "javac Main.java && java Main"]
        """.trimIndent()
    ),
    C(
        1,
        "30m",
        5,
        """
            FROM gcc:4.9

            WORKDIR /run            

            RUN cat <<EOF > code.c
            {code}
            EOF

            CMD ["/bin/sh", "-c", "gcc -o code code.c -std=c99 && ./code"]
        """.trimIndent()
    );

    fun createImage(code: String): String {
        // not using templates cause code messes up indention for trimIndent()
        return this.image.replace("{code}", code)
    }

    companion object {

        fun from(name: String): Language? {
            return try {
                valueOf(name.uppercase())
            } catch (e: Exception) {
                return null
            }
        }
    }
}