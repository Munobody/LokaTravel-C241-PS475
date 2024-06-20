package com.example.lokatravel.ui.home

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class Model(context: Context) {

    private val interpreter: Interpreter

    init {
        val modelByteBuffer = loadModelFile(context)
        interpreter = Interpreter(modelByteBuffer)
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): ByteBuffer {
        val assetFileDescriptor = context.assets.openFd("collaborative_model (1).tflite")
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
    @Throws(IOException::class)
    fun predict(input: LongArray): LongArray {
        val inputBuffer = ByteBuffer.allocateDirect(input.size * Long.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())  // Set byte order for consistency
        val longBuffer = inputBuffer.asLongBuffer()
        longBuffer.put(input)

        val output = LongArray(getOutputSize(interpreter))
        interpreter.run(inputBuffer, output)
        return output
    }

    private fun getOutputSize(interpreter: Interpreter): Int {
        val outputTensor = interpreter.getOutputTensor(0)
        return outputTensor.shape()[0]
    }
}