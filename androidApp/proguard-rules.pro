# Keep line numbers for readable stack traces; Crashlytics deobfuscates them with the
# mapping file its Gradle plugin uploads automatically on a minified release build.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
