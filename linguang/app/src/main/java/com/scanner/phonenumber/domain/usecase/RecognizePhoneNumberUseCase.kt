package com.scanner.phonenumber.domain.usecase

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.scanner.phonenumber.domain.util.PhoneNumberParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecognizePhoneNumberUseCase @Inject constructor(
    private val textRecognizer: TextRecognizer,
    private val phoneNumberParser: PhoneNumberParser
) {
    
    data class RecognitionResult(
        val recognizedText: String,
        val phoneNumbers: List<String>,
        val formattedPhoneNumbers: List<String>
    )
    
    suspend operator fun invoke(bitmap: Bitmap): Result<RecognitionResult> {
        return withContext(Dispatchers.IO) {
            try {
                val image = InputImage.fromBitmap(bitmap, 0)
                val result = textRecognizer.process(image).await()
                
                val allText = result.text
                val phoneNumbers = phoneNumberParser.extractPhoneNumbers(allText)
                val formattedNumbers = phoneNumbers.map { phoneNumberParser.formatPhoneNumber(it) }
                
                Result.success(
                    RecognitionResult(
                        recognizedText = allText,
                        phoneNumbers = phoneNumbers.filter { phoneNumberParser.isValidPhoneNumber(it) },
                        formattedPhoneNumbers = formattedNumbers.filter { phoneNumberParser.isValidPhoneNumber(it) }
                    )
                )
            } catch (e: Exception) {
                Result.failure(RecognitionException("OCR识别失败: ${e.message}", e))
            }
        }
    }
    
    suspend operator fun invoke(image: InputImage): Result<RecognitionResult> {
        return withContext(Dispatchers.IO) {
            try {
                val result = textRecognizer.process(image).await()
                
                val allText = result.text
                val phoneNumbers = phoneNumberParser.extractPhoneNumbers(allText)
                val formattedNumbers = phoneNumbers.map { phoneNumberParser.formatPhoneNumber(it) }
                
                Result.success(
                    RecognitionResult(
                        recognizedText = allText,
                        phoneNumbers = phoneNumbers.filter { phoneNumberParser.isValidPhoneNumber(it) },
                        formattedPhoneNumbers = formattedNumbers.filter { phoneNumberParser.isValidPhoneNumber(it) }
                    )
                )
            } catch (e: Exception) {
                Result.failure(RecognitionException("OCR识别失败: ${e.message}", e))
            }
        }
    }
}

class RecognitionException(message: String, cause: Throwable? = null) : Exception(message, cause)