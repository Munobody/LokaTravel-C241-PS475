package com.example.lokatravel.ui.home

import android.content.Context
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class CollaborativeModel(context: Context) {

    private val interpreter: Interpreter

    init {
        // Load the TensorFlow Lite model from assets folder
        val options = Interpreter.Options()
        interpreter = Interpreter(loadModelFile(context), options)
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

    // Placeholder for input data, replace with actual input mechanism
    private val input: FloatArray = floatArrayOf(0.0f, 0.0f) // Example, replace with actual input data

    fun predict(): Int {
        try {
            // Example input data (replace with your actual data)
            val input = floatArrayOf(0.5f, 0.3f)

            // Prepare input buffer
            val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, input.size), DataType.FLOAT32)
            inputBuffer.loadArray(input)

            // Prepare output buffer
            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)

            // Run inference
            interpreter.run(inputBuffer.buffer, outputBuffer.buffer)

            // Get the prediction result
            val prediction = outputBuffer.floatArray[0].toInt()

            return prediction
        } catch (e: Exception) {
            Log.e("CollaborativeModel", "Prediction error: ${e.message}")
            throw e  // Rethrow the exception or handle as needed
        }
    }


    fun close() {
        interpreter.close()
    }
}
