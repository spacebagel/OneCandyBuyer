package com.vegan.onecandybuyer

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class BarcodeUtils {
    companion object {
        fun generateBarcode(value: String): Bitmap? {
            return try {
                val writer = com.google.zxing.oned.EAN13Writer()
                val bitMatrix: BitMatrix = writer.encode(value, BarcodeFormat.EAN_13, 600, 300)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(
                            x,
                            y,
                            if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
                        )
                    }
                }
                bitmap
            } catch (e: WriterException) {
                e.printStackTrace()
                null
            }
        }

        fun getControlSum(barcode: String): Int {
            if (barcode.isEmpty()) return 0

            var sumEven = 0
            var sumOdd = 0

            for (i in barcode.indices) {
                val digit = barcode[i].digitToInt()
                if ((i + 1) % 2 == 0) {
                    sumEven += digit
                } else if (i != barcode.length - 1) {
                    sumOdd += digit
                }
            }

            val totalSum = sumEven * 3 + sumOdd
            val remainder = totalSum % 10

            return if (remainder != 0) 10 - remainder else 0
        }
    }
}