package com.catscoffeeandkitchen.features.weather.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object IntToBoolSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IntBoolean", PrimitiveKind.INT)
    override fun serialize(encoder: Encoder, value: Boolean) = encoder.encodeInt(if (value) 1 else 0)
    override fun deserialize(decoder: Decoder): Boolean = decoder.decodeInt() > 0
}
