-keepattributes Signature,*Annotation*,InnerClasses,EnclosingMethod

# Kotlinx Serialization uses generated serializers, but these attributes keep
# serialized metadata stable for JSON backup import/export after minification.
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}
