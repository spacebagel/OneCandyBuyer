package com.vegan.onecandybuyer

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vegan.onecandybuyer.ui.theme.OneCandyBuyerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OneCandyBuyerTheme {
                var showDialog by remember { mutableStateOf(true) }

                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🍬 OneCandyBuyer",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        val context = LocalContext.current

                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://www.auchan.ru/catalog/konditerskie-izdeliya/konfety-karamel/vesovye-konfety-karamel/")
                            }
                            context.startActivity(intent)
                        }) {
                            Text("Каталог")
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Числовое значение со штрих-кода:")

                        var barcodeValueTb by remember { mutableStateOf("") }
                        val maxBarcodeValueLength = 6
                        val maxCandyWeightValueLength = 5
                        val prefixBarcode = 2
                        var barcodeValue by remember { mutableStateOf("") }

                        TextField(
                            value = barcodeValueTb,
                            onValueChange = { newText ->
                                if (newText.length <= maxBarcodeValueLength) {
                                    barcodeValueTb = newText
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Штрих-код") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Значение с весов:")

                        var candyWeightTb by remember { mutableStateOf("") }
                        TextField(
                            value = candyWeightTb,
                            onValueChange = { newText ->
                                if (newText.length <= maxCandyWeightValueLength) {
                                    candyWeightTb = newText
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Вес в граммах") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        var barcodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
                        val isInputValid = barcodeValueTb.isNotEmpty() && candyWeightTb.isNotEmpty()

                        Button(onClick = {
                            val preFinalBarcode = prefixBarcode.toString() +
                                    "0".repeat(maxBarcodeValueLength - barcodeValueTb.length) +
                                    barcodeValueTb +
                                    "0".repeat(maxCandyWeightValueLength - candyWeightTb.length) +
                                    candyWeightTb

                            barcodeValue =  preFinalBarcode + BarcodeUtils.getControlSum(preFinalBarcode).toString()
                            barcodeBitmap = BarcodeUtils.generateBarcode(barcodeValue)
                        },
                            enabled = isInputValid
                            ) {
                            Text("Получить штрих-код")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        barcodeBitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(), contentDescription = "Barcode")
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = barcodeValue)
                    }
                    if (showDialog) {
                        UserAgreement().ShowAlert(
                            onDismiss = { showDialog = false },
                            onNoClick = { finish() }
                        )
                    }
                }
            }
        }
    }
}