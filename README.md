# Description
[Java Image I/O](http://docs.oracle.com/javase/7/docs/api/javax/imageio/package-summary.html) reader and writer for the
[Google WebP](https://developers.google.com/speed/webp/) image format.

# License
webp-imageio is distributed under the [Apache Software License](https://www.apache.org/licenses/LICENSE-2.0) version 2.0.

# Usage
- Add webp-imageio.jar to the classpath of your application
- Ensure libwebp-jni.so or webp-jni.dll is accessible on the Java native library path (java.library.path system property)
- The WebP reader and writer can be used like any other Image I/O reader and writer.

# Compiling
The build should work with either Maven or CMake but the Maven build incorporates unit tests to confirm that the generated library works.

## Maven
The Maven build uses the [cmake-maven-plugin](https://code.google.com/p/cmake-maven-project/) to build the native code.  The Java code, of course, just uses Maven.
- Run 'mvn clean install'
- The build will automatically download libwebp and compile it into the JNI library

## CMake
- Install CMake 2.8 or newer. CMake can be downloaded from www.cmake.org or installed using
  your systems package manager.
- Download [libwebp 0.4.2](http://downloads.webmproject.org/releases/webp/libwebp-0.4.2.tar.gz) and extract it into the project's directory
- Run 'cmake .' in the root of directory of the project to generate the build scripts for your system.
- Build the project using the generated build scripts.  If the Java build fails and you previously built with Maven, run 'mvn clean' and try again.
- The build scripts will generate a number of binaries
    - java/webp-imageio.jar: JAR file containing the Image I/O reader and writer
    - c/libwebp-imageio.so: the JNI library that is required by webp-imageio.jar
